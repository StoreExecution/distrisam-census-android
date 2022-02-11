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
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.event.DownloadEvent;
import com.storexecution.cocacola.event.DownloadFinishedEvent;
import com.storexecution.cocacola.model.RTMSalepoint;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.ui.rtm.RTMMapActivity;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.GsonUtil;
import com.storexecution.cocacola.util.GsonUtils;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.annotation.Nullable;

import io.realm.Realm;

/**
 * Created by Koceila on 15/05/2017.
 */

public class DownloadService extends Service {
    Handler mHandler = new Handler();
    final int MAX_RETRIES = 30;
    int RETRIES = 0;
    final int NOTIFICATION_ID = 2531;
    final int RANDOM = 2348;
    Vibrator v;
    Context context;
    Uri notification;
    Ringtone r;
    private NotificationManager notificationManager;

    public String fileName = "";

    User user;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;
    Criteria criteria;
    private PowerManager.WakeLock mWakeLock = null;
    private WifiManager.WifiLock mWifiLock = null;
    String source;

    String progress;
    boolean inditerminate;
    String msg;

    boolean noimages;
    Realm realm;


    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        realm = Realm.getDefaultInstance();
        progress = "";
        msg = "Téléchargement...";
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        savePath = getFilesDir().getAbsolutePath() + File.separator;
        String savePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "cocacola" + File.separator + "rtm" + File.separator;
        user = realm.where(User.class).findFirst();

        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                EventBus.getDefault().post(new DownloadEvent(progress, msg));

                mHandler.postDelayed(this, 1000);
            }

        }, 1000);


    }


    String savePath;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //   super.onStartCommand(intent, flags, startId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(NOTIFICATION_ID, getNotification());

        notificationManager.notify(NOTIFICATION_ID, getNotification());
        String url;
        if (BuildConfig.DEBUG) {
            url = Constants.URL_RTM_TEST + user.getId();
        } else {
            url = Constants.URL_RTM + user.getId();

        }

        Log.e("sourceurl", url);

        donwload_affectation(url);
        return START_STICKY;
    }

    private void donwload_affectation(String url) {


        setWifiLock(context, WifiManager.WIFI_MODE_FULL_HIGH_PERF);
        stayAwake(true);

        wifiLock(true);

        Log.e("url", url);

        final File f = new File(savePath + "affectation.json");
        if (f.exists())
            f.delete();
        FileDownloader.getImpl().create(url)

                .setPath(savePath + "affectation.json")
                .setAutoRetryTimes(20)
                .setForceReDownload(true)


                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.e("totalp", totalBytes + " ");


                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {


                        progress = String.format("%.2f", ((float) soFarBytes) / (1024 * 1024));
                        progress += " MB";
                        //   notificationManager.notify(NOTIFICATION_ID, getNotification());
                        Log.e("progress", progress);
                        //   Log.w("retry", task.getRetryingTimes() + " ");
                    }


                    @Override
                    protected void completed(BaseDownloadTask task) {

                        progress = String.format("%.2f", ((float) task.getSmallFileSoFarBytes()) / (1024 * 1024));
                        progress += " MB";

                        wifiLock(false);
                        new ParseJsonTask().execute(task.getPath());

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e("err", "error");
                        e.printStackTrace();
                        wifiLock(false);
                        mHandler.removeCallbacksAndMessages(null);
                        EventBus.getDefault().post(new DownloadFinishedEvent(false));
                        msg = "Erreur de connexion ";
                        notificationManager.notify(NOTIFICATION_ID, getNotification());
                        stopService();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        wifiLock(false);
                        Log.e("warn", "warn");
                        msg = "Erreur";
                        notificationManager.notify(NOTIFICATION_ID, getNotification());
                        stopForeground(false);
                        mHandler.removeCallbacksAndMessages(null);
                        EventBus.getDefault().post(new DownloadFinishedEvent(false));
                        stopService();
                    }
                }).start();


    }

    private class ParseJsonTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            msg = "Mise a jour";
            progress = "";
            notificationManager.notify(NOTIFICATION_ID, getNotification());
            Log.e("begin ", "begin ");
        }

        protected Integer doInBackground(String... paths) {

            ArrayList<RTMSalepoint> affectations = new ArrayList<>();
            ArrayList<String> json = new ArrayList<>();
           Realm realm2 = Realm.getDefaultInstance();
            realm2.beginTransaction();
            realm2.where(RTMSalepoint.class).findAll().deleteAllFromRealm();
            realm2.commitTransaction();

            int size = 0;
            int index = 1;
            try {
                FileInputStream fis = new FileInputStream(paths[0]);
                Reader isr = new InputStreamReader(fis, "UTF-8");
                JsonReader reader = new JsonReader(isr);
                Gson gson = new GsonBuilder()

                        .create();


                // Read file in stream mode
                reader.beginArray();


                while (reader.hasNext()) {
                    // Read data into object model
                    RTMSalepoint investigation = gson.fromJson(reader, RTMSalepoint.class);
                    json.add(GsonUtils.rtmSalepointToJson(investigation));
                    affectations.add(investigation);
                    //   Log.e("name", investigation.getSalepointName());
                    size += 1;
                    if (size == 1) {
                        //   Log.e("reader", reader.nextString() + " ");

                    }
                    if (affectations.size() >= 500) {
                        realm2.beginTransaction();
                        realm2.insertOrUpdate(affectations);
                        realm2.commitTransaction();
                        affectations.clear();
                        json.clear();
                    }
                    //  affectations.add(investigation);

                }
                reader.endArray();
                reader.close();

                if (affectations.size() > 0) {
                    realm2.beginTransaction();
                    realm2.insertOrUpdate(affectations);
                    realm2.commitTransaction();
                    affectations.clear();
                    json.clear();
                }

            } catch (IOException ex) {

            }


            Log.e("size ", size + " ");
            return affectations.size();
        }


        protected void onPostExecute(Integer result) {


            stayAwake(false);
            msg = "Mise a jour términer";
            notificationManager.notify(NOTIFICATION_ID, getNotification());
            mHandler.removeCallbacksAndMessages(null);
            EventBus.getDefault().post(new DownloadFinishedEvent(false));
            stopService();
            Log.e("finished ", " finished");
        }
    }


    public void stayAwake(boolean awake) {
        if (mWakeLock != null) {
            if (awake && !mWakeLock.isHeld()) {
                mWakeLock.acquire();

            } else if (!awake && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }

    }

    public void setWifiLock(Context context, int mode) {
        boolean washeld = false;
        if (mWifiLock != null) {
            if (mWifiLock.isHeld()) {
                washeld = true;
                mWifiLock.release();
            }
            mWifiLock = null;
        }
        mWifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(mode, RTMMapActivity.class.getName() + " Wifi");
        if (washeld) {
            mWifiLock.acquire();
        }

    }

    public void wifiLock(Boolean awake) {
        if (mWifiLock != null) {
            if (awake && !mWifiLock.isHeld()) {
                mWifiLock.acquire();

            } else if (!awake && mWifiLock.isHeld()) {
                mWifiLock.release();
            }
        }


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        String message = "Mise a jour de la base de données";


        return notification.setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message)

                .setAutoCancel(false)
                .setContentText(msg + "  ").build();


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

    private void stopService() {

        stopForeground(false);
        notificationManager.cancel(NOTIFICATION_ID);
        mHandler.removeCallbacksAndMessages(null);
        stopSelf();
    }


}
