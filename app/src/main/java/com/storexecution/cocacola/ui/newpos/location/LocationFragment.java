package com.storexecution.cocacola.ui.newpos.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pappin.mbs.MaterialBarcodeScanner;
import com.github.pappin.mbs.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Commune;
import com.storexecution.cocacola.model.Daira;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.model.Wilaya;
import com.storexecution.cocacola.ui.newpos.fridge.FridgeFragment;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.DistanceCalculator;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class LocationFragment extends Fragment implements LocationListener, LocationValidationCallbackInterface {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.etLang)
    EditText etLang;
    @BindView(R.id.etLat)
    EditText etLat;
    @BindView(R.id.spCommune)
    Spinner spCommune;
    @BindView(R.id.etAdress)
    EditText etAdress;
    @BindView(R.id.rgClosed)
    RadioGroup rgClosed;
    @BindView(R.id.rbClosedYes)
    RadioButton rbClosedYes;
    @BindView(R.id.rbClosedNo)
    RadioButton rbClosedNo;
    @BindView(R.id.llReasons)
    LinearLayout llReasons;
    @BindView(R.id.spReasons)
    Spinner spReasons;
    @BindView(R.id.llRefuse)
    LinearLayout llRefuse;
    @BindView(R.id.rgInterview)
    RadioGroup rgInterview;
    @BindView(R.id.rbInterviewYes)
    RadioButton rbInterviewYes;
    @BindView(R.id.rbInterviewNo)
    RadioButton rbInterviewNo;
    @BindView(R.id.llRefuseReasons)
    LinearLayout llRefuseReasons;
    @BindView(R.id.spRefuseReasons)
    Spinner spRefuseReasons;
    @BindView(R.id.etOtherReason)
    EditText etOtherReason;
    @BindView(R.id.etBarCode)
    TextView etBarCode;
    @BindView(R.id.barCode)
    ImageView barCode;
    @BindView(R.id.btgps)
    com.kusu.loadingbutton.LoadingButton btgps;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    @BindView(R.id.tvPos)
    TextView tvPos;

    /**
     * ButterKnife Code
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    LocationManager locationManager;
    Session session;
    Salepoint salepoint;
    Realm realm;
    Toast toast;
    Photo photo;
    ArrayList<String> communesnames;
    RealmList<Commune> communes;
    RealmList<Daira> dairas;
    User user;
    boolean first;
    Location firstLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        initCommunesSpinner(user.getWilaya());
        if (salepoint.getCommune() > 0) {

            Commune commune = realm.where(Commune.class).equalTo("id", salepoint.getCommune()).findFirst();
            int index = communesnames.indexOf(commune.getName());
            if (index != -1) {
                spCommune.setSelection(index);
            } else
                spCommune.setSelection(0);
        }


        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                UUID.randomUUID().toString());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        etBarCode.setEnabled(false);
        etLang.setEnabled(false);
        etLat.setEnabled(false);
        etLat.setText(String.valueOf(salepoint.getLatitude()));
        etLang.setText(String.valueOf(salepoint.getLongitude()));
        Log.e("landmark", salepoint.getLandmark() + " ");
        etAdress.setText(salepoint.getLandmark());
        etBarCode.setText(salepoint.getBarcode());
        photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_POS).findFirst();
        if (photo != null)
            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));

        if (salepoint.getClosed() == 1) {
            llReasons.setVisibility(View.VISIBLE);
            rbClosedYes.setChecked(true);
            llRefuse.setVisibility(View.GONE);
            spRefuseReasons.setSelection(0);
            spReasons.setSelection(salepoint.getClosedReason());
        } else if (salepoint.getClosed() == 0) {
            llReasons.setVisibility(View.GONE);
            rbClosedNo.setChecked(true);
            spReasons.setSelection(0);
            llRefuse.setVisibility(View.VISIBLE);
            if (salepoint.getRefuse() == 1) {
                rbInterviewNo.setChecked(true);
                llRefuseReasons.setVisibility(View.VISIBLE);
                spRefuseReasons.setSelection(salepoint.getRefuseReason());
                etOtherReason.setText(salepoint.getOtherRefuseReason());
            } else if (salepoint.getRefuse() == 0) {

                rbInterviewYes.setChecked(true);
                llRefuseReasons.setVisibility(View.GONE);
                spRefuseReasons.setSelection(0);
            }

        } else {

        }

        rgClosed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbClosedYes.getId() == i) {
                    llReasons.setVisibility(View.VISIBLE);

                    spReasons.setSelection(salepoint.getClosedReason());
                    llRefuse.setVisibility(View.GONE);
                    llRefuseReasons.setVisibility(View.GONE);
                    spRefuseReasons.setSelection(0);
                    salepoint.setRefuseReason(0);
                    spRefuseReasons.setSelection(0);
                    salepoint.setRefuse(0);
                } else if (rbClosedNo.getId() == i) {
                    llReasons.setVisibility(View.GONE);
                    rbClosedNo.setChecked(true);
                    spReasons.setSelection(0);
                    salepoint.setClosedReason(0);
                    llRefuse.setVisibility(View.VISIBLE);
                    //llRefuse.setVisibility(View.VISIBLE);
                }
            }
        });

        rgInterview.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbInterviewYes.getId() == i) {
                    spRefuseReasons.setSelection(0);
                    salepoint.setRefuse(0);
                    llRefuseReasons.setVisibility(View.GONE);


                } else if (rbInterviewNo.getId() == i) {
                    spRefuseReasons.setSelection(0);
                    salepoint.setRefuse(1);
                    llRefuseReasons.setVisibility(View.VISIBLE);
                }
            }
        });
        checkNotification();

        return v;
    }

    private void initCommunesSpinner(int wilayaid) {
        communes = new RealmList<>();
        communesnames = new ArrayList<>();
        communesnames.add("");

        Wilaya wilaya = realm.where(Wilaya.class).equalTo("id", wilayaid).findFirst();

        dairas = wilaya.getDairas();
        for (Daira daira : dairas)
            communes.addAll(daira.getCommunes());
        for (Commune commune : communes)
            communesnames.add(commune.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, communesnames);
        spCommune.setAdapter(adapter);

    }

    private int getCommuneId(int wilayaId, String name) {
        Wilaya wilaya = realm.where(Wilaya.class).equalTo("id", wilayaId).findFirst();
        // wilaya.getDairas().forEach();
        RealmResults<Commune> communes = realm.where(Commune.class).equalTo("name", name).findAll();

        for (Commune commune : communes) {
            Daira daira = realm.where(Daira.class).equalTo("communes.id", commune.getId()).findFirst();
            if (wilaya.getDairas().contains(daira)) {
                return commune.getId();
            }
            // if(commune.get)
        }

        return 0;

    }


    @OnClick(R.id.btgps)
    public void getLocation() {
        first = true;

        if (locationManager == null && isAdded())
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsActive = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsActive) {
            btgps.showLoading();
            gpsLocal();

        } else {
            makeToast("Veuillez activer votre GPS");
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

        locationManager.requestLocationUpdates(provider, 0, 1, this);


    }

    public static void showFragment(Fragment fragment, FragmentManager fragmentManager, boolean withAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (withAnimation) {
            transaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e("accurency", location.getAccuracy() + " ");
        if (location.getAccuracy() != 0.0 && location.getAccuracy() < 10.0) {

            if (first) {

                firstLocation = location;
                first = false;
            } else {

                if (!location.isFromMockProvider()) {

                    Log.e("distance", DistanceCalculator.getDistance(
                            location.getLatitude(),
                            location.getLongitude(),
                            firstLocation.getLatitude(),
                            firstLocation.getLongitude()) + " ");
                    if (DistanceCalculator.getDistance(
                            location.getLatitude(),
                            location.getLongitude(),
                            firstLocation.getLatitude(),
                            firstLocation.getLongitude())
                            <= 4) {

                        btgps.setEnabled(true);
                        btgps.hideLoading();


                        if (wakeLock != null && wakeLock.isHeld())
                            wakeLock.release();


                        if (locationManager != null) {
                            locationManager.removeUpdates(this);
                            locationManager = null;
                        }

                        MapFragmentDialog fragmentDialog = new MapFragmentDialog();
                        fragmentDialog.setLocation(location);
                        fragmentDialog.setTargetFragment(LocationFragment.this, 1223);

                        if (getActivity() != null)
                            showFragment(fragmentDialog, getActivity().getSupportFragmentManager(), true);


                    } else {
                        firstLocation = location;
                    }
                } else {
                    btgps.setEnabled(true);
                    btgps.hideLoading();
                    Toasty.error(getActivity(), "Fausse position detecter", Toasty.LENGTH_LONG).show();

                }


            }


        } else {
            if (getActivity() != null)
                Toasty.info(getActivity(), "Precision " + location.getAccuracy() + " m", 4000);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @OnClick(R.id.fabNext)
    public void save() {
        if (check()) {
            setData();
            if (locationManager != null) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
            getActivity().finish();
        } else {
            Toasty.error(getContext(), "Veuillez specifier si le magsin est fermé", 5000).show();
        }

    }

    @OnClick(R.id.fabPrev)
    public void prev() {

        if (check()) {
            setData();
            if (locationManager != null) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
            getActivity().onBackPressed();
        } else {
            Toasty.error(getContext(), "Veuillez specifier si le magsin est fermé", 5000).show();
        }


    }

    private void makeToast(String message) {
        if (toast != null)
            toast.cancel();
        if (isAdded()) {
            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
            toast.show();
        }


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
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type, salepoint.getMobile_id(), Constants.IMG_POS));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type, salepoint.getMobile_id(), Constants.IMG_POS));

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
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_POS).findFirst();

            realm.beginTransaction();
            if (photo == null) {
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

                photo.setImageID("pos_" + UUID.randomUUID());

                photo.setTypeID(salepoint.getMobile_id());

                photo.setType(Constants.IMG_POS);
            }

            photo.setDate(DateUtils.todayDateTime() + "");
            photo.setSynced(false);
            photo.setUser_id(user.getId());
            photo.setImage(imageBase64);
            realm.commitTransaction();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto();
    }

    private void setData() {


        Log.e("landmark", etAdress.getText().toString() + " 1");
        salepoint.setBarcode(etBarCode.getText().toString());
        salepoint.setLandmark(etAdress.getText().toString());
        if (rbClosedYes.isChecked()) {
            salepoint.setClosed(1);
            salepoint.setClosedReason(spReasons.getSelectedItemPosition());
        }
        if (rbClosedNo.isChecked()) {
            salepoint.setClosed(0);
            salepoint.setClosedReason(0);
            if (rbInterviewYes.isChecked()) {

                salepoint.setRefuse(0);
                salepoint.setRefuseReason(0);
            } else if (rbInterviewNo.isChecked()) {
                salepoint.setRefuse(1);
                salepoint.setRefuseReason(spRefuseReasons.getSelectedItemPosition());

                salepoint.setOtherRefuseReason(etOtherReason.getText().toString());

            }
            salepoint.setUser_id(user.getId());
        }


        if (spCommune.getSelectedItemPosition() > 0) {

            // int communeId = communes.where().equalTo("name", spCommune.getSelectedItem().toString()).findFirst().getId();
            int communeId = getCommuneId(user.getWilaya(), spCommune.getSelectedItem().toString());
            // Commune commune = realm.where(Commune.class).equalTo("name", spCommune.getSelectedItem().toString()).findFirst();

            //  int id = commune.getId();
            salepoint.setCommune(communeId);
        } else {
            salepoint.setCommune(0);
        }
        session.setSalepoint(salepoint);

    }


    @OnClick(R.id.barCode)
    public void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {

                        if (barcode != null && barcode.rawValue != null) {
                            if (barcode.rawValue.toString().toLowerCase().startsWith("pos")) {
                                etBarCode.setText(barcode.rawValue);
                                salepoint.setBarcode(barcode.rawValue);
                            } else {
                                if (getActivity() != null)
                                    Toasty.error(getActivity(), "Le code a barre n'est pas valide").show();
                            }
                        }
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    private boolean check() {
        if (rgClosed.getCheckedRadioButtonId() == -1)
            return false;

        if (rbClosedYes.isChecked()) {
            if (spReasons.getSelectedItemPosition() == 0)
                return false;
        } else if (rbClosedNo.isChecked()) {
            if (rgInterview.getCheckedRadioButtonId() == -1)
                return false;
            else if (rbInterviewNo.isChecked()) {
                if (spRefuseReasons.getSelectedItemPosition() == 0)
                    return false;
                if (spRefuseReasons.getSelectedItemPosition() == 5)
                    if (etOtherReason.getText().length() == 0)
                        return false;
            }
        }


        return true;
    }


    @Override
    public void setValidLocation(Location location) {
        String Lat = String.valueOf(location.getLatitude() + "");
        String Long = String.valueOf(location.getLongitude() + "");
        etLang.setText(Long);
        etLat.setText(Lat);
        salepoint.setLatitude(location.getLatitude());
        salepoint.setLongitude(location.getLongitude());
        salepoint.setAccurency(location.getAccuracy());
        salepoint.setMock(false);
    }

    @Override
    public void unsetLocation() {
        getLocation();
    }


    private void checkNotification() {
        Notification notification;
        if (salepoint.getNotificationId() != 0)
            notification = realm.where(Notification.class).equalTo("id", salepoint.getNotificationId()).findFirst();
        else
            notification = null;
        if (notification != null) {
            for (ValidationConditon validationConditon : notification.getConditions()) {

                if (validationConditon.getStatus() == 0 && validationConditon.getDataType().equals(Constants.IMG_POS)) {
                    tvPos.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                    Log.e("notification", validationConditon.getDataType() + " ");
                }
            }


        }

    }
}
