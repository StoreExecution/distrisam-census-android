package com.storexecution.cocacola;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateActivity extends AppCompatActivity {
    @BindView(R.id.pbProgress)
    ProgressBar progress;
    @BindView(R.id.tvPercent)
    TextView tvPercent;

    String savePath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "cocacola" + File.separator;
    ;

    boolean downloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        progress.setMax(100);
        progress.setProgress(0);
        progress.setIndeterminate(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        File f = new File(savePath + "app-release.apk");
        if (f.exists())
            f.delete();
        Log.e("url", Constants.URL_UPDATE);
        FileDownloader.getImpl().create(Constants.URL_UPDATE)
                .setPath(savePath + "app-release.apk")
                .setAutoRetryTimes(35)

                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progress.setIndeterminate(true);
                        downloading = true;
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        progress.setIndeterminate(false);
                        // int p = Math.round((((float) soFarBytes) / totalBytes) * 100);
                        String p = String.format("%.2f", ((float) soFarBytes) / (1024 * 1024));
                        //progress.setProgress(p);
                        Log.e("Progress", p + "");
                        tvPercent.setText(p + "MB");
                        downloading = true;
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        progress.setProgress(100);
                        tvPercent.setText(100 + "%");
                        downloading = false;
                        install();
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        downloading = false;
                        tvPercent.setText("Erreur");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

    public void install() {

        //   String filepath = Environment.getExternalStorageDirectory().getPath() + "/appupdate.apk";

        Uri fileLoc;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            fileLoc = Uri.fromFile(new File(savePath + "app-release.apk"));
        } else
            fileLoc = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", new File(savePath + "app-release.apk"));

        Log.e("fileURl", fileLoc.toString() + " ");

       Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);

        //  InputStream in = new FileInputStream(fileLoc);
        intent.setDataAndType(fileLoc, "application/vnd.android.package-archive");

       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       startActivity(intent);



    }

    public static boolean installPackage(Context context, Uri uri, String packageName)
            throws IOException {
        PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(packageName);
        // set params
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
        OutputStream out = session.openWrite("COSU", 0, -1);
        byte[] buffer = new byte[65536];
        int c;
        InputStream in = context.getContentResolver().openInputStream(uri);
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
        session.fsync(out);
        in.close();
        out.close();

        session.commit(createIntentSender(context, sessionId));
        return true;
    }

    public static final String ACTION_INSTALL_COMPLETE
            = "com.storexecution.cocacola.INSTALL_COMPLETE";

    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent(ACTION_INSTALL_COMPLETE),
                0);
        return pendingIntent.getIntentSender();
    }


    @Override
    public void onBackPressed() {
        if (!downloading)
            super.onBackPressed();
        else {


            new MaterialDialog.Builder(this)
                    .title("Mise a jour")
                    .content("voulez-vous arrêter le téléchargement ?")
                    .positiveText("Oui")
                    .negativeText("Non")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            UpdateActivity.this.finish();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();


        }
    }
}
