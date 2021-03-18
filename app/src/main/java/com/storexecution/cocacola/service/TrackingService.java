package com.storexecution.cocacola.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.google.gson.Gson;
import com.storexecution.cocacola.R;

import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.GsonUtils;
import com.storexecution.cocacola.util.LocationUtil;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingService extends Service implements LocationListener {
    Handler mHandler = new Handler();
    final int MAX_RETRIES = 30;
    int RETRIES = 0;
    final int NOTIFICATION_ID = 3215;
    final int RANDOM = 2348;
    Vibrator v;

    Uri notification;
    Ringtone r;
    private NotificationManager notificationManager;

    public String fileName = "";
    private PowerManager.WakeLock mWakeLock = null;
    User user;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;
    Criteria criteria;

    Realm realm;
    Suivi suivi;
    SharedPrefUtil sharedPrefUtil;
    RealmList<Salepoint> salepoints;
    RealmList<Suivi> suivis;
    RealmList<Photo> photos;
    ApiEndpointInterface service;
    RetrofitClient retrofitClient;
    Gson gson;
    Map<String, String> headers;
    boolean syncing = false;
    int synced = 0;
    int size = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sharedPrefUtil = SharedPrefUtil.getInstance(this);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                UUID.randomUUID().toString());
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));


        salepoints = new RealmList<>();
        gson = new Gson();
        suivis = new RealmList<>();
        photos = new RealmList<>();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(NOTIFICATION_ID, getNotification());
        notificationManager.notify(NOTIFICATION_ID, getNotification());
        wakeLock.acquire();

        realm = Realm.getDefaultInstance();
        // user = realm.where(User.class).findFirst();
        Log.e("tracking", "tracking");


        Log.e("tracking", "lunch");
        user = realm.where(User.class).findFirst();


        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
//        criteria.setAltitudeRequired(true);
//        criteria.setBearingRequired(true);
//        criteria.setCostAllowed(true);
//        criteria.setSpeedRequired(false);

        //  criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);

        // criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        //criteria.setPowerRequirement(Criteria.POWER_HIGH);
        /* stop work after 6 minutes */
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//                stopService();
//            }
//
//        }, 4 * 60 * 1000);

        //if (sharedPrefUtil.getBoolean("working", false)) {

        if (LocationUtil.isGpsActive(this))
            gpsLocal();
        else {
            Log.e("Tracking Service", "GPS not Active");
            stopService();
        }

//        } else {
//            Log.e("tracking", "cancel");
//            new AlarmTask(TrackingService.this).cancelAlarm();
//        }

    }

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private void gpsLocal() {


        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null)
            locationManager.requestLocationUpdates(provider, 5000, 5, this);
        else
            stopService();

    }

    @Override
    public void onLocationChanged(Location location) {


        //  if (sharedPrefUtil.getBoolean("logged", false)) {
        Log.e("Tracking Service", "onLocationChanged " + location.getAccuracy());
        if (location.getAccuracy() != 0.0 && location.getAccuracy() < 25.0) {
            suivi = new Suivi();
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
        }


//        } else {
//            Log.e("tracking", "cancel");
//            new AlarmTask(TrackingService.this).cancelAlarm();
//
//        }

        try {
            if (!DateUtils.isTimeBetweenTwoTime(Constants.START_TIME, Constants.END_TIME, DateUtils.currentTime())) {

                mHandler.removeCallbacksAndMessages(null);
                if (locationManager != null)
                    locationManager.removeUpdates(this);
                if (wakeLock != null && wakeLock.isHeld())
                    wakeLock.release();


                if (locationManager != null) {
                    locationManager.removeUpdates(this);
                    locationManager = null;
                }

                stopService();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!syncing)
            sync();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @TargetApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.storexecution.cocacola.service";
        String channelName = "Coca Cola ";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(" MAJ des données utilisateur")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(3, notification);
    }

    private Notification getNotification() {


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        String message = "Coca Cola";


        return notification.setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name) + " Suivi")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(false)
                .setContentText("").build();


    }


    private void stopService() {
        Log.e("Tracking service", "stopService");
        if (locationManager != null)
            locationManager.removeUpdates(this);
        stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
        mHandler.removeCallbacksAndMessages(null);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
            locationManager.removeUpdates(this);
        mHandler.removeCallbacksAndMessages(null);
    }


    public void sync() {
        Log.e("Synchsuivie", "syncing");
        syncing = true;
        salepoints.clear();
        suivis.clear();
        photos.clear();

        synced = 0;
        salepoints.addAll(realm.where(Salepoint.class).equalTo("synced", false).findAll());

        suivis.addAll(realm.where(Suivi.class).equalTo("synced", false).limit(50).findAll());
        photos.addAll(realm.where(Photo.class).equalTo("synced", false).findAll());
        size = salepoints.size() + suivis.size() + photos.size();
        //syncTrackings(suivis, 0);
        syncPhotos(photos, 0);
        //syncSalepoints(salepoints, 0);
    }

    private boolean isAllSurveysAndPhotosSynced() {
        long count = realm.where(Salepoint.class).equalTo("synced", false).count() + realm.where(Photo.class).equalTo("synced", false).count();
        return count == 0;
    }

    private void syncTrackings3(RealmList<Suivi> toSync, int index) {


        Log.e("syncTrackings", index + " ");
        Suivi suivi = new Suivi();
        if (index < toSync.size()) {
            synced++;

            suivi = realm.copyFromRealm(toSync.get(index));


            Log.e("ssuivie", gson.toJson(suivi));

            service.postSuivi(headers, gson.toJson(suivi)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("Synchsuivie", gson.toJson(response.body()) + " " + response.code());

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncTrackings3(toSync, (index + 1));
                        else {

                            syncing = false;
                        }
                    } else {
                        if (response.code() == 400 || response.code() == 500 || response.code() == 403)
                            syncing = false;
                        else
                            syncTrackings3(toSync, (index + 1));


                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size()) {
                        if (isAllSurveysAndPhotosSynced()) {
                            syncTrackings3(toSync, (index + 1));
                        } else {
                            syncing = false;
                        }
                    } else {
                        syncing = false;
                    }

                }
            });
        } else {
            syncing = false;
        }


    }

    int limit = 100;

    private void syncPhotos(RealmList<Photo> toSync, int index) {


        Log.e("syncPhotos", index + " ");
        Photo photo = new Photo();
        if (index < toSync.size()) {
            synced++;

            photo = realm.copyFromRealm(toSync.get(index));
            Photo photo2 = realm.copyFromRealm(toSync.get(index));

            photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.postImage(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("syncPhotos", gson.toJson(response.body()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncPhotos(toSync, (index + 1));
                        else {
                            syncSalepoints(salepoints, 0);

                        }
                    } else {
                        if (response.code() == 400 || response.code() == 500 || response.code() == 403)
                            syncing = false;
                        else
                            syncPhotos(toSync, (index + 1));

                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncPhotos(toSync, (index + 1));
                    else {
                        syncSalepoints(salepoints, 0);

                    }

                }
            });
        } else {
            syncSalepoints(salepoints, 0);

        }


    }


    private void syncSalepoints(RealmList<Salepoint> toSync, int index) {


        Log.e("syncSalepoints", index + " ");

        Salepoint photo = new Salepoint();
        if (index < toSync.size()) {
            synced++;

            photo = realm.copyFromRealm(toSync.get(index));
            Salepoint photo2 = realm.copyFromRealm(toSync.get(index));

            //photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.syncSalepoint(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("Synchsuivie", gson.toJson(response.body()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncSalepoints(toSync, (index + 1));
                        else {
                            if (isAllSurveysAndPhotosSynced())
                                syncTracks();
                            else
                                syncing = false;
                        }
                    } else {
                        //  Toasty.error(getApplicationContext(), "Vous etes deconnecter", 5000).show();
                        if (response.code() == 400 || response.code() == 500 || response.code() == 403)
                            syncing = false;
                        else {
                            syncSalepoints(toSync, (index + 1));
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncSalepoints(toSync, (index + 1));
                    else {
                        if (isAllSurveysAndPhotosSynced())
                            syncTracks();
                        else
                            syncing = false;
                    }

                }
            });
        } else {
            if (isAllSurveysAndPhotosSynced())
                syncTracks();
            else
                syncing = false;

        }


    }


    public synchronized void syncTracks() {
        suivis.clear();
        suivis.addAll(realm.where(Suivi.class).equalTo("synced", false).limit(limit).findAll());
        if (suivis.size() > 0)
            syncTrackings2(suivis);
        else {
            syncing = false;
        }
    }


    private void syncTrackings2(RealmList<Suivi> toSync) {

        ArrayList<Suivi> s = new ArrayList<>(realm.copyFromRealm(toSync));
        Log.e("syncTrackings", GsonUtils.trackingListToJson(s) + " ");
        Suivi suivi = new Suivi();


        // pDialog.setContentText("Sychronisation " + synced + "/" + size);


        service.postSuivi(headers, GsonUtils.trackingListToJson(s)).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                try {
                    Log.e("Synchsuivie", gson.toJson(response.body()) + " " + response.code() + " " + call.request().body().contentLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response.code() == 200) {
                    realm.beginTransaction();
                    for (Suivi suivi1 : toSync)

                        suivi1.setSynced(true);
                    realm.commitTransaction();


                    syncTracks();

                } else {
                    if (response.code() == 400 || response.code() == 500)
                        syncing = false;
                    else
                        syncTracks();


                }

            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                t.printStackTrace();
                syncTracks();

            }
        });
        synced += limit;


    }


}

