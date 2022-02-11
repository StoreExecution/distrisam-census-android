package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.storexecution.cocacola.model.Commune;
import com.storexecution.cocacola.model.Daira;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.PaymentSummary;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.Wilaya;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.receiver.AlarmReceiver;
import com.storexecution.cocacola.service.LocationUpdatesService;
import com.storexecution.cocacola.ui.map.MapActivity;
import com.storexecution.cocacola.ui.newpos.NewSurveyActivity;
import com.storexecution.cocacola.ui.notification.NotificationActivity;
import com.storexecution.cocacola.ui.payment.PaymentsActivity;
import com.storexecution.cocacola.ui.rtm.RTMMapActivity;
import com.storexecution.cocacola.ui.support.SupportActivity;
import com.storexecution.cocacola.ui.sync.SyncActivity;
import com.storexecution.cocacola.util.AlarmTask;
import com.storexecution.cocacola.util.AppVersion;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.LocationUtil;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.cvNewPos)
    androidx.cardview.widget.CardView cvNewPos;
    @BindView(R.id.cvMaps)
    androidx.cardview.widget.CardView cvMaps;
    @BindView(R.id.cvSync)
    androidx.cardview.widget.CardView cvSync;
    @BindView(R.id.cvNotification)
    androidx.cardview.widget.CardView cvNotification;
    @BindView(R.id.cvAssist)
    androidx.cardview.widget.CardView cvAssist;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @BindView(R.id.tvNotificationCount)
    TextView tvNotificationCount;

    @BindView(R.id.llNotificationIdicator)
    LinearLayout llNotificationIdicator;
    Realm realm;
    User user;
    @BindView(R.id.swiperefresh)
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout swiperefresh;
    ApiEndpointInterface service;
    SharedPrefUtil sharedPrefUtil;
    Map<String, String> headers;
    final String TAG_PAYMENTSUMMARY = "paymentSummary";
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    /**
     * ButterKnife Code
     **/

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("onServiceConnected", "onServiceConnected");
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Suivi> ss = realm.where(Suivi.class).findAll();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                for (Suivi s : ss) {
//                    s.setSynced(false);
//                }
//            }
//        });
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotification();

            }
        });
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        sharedPrefUtil = SharedPrefUtil.getInstance(this);
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        user = realm.where(User.class).findFirst();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.FOREGROUND_SERVICE,

                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.REQUEST_INSTALL_PACKAGES


                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        checkAlarm();
        Log.e("REALM", Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()) + " ");
        tvVersion.setText("Version : " + AppVersion.getVersionName(this));
        updateNotifications();

        if (sharedPrefUtil.getBoolean("purge3", true) && AppVersion.getVersionCode(this) == 44) {
//            realm.beginTransaction();
//
//            realm.where(Photo.class).equalTo("synced",true).sort("id", Sort.ASCENDING).limit((realm.where(Photo.class).count() / 2)).findAll().deleteAllFromRealm();
//            // realm.where(Photo.class).lessThan("Date", new Date("2021-07-10 00:00:00")).findAll().deleteAllFromRealm();
//            sharedPrefUtil.putBoolean("purged3", false);
//            Realm.compactRealm(Realm.getDefaultConfiguration());
//            realm.commitTransaction();
        }
        //new AlarmTask(this).run();

        // realm.where(Suivi.class).notEqualTo("jour", DateUtils.todayDate()).and().equalTo("synced", true).findAll().deleteAllFromRealm();

        user = realm.where(User.class).findFirst();
        if (user != null)
            tvUsername.setText(user.getUsername());
        // mService.requestLocationUpdates();
    }

    private void updateNotifications() {
        long count = realm.where(Notification.class).equalTo("status", 0).and().equalTo("userId", user.getId()).count();
        if (count > 0) {

            llNotificationIdicator.setVisibility(View.VISIBLE);
            tvNotificationCount.setVisibility(View.VISIBLE);
            tvNotificationCount.setText(String.valueOf(count));

        } else {
            llNotificationIdicator.setVisibility(View.GONE);
            tvNotificationCount.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  fetchNotification();
    }

    private void fetchNotification() {

        if (user != null)
            service.fetchNotification(headers).enqueue(new Callback<ArrayList<Notification>>() {
                @Override
                public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {

                    if (swiperefresh.isRefreshing())
                        swiperefresh.setRefreshing(false);
                    Log.e("notifs", new Gson().toJson(response.body()));
                    if (response.body() != null) {
                        realm.beginTransaction();
                        realm.delete(Notification.class);
                        realm.insertOrUpdate(response.body());
                        realm.commitTransaction();
                        Toasty.success(HomeActivity.this, "Notification  mis a jour", 3000).show();

                    }
                    updateNotifications();
                }

                @Override
                public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                    Toasty.error(HomeActivity.this, "Erreur", 3000).show();
                    if (swiperefresh.isRefreshing())
                        swiperefresh.setRefreshing(false);

                }
            });
    }

    private void checkAlarm() {
        // Log.e("testAlarm", " in time");

        Intent intent = new Intent(this, AlarmReceiver.class);//the same as up
        // intent.setAction(AlarmReceiver.ACTION_ALARM_RECEIVER);//the same as up
        boolean isWorking = (PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag

        if (!isWorking) {
            Log.e("testAlarm", "test");
            new AlarmTask(this).run();
        } else {

            Log.e("testAlarm", "working");
        }
    }


    @OnClick(R.id.cvNewPos)
    public void newPos() {
        if (LocationUtil.isGpsActive(this)) {
            startActivity(new Intent(this, NewSurveyActivity.class));
        } else {
            Toasty.error(this, "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvMaps)
    public void map() {
        if (LocationUtil.isGpsActive(this)) {
            startActivity(new Intent(this, MapActivity.class));
        } else {
            Toasty.error(this, "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvSync)
    public void synch() {
        if (LocationUtil.isGpsActive(this)) {
            startActivity(new Intent(this, SyncActivity.class));
        } else {
            Toasty.error(this, "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvUpdate)
    public void cvUpdate() {
        startActivity(new Intent(this, RTMMapActivity.class));
    }

    @OnClick(R.id.cvNotification)
    public void cvNotification() {
        //  Toasty.info(getApplicationContext(), "Aucune notification").show();

        startActivity(new Intent(this, NotificationActivity.class));
    }

    @OnClick(R.id.ivLogout)
    public void logout() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Déconnexion")
                .setContentText("Êtes vous sur de vouloir vous déconnecter")
                .setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        triggerRebirth(HomeActivity.this);
                        sweetAlertDialog.dismiss();
                    }
                })
                .setCancelButton("Non", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    public void triggerRebirth(Context context) {
        Toasty.error(getApplicationContext(), "Vous etes deconnecter", 5000).show();
        SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());


        sharedPrefUtil.putString(Constants.ACCESS_TOKEN, "");
        sharedPrefUtil.putString(Constants.TOKEN_TYPE, "");
        // sharedPrefUtil.putInt(Constants.TOKEN_TYPE, loginResponse.getUser()());
        //sharedPrefUtil.putString(Constants.TOKEN_TYPE, loginResponse.getTokenType());
        sharedPrefUtil.putBoolean(Constants.LOGGED, false);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(Intent.KEY_RESTART_INTENT, nextIntent);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        //  Runtime.getRuntime().exit(0);

    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @OnClick(R.id.cvAssist)
    public void cvAssist() {
        startActivity(new Intent(this, PaymentsActivity.class));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
