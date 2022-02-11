package com.storexecution.cocacola.service;

/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.storexecution.cocacola.HomeActivity;
import com.storexecution.cocacola.LoginActivity;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.event.LocationUpdatedEvent;
import com.storexecution.cocacola.event.MockPosition;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.ui.sync.ImageSyncActivity;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.SharedPrefUtil;


import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdatesService extends Service {

    private static final String PACKAGE_NAME =
            "com.storexecution.cocacola.service.LocationUpdatesService";

    private static final String TAG = "resPOINT";

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "coca.channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 9000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    RealmList<Photo> photos;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 2;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    Double latitude, longitude;

    /**
     * The current location.
     */
    private Location mLocation;

    Realm realm;
    User user;
    SharedPrefUtil sharedPrefUtil;

    Gson gson;
    Map<String, String> headers;


    ApiEndpointInterface service;
    boolean syncing;


    /**
     * Realtime location save in firestore or firebase
     */


    @SuppressWarnings("deprecation")
    public LocationUpdatesService() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };


        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        gson = new Gson();
        // updateNotSynced();

        photos = new RealmList<>();
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            //  removeLocationUpdates();
            //stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged");
        //  mChangingConfiguration = true;
    }


    @SuppressWarnings("deprecation")
    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;

        // Register Firestore when service will restart
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        sharedPrefUtil = SharedPrefUtil.getInstance(this);
        requestLocationUpdates();
        return mBinder;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;

        // Register Firestore when service will restart
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        sharedPrefUtil = SharedPrefUtil.getInstance(this);

        requestLocationUpdates();
        super.onRebind(intent);
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");
        Log.i(TAG, Utils.requestingLocationUpdates(this) + " ");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.d(TAG, "Starting foreground service");

            // TODO(developer). If targeting O, use the following code.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.e(TAG, "startForegroundService");
//                ContextCompat.startForegroundService(this,new Intent(this,
//                        LocationUpdatesService.class));
//            } else {
//                startForeground(NOTIFICATION_ID, getNotification());
//
//            }

            try {
                startForeground(NOTIFICATION_ID, getNotification());

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.d(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.d(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Log.e(TAG, "getNotification");
        Intent intent = new Intent(this, LocationUpdatesService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentText("Mise a jour des données")
                .setContentTitle("Coca-Cola")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)

                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }


        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.d(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.d(TAG, "New location: " + location);

        mLocation = location;
        if (!syncing) {
            photos.clear();
            photos.addAll(realm.where(Photo.class).equalTo("synced", false).and().notEqualTo("error", true).findAll());
            if (photos.size() > 0)
                syncPhotos(photos, 0);
        }
        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        //  LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            // mNotificationManager.notify(NOTIFICATION_ID, getNotification());

            // Getting location when notification was call.
            latitude = location.getLatitude();
            longitude = location.getLongitude();


            // Here using to call Save to serverMethod
            SavetoServer();

        }

        try {
            if (DateUtils.isTimeBetweenTwoTime(Constants.START_TIME, Constants.END_TIME, DateUtils.currentTime())) {

                if (location.getAccuracy() != 0.0 && location.getAccuracy() < 16.0) {

                    if (!location.isFromMockProvider()) {
                        Suivi suivi = new Suivi();
                        suivi.setMobile_Id(UUID.randomUUID().toString());
                        suivi.setHeur(DateUtils.currentTime());
                        suivi.setJour(DateUtils.todayDate());
                        suivi.setType("mid");
                        if (user != null)
                            suivi.setUser(user.getId());
                        suivi.setMilis(DateUtils.currentMilis());
                        suivi.setLatitude(location.getLatitude());
                        suivi.setLongitude(location.getLongitude());
                        suivi.setAccurency(location.getAccuracy());
                        suivi.setIsmock(location.isFromMockProvider());


                        realm.beginTransaction();
                        realm.insertOrUpdate(suivi);
                        realm.commitTransaction();
                    } else {

                        EventBus.getDefault().post(new MockPosition());

                    }

                    //    Toasty.success(this, "va :" + location.getAccuracy() + " ", Toasty.LENGTH_LONG).show();
                }
            } else {
                removeLocationUpdates();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(0)
                .setWaitForAccurateLocation(true);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Save a value in realtime to firestore when user in background
     * For foreground you have to call same method to activity
     */

    private void SavetoServer() {

    }


    private void syncPhotos(RealmList<Photo> toSync, int index) {

        syncing = true;
        Log.e("syncPhotos", index + " ");
        Photo photo = new Photo();
        if (index < toSync.size()) {


            photo = realm.copyFromRealm(toSync.get(index));
            Photo photo2 = realm.copyFromRealm(toSync.get(index));

            if (photo.getImage().getBytes().length >= (1024 * 500) || photo.isError()) {
                Bitmap img = Base64Util.Base64ToBitmap(photo.getImage(), 1);
                img = getResizedBitmap(img, 700);
                photo.setImage(Base64Util.bitmapToBase64String(img, 90));
            }

            final String photostring = photo.getImage();


            photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.postImage(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("syncPhotos", gson.toJson(response.body()) + " ");
                    Log.e("syncPhotos", gson.toJson(response.code()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();


                        toSync.get(index).setSynced(true);

                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncPhotos(toSync, (index + 1));
                        else {
                            syncing = false;

                        }
                    } else if (response.code() == 403) {
                        realm.beginTransaction();
                        if (!toSync.get(index).isError()) {


                            toSync.get(index).setSynced(false);
                            toSync.get(index).setError(true);
                            syncing = false;
                        } else {
                            syncing = false;
                            //   toSync.get(index).setImage(photostring);
                        }

                        realm.commitTransaction();


                    } else {

                        if (response.code() == 400 || response.code() == 500) {
                            {
                                syncing = false;
                            }
                        } else
                            syncPhotos(toSync, (index + 1));

                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {

                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncPhotos(toSync, (index + 1));
                    else {

                        syncing = false;
                    }

                }
            });
        } else {
            // syncSalepoints(salepoints, 0);
            syncing = false;
        }


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}