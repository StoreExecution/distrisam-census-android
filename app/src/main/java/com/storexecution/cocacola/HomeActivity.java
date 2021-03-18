package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.storexecution.cocacola.model.Commune;
import com.storexecution.cocacola.model.Daira;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.Wilaya;
import com.storexecution.cocacola.receiver.AlarmReceiver;
import com.storexecution.cocacola.ui.map.MapActivity;
import com.storexecution.cocacola.ui.newpos.NewSurveyActivity;
import com.storexecution.cocacola.ui.support.SupportActivity;
import com.storexecution.cocacola.ui.sync.SyncActivity;
import com.storexecution.cocacola.util.AlarmTask;
import com.storexecution.cocacola.util.AppVersion;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.LocationUtil;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {

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
    Realm realm;
    User user;


    /**
     * ButterKnife Code
     **/
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


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
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
        tvVersion.setText("Version : " + AppVersion.getVersionName(this));

        //new AlarmTask(this).run();
        try {
            InputStream inputStream = getAssets().open("algeria_cities.json");

            realm.beginTransaction();
            realm.delete(Wilaya.class);
            realm.delete(Daira.class);
            realm.delete(Commune.class);
            realm.createOrUpdateAllFromJson(Wilaya.class, inputStream);
            realm.commitTransaction();

            Wilaya wilaya = realm.where(Wilaya.class).equalTo("id", 16).findFirst();
            Daira daira = wilaya.getDairas().first();
            Log.e("wila", daira.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        user = realm.where(User.class).findFirst();
        if (user != null)
            tvUsername.setText(user.getUsername());
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
        startActivity(new Intent(this, UpdateActivity.class));
    }

    @OnClick(R.id.cvNotification)
    public void cvNotification() {
        Toasty.info(getApplicationContext(), "Aucune notification").show();
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

    @OnClick(R.id.cvAssist)
    public void cvAssist() {
        startActivity(new Intent(this, SupportActivity.class));
    }
}
