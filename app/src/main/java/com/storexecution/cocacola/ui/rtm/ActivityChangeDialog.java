package com.storexecution.cocacola.ui.rtm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.ActivityChange;
import com.storexecution.cocacola.model.Photo;

import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.DistanceCalculator;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.SharedPrefUtil;
import com.storexecution.cocacola.util.UtilBase64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityChangeDialog extends DialogFragment implements LocationListener {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.spType)
    Spinner spType;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btgps)
    com.kusu.loadingbutton.LoadingButton btgps;
    @BindView(R.id.btValidate)
    Button btValidate;
    /**
     * ButterKnife Code
     **/
    Realm realm;
    Photo photo;
    String base64Photo;

    ActivityChange activityChange;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;

    public int rtmId;

    Gson gson;
    Map<String, String> headers;
    SharedPrefUtil sharedPrefUtil;
    User user;

    ApiEndpointInterface service;
    SweetAlertDialog sweetAlertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_change_dialog, container, false);
        ButterKnife.bind(this, v);


        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                UUID.randomUUID().toString());
        activityChange = new ActivityChange();
        activityChange.setMobileId(UUID.randomUUID().toString());
        activityChange.setMobileDate(DateUtils.todayDateTime());

        activityChange.setRtmId(rtmId);
        Log.e("RTM", rtmId + " ");

        gson = new Gson();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        sharedPrefUtil = SharedPrefUtil.getInstance(getActivity());
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public int getRtmId() {
        return rtmId;
    }

    public void setRtmId(int rtmId) {
        this.rtmId = rtmId;
    }

    @OnClick(R.id.btgps)
    public void getLocation() {


        if (locationManager == null && isAdded())
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsActive = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsActive) {
            btgps.showLoading();
            gpsLocal();

        } else {
            Toasty.info(getActivity(), "Veuillez activer votre GPS", 3000).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void gpsLocal() {

        //makeToast("Vous Utiliser le GPS ,cela peut prendre plusieurs minutes");
        Toasty.info(getActivity(), "Vous Utiliser le GPS ,cela peut prendre plusieurs minutes").show();

        btgps.setEnabled(false);
        wakeLock.acquire();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        if (locationManager == null && isAdded())
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);

        locationManager.requestLocationUpdates(provider, 1, 1, this);


    }


    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;

    public void takePhoto() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("fileuri", fileUri.getPath() + " ");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
       /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File(fileUri.getPath())));*/
        // start the image capture Intent
        this.startActivityForResult(intent, 1234);
    }

    public Uri getOutputMediaFileUri(int type) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type, activityChange.getMobileId(), Constants.IMG_ACTIVITYCHANGE));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type, activityChange.getMobileId(), Constants.IMG_ACTIVITYCHANGE));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.e("resultcode",resultCode+" /");
        if (resultCode == Activity.RESULT_OK) {
            // successfully captured the image
            // display it in image view
            //   Uri uri = data.getData();
            if (data != null)
                previewCapturedImage(requestCode, data.getData());
            else {
                previewCapturedImage(requestCode, null);
            }
        } else {
            // failed to capture image
            Toast.makeText(getActivity(),
                    "veuillez réessayer", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void previewCapturedImage(int requestCode, Uri uri) {
        try {
            //Log.e("fileuri2",uri.getPath()+" ");
            //Log.e("fileuri3",fileUri.getPath()+" ");

            // Log.e("fileuri3",getRealPathFromURI(fileUri));
            String path;
            if (uri == null)
                path = fileUri.getPath();
            else
                fileUri = uri;


            //    plvi.setToken(plv.getToken());

            //   if (uri != null)
            // path = getRealPathFromURI(uri);

            // Log.e("uri", path);
            final Bitmap resized = ImageLoad.decodeScaledBitmapFromSdCard(fileUri, 800, 600, getActivity());

            Bitmap mutableBitmap = resized.copy(Bitmap.Config.RGB_565, true);


            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setTextSize(24);
            paint.setTextAlign(Paint.Align.LEFT);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            canvas.drawText("Date: " + DateUtils.todayDateTime(), 20, 35, paint);

       /*     ivPlv.setImageResource(android.R.color.transparent);
            PicassoSingleton.with(getActivity())
                    .load("file://" + fileUri.getPath())
                    .resize(250, 250)
                    .noFade()
                    .into(ivPlv);*/
            ivPhoto.setImageBitmap(mutableBitmap);
            base64Photo = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() != 0.0 && location.getAccuracy() < 10.0) {

            btgps.setEnabled(true);
            btgps.hideLoading();


            if (wakeLock != null && wakeLock.isHeld())
                wakeLock.release();


            if (locationManager != null) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
            activityChange.setLatitude(location.getLatitude());
            activityChange.setLongitude(location.getLongitude());
            btgps.setBackgroundColor(getActivity().getResources().getColor(R.color.main_green_color));
            btgps.setButtonColor(getActivity().getResources().getColor(R.color.main_green_color));


        } else {
            if (getActivity() != null)
                Toasty.info(getActivity(), "Precision " + location.getAccuracy() + " m", 4000);
        }
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


    private boolean check() {
        boolean valid = true;
        if (base64Photo == null && base64Photo.length() == 0)
            return false;
        if (spType.getSelectedItemPosition() == 0)
            return false;
        if (activityChange.getLatitude() == 0.0 || activityChange.getLongitude() == 0.0)
            return false;


        return valid;

    }

    @OnClick(R.id.btValidate)
    public void validate() {

        if (check()) {

            sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitle("Synchronisation ...");
            sweetAlertDialog.show();


            realm.beginTransaction();
            if (photo == null)
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("activity_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            photo.setTypeID(activityChange.getMobileId());
            photo.setImage(base64Photo);
            photo.setType(Constants.IMG_ACTIVITYCHANGE);


            activityChange.setComment(etComment.getText().toString());
            activityChange.setType(spType.getSelectedItemPosition());
            realm.insertOrUpdate(activityChange);

            realm.commitTransaction();

            sync();

        } else {


            Toasty.error(getActivity(), "Veuillez saisir tous les champs obiligatoires", 3000).show();
        }

    }

    private void close() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
            sweetAlertDialog.dismiss();
        ((RTMMapActivity) getActivity()).setLandscap();
        getFragmentManager().popBackStack();
    }


    private void sync() {


        service.postActivityChange(headers, gson.toJson(activityChange)).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                if (response.body() != null && !response.body().isError()) {

                    realm.beginTransaction();
                    activityChange = realm.where(ActivityChange.class).equalTo("mobileId", activityChange.getMobileId()).findFirst();
                    activityChange.setSynced(true);
                    realm.insertOrUpdate(activityChange);
                    realm.commitTransaction();
                    Toasty.success(getActivity(), "Synchronisation reussi", 2000).show();
                } else {

                    Toasty.warning(getActivity(), "Erreur de synchronisation", 2000).show();

                }

                close();
            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                Toasty.error(getActivity(), "Erreur de connexion", 2000).show();
                close();
            }
        });

    }
}
