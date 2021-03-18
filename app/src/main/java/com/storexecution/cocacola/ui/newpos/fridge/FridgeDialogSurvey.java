package com.storexecution.cocacola.ui.newpos.fridge;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pappin.mbs.MaterialBarcodeScanner;
import com.github.pappin.mbs.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.storexecution.FridgeDialogCallbackInterface;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.UtilBase64;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FridgeDialogSurvey extends DialogFragment {

    public FridgeDialogSurvey() {
        setCancelable(false);
    }

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.llFridge)
    LinearLayout llFridge;
    @BindView(R.id.rgAbused)
    RadioGroup rgAbused;
    @BindView(R.id.rbAbusedYes)
    RadioButton rbAbusedYes;
    @BindView(R.id.rbrbAbusedNo)
    RadioButton rbrbAbusedNo;
    @BindView(R.id.spFridgeOwner)
    Spinner spFridgeOwner;
    @BindView(R.id.spFridgeDate)
    Spinner spFridgeDate;
    @BindView(R.id.spFridgeModel)
    Spinner spFridgeModel;
    @BindView(R.id.etSerialNumber)
    EditText etSerialNumber;
    @BindView(R.id.spfridgeState)
    Spinner spfridgeState;
    @BindView(R.id.spBreakDownType)
    Spinner spBreakDownType;
    @BindView(R.id.rgIsOn)
    RadioGroup rgIsOn;
    @BindView(R.id.rbFridgeOn)
    RadioButton rbFridgeOn;
    @BindView(R.id.rbFridgeOff)
    RadioButton rbFridgeOff;
    @BindView(R.id.etFrigeTemp)
    EditText etFrigeTemp;
    @BindView(R.id.rgExternalFridge)
    RadioGroup rgExternalFridge;
    @BindView(R.id.rbExternalFridgeYes)
    RadioButton rbExternalFridgeYes;
    @BindView(R.id.rbExternalFridgeNo)
    RadioButton rbExternalFridgeNo;
    @BindView(R.id.ivPhotoBarcode)
    ImageView ivPhotoBarcode;
    @BindView(R.id.btDelete)
    Button btDelete;
    @BindView(R.id.btValidate)
    Button btValidate;
    @BindView(R.id.llBarcode)
    LinearLayout llBarcode;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.etBarCode)
    TextView etBarCode;
    @BindView(R.id.barCode)
    Button barCode;
    @BindView(R.id.btValidateBarCode)
    Button btValidateBarCode;
    /**
     * ButterKnife Code
     **/
    private Fridge fridge;
    private RealmList<Fridge> fridges;
    private int index;
    private Uri fileUri;
    int MEDIA_TYPE_IMAGE = 1;
    Realm realm;
    Photo photo;
    Photo photoBarcode;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fridge_dialog_survey, container, false);
        ButterKnife.bind(this, v);
        llFridge.setVisibility(View.VISIBLE);
        llBarcode.setVisibility(View.GONE);
        realm = Realm.getDefaultInstance();
        spBreakDownType.setSelection(0);
        spBreakDownType.setEnabled(false);
        setCancelable(false);
        fridge = fridges.get(index);
        if (fridge.getAbused() == 1)
            rbAbusedYes.setChecked(true);
        else if (fridge.getAbused() == 0)
            rbrbAbusedNo.setChecked(true);

        if (fridge.getIsOn() == 1)
            rbFridgeOn.setChecked(true);
        else if (fridge.getIsOn() == 0)
            rbFridgeOff.setChecked(true);

        if (fridge.getExternal() == 1)
            rbExternalFridgeYes.setChecked(true);
        else if (fridge.getExternal() == 0)
            rbExternalFridgeNo.setChecked(true);
        spFridgeOwner.setSelection(fridge.getFridgeOwner());
        spBreakDownType.setSelection(fridge.getBreakDownType());
        spFridgeModel.setSelection(fridge.getFridgeModel());
        spfridgeState.setSelection(fridge.getFridgeState());

        spFridgeDate.setSelection(fridge.getAttributionYear());

        etBarCode.setText(fridge.getBarCode());
        etSerialNumber.setText(fridge.getFridgeSerial());
        etFrigeTemp.setText(fridge.getFridgeTemp());
        photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();
        if (photo != null)
            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));

        photoBarcode = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE_BARCODE).findFirst();
        if (photoBarcode != null)
            ivPhotoBarcode.setImageBitmap(Base64Util.Base64ToBitmap(photoBarcode.getImage()));
        spfridgeState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 1)

                    spBreakDownType.setEnabled(true);
                else
                    spBreakDownType.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return v;


    }

    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    private void check() {
        fridge.setFridgeOwner(spFridgeOwner.getSelectedItemPosition());
        fridge.setBreakDownType(spBreakDownType.getSelectedItemPosition());
        fridge.setFridgeModel(spFridgeModel.getSelectedItemPosition());

        fridge.setFridgeSerial(etSerialNumber.getText().toString());
        fridge.setFridgeState(spfridgeState.getSelectedItemPosition());
        fridge.setAttributionYear(spFridgeDate.getSelectedItemPosition());
        if (etFrigeTemp.getText().toString().length() > 0)
            fridge.setFridgeTemp(etFrigeTemp.getText().toString());

        if (rbAbusedYes.isChecked())
            fridge.setAbused(1);
        else if (rbrbAbusedNo.isChecked())
            fridge.setAbused(0);

        if (rbFridgeOn.isChecked())
            fridge.setIsOn(1);
        else if (rbFridgeOff.isChecked())
            fridge.setIsOn(0);

        if (rbExternalFridgeYes.isChecked())
            fridge.setExternal(1);
        else if (rbExternalFridgeNo.isChecked())
            fridge.setExternal(0);

    }

    @OnClick(R.id.btValidate)
    public void validate() {
        check();
        if (!validateCocaColaFridge(fridge)) {

            FridgeDialogCallbackInterface callbackInterfrae = (FridgeDialogCallbackInterface) getTargetFragment();
            callbackInterfrae.setFridge(fridge, index);


            getFragmentManager().popBackStack();
        } else {
            llFridge.setVisibility(View.INVISIBLE);
            llBarcode.setVisibility(View.VISIBLE);

        }


    }

    @OnClick(R.id.btValidateBarCode)
    public void btValidateBarCode() {
        boolean valide = true;
        if (fridge.getBarCode().length() == 0)
            valide = false;
        Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE_BARCODE).findFirst();
        if (photo == null) {
            valide = false;
        }


        if (valide) {
            FridgeDialogCallbackInterface callbackInterfrae = (FridgeDialogCallbackInterface) getTargetFragment();
            callbackInterfrae.setFridge(fridge, index);


            getFragmentManager().popBackStack();

        }


    }

    @OnClick(R.id.btDelete)
    public void close() {

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Modification")

                .setContentText("Êtes vous sure de vouloir supprimé ce frigo  ? ")
                .setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Toasty.error(getContext(), "Frigo supprimé", 6000).show();
                        FridgeDialogCallbackInterface callbackInterfrae = (FridgeDialogCallbackInterface) getTargetFragment();
                        callbackInterfrae.deleteFridge(index);
                        getFragmentManager().popBackStack();
                        sweetAlertDialog.dismiss();
                    }
                })
                .setCancelButton("Non", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    String typeimage;

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto(Constants.IMG_FRIDGE);
    }

    @OnClick(R.id.ivPhotoBarcode)
    public void setIvPhotoBarcode() {

        takePhoto(Constants.IMG_FRIDGE_BARCODE);
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
                        if (barcode.rawValue.toString().toLowerCase().startsWith("cde")) {
                            etBarCode.setText(barcode.rawValue);
                            fridge.setBarCode(barcode.rawValue);
                        } else {
                            if (getActivity() != null)
                                Toasty.error(getActivity(), "Le code a barre n'est pas valide").show();
                        }
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    public RealmList<Fridge> getFridges() {
        return fridges;
    }

    public void setFridges(RealmList<Fridge> fridges) {
        this.fridges = fridges;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void takePhoto(String type) {

        typeimage = type;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("fileuri", fileUri.getPath() + " ");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       /* intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +".provider",
                        new File(fileUri.getPath())));*/
        // start the image capture Intent
        this.startActivityForResult(intent, 1234);
    }

    public Uri getOutputMediaFileUri(int type) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type));
        } else {
            File file = ImageLoad.getOutputMediaFile(getContext(), type);
            if (file == null) {
                Log.e("nullfile", "nullfile");
            } else {
                Log.e("nullfile", "not");
            }
            return FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);


        }


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

            canvas.drawText("Date: " + currentDateandTime, 20, 35, paint);

       /*     ivPlv.setImageResource(android.R.color.transparent);
            PicassoSingleton.with(getActivity())
                    .load("file://" + fileUri.getPath())
                    .resize(250, 250)
                    .noFade()
                    .into(ivPlv);*/
            if (typeimage.equals(Constants.IMG_FRIDGE))
                ivPhoto.setImageBitmap(mutableBitmap);
            else
                ivPhotoBarcode.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            realm.beginTransaction();
            if (photo == null)
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("fi_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            photo.setTypeID(fridge.getMobileId());
            photo.setImage(imageBase64);
            photo.setType(typeimage);
            realm.commitTransaction();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

    private boolean validateCocaColaFridge(Fridge fridgecocacola) {
        Log.e("fridge", new Gson().toJson(fridge));
        boolean valid = true;
        Log.e("1 fridgesurv", valid + " ");
        if (fridge.getAbused() < 0) {
            return false;
        }
        Log.e("2 fridgesurv", valid + " ");
        if (fridge.getFridgeOwner() == 0) {
            return false;
        }
        Log.e("3 fridgesurv", valid + " ");
        if (fridge.getFridgeModel() == 0) {
            return false;
        }
        Log.e("4 fridgesurv", valid + " ");
//        if (fridge.getFridgeSerial().length() == 0) {
//            return false;
//        }
//        if (fridge.getBarCode().length() == 0) {
//            return false;
//        }
        if (fridge.getFridgeState() == 0) {
            return false;
        }
        Log.e("5 fridgesurv", valid + " ");
        if (fridge.getFridgeState() == 2) {
            if (fridge.getBreakDownType() == 0)
                return false;
        }
        Log.e("6 fridgesurv", valid + " ");
        if (fridge.getIsOn() < 0) {

            return false;
        }
        Log.e("7 fridgesurv", valid + " ");
        if (fridge.getIsOn() == 1) {
            if (fridge.getFridgeTemp().length() == 0)
                return false;
        }
        Log.e("8 fridgesurv", valid + " ");
        if (fridge.getExternal() == -1) {

            return false;
        }
        Log.e("9 fridgesurv", valid + " ");

        // Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();
        Photo photofridge = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();

        if (photofridge == null) {
            return false;
        }
        Log.e("10 fridgesurv", valid + " ");

        return valid;

    }


}
