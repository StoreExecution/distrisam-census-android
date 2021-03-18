package com.storexecution.cocacola.ui.newpos;

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
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pappin.mbs.MaterialBarcodeScanner;
import com.github.pappin.mbs.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.ImageLoad;
import com.storexecution.cocacola.util.PrimaryKeyFactory;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.UtilBase64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarCodeFragment extends Fragment {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.etBarCode)
    TextView etBarCode;

    @BindView(R.id.barCode)
    Button barCode;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;

    /**
     * ButterKnife Code
     **/

    Session session;
    Salepoint salepoint;
    Realm realm;
    Toast toast;
    Photo photo;

    public BarCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bar_code, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        etBarCode.setText(salepoint.getBarcode());

        photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_BARCODE).findFirst();
        if (photo != null)
            ivPhoto.setImageBitmap(Base64Util.Base64ToBitmap(photo.getImage()));


        return v;
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
            return Uri.fromFile(ImageLoad.getOutputMediaFile3(type));
        } else

            return FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", ImageLoad.getOutputMediaFile(getContext(), type));

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
            ivPhoto.setImageBitmap(mutableBitmap);
            String imageBase64 = UtilBase64.bitmapToBase64String(mutableBitmap);

            // fridge.setPhotoFridge(imageBase64);
            realm.beginTransaction();
            if (photo == null)
                photo = realm.createObject(Photo.class, PrimaryKeyFactory.nextKey(Photo.class));

            photo.setImageID("pos_" + UUID.randomUUID());
            photo.setDate(System.currentTimeMillis() / 1000 + "");
            photo.setTypeID(salepoint.getMobile_id());
            photo.setImage(imageBase64);
            photo.setType(Constants.IMG_BARCODE);
            realm.commitTransaction();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivPhoto)
    public void photo() {

        takePhoto();
    }


    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        //  getActivity().getFragmentManager().popBackStack();

        getActivity().onBackPressed();


    }

    private void setData() {

        if (etBarCode.getText().toString().length() > 0)
            salepoint.setBarcode(etBarCode.getText().toString());


        session.setSalepoint(salepoint);


    }


    private boolean checkPosInformation() {

        boolean started = false;
        boolean completd = true;

        if (salepoint.getPosName().length() > 0) {
            started = true;
        } else {
            completd = false;
        }
//        if (salepoint.getOwnerName().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//        }
//        if (salepoint.getOwnerPhone().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//        }
//
//        if (salepoint.getManagerPhone().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//        }
//        if (salepoint.getManagerName().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//        }

        if ((salepoint.getOwnerName().length() > 0 && salepoint.getOwnerPhone().length() > 0) || (salepoint.getManagerPhone().length() > 0 && salepoint.getManagerName().length() > 0)) {
            started = true;
        } else {
            completd = false;
        }

        if (salepoint.getSalepointSurface() > 0) {
            started = true;
        } else {
            completd = false;
        }
        if (salepoint.getSellSoda() == 1) {
            if (salepoint.getPurchaseSource() > 0) {
                started = true;
            } else {
                completd = false;
            }


            if (salepoint.getPurchaseFrequency() > 0) {
                started = true;
            } else {
                completd = false;
            }

            if (salepoint.getVisitdays().size() > 0) {
                started = true;
            } else {
                if (salepoint.getPurchaseSource() == 1 || salepoint.getPurchaseSource() == 3)
                    completd = false;
            }
        }

        if (salepoint.getPosSystem() >= 0) {
            started = true;
        } else {
            completd = false;
        }

//        if (salepoint.getVisitdays().size() > 0) {
//            started = true;
//        } else {
//            if (salepoint.getPurchaseSource() == 1 || salepoint.getPurchaseSource() == 3)
//                completd = false;
//        }

        if (salepoint.getSalepointZone() > 0) {
            started = true;
        } else {
            completd = false;
        }


        if (salepoint.getClassification() > 0) {
            started = true;
        } else {
            completd = false;
        }

        return completd;


    }

    private boolean checkTurnOver() {
        boolean started = false;
        boolean completd = true;

        if (salepoint.getPurchaseCocaColaHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseCocaColaLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseHamoudLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseHamoudLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseHamoudHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchasePepsiLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchasePepsiHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseSodaLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseSodaHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }



        if (salepoint.getPurchaseRouibaLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseRouibaHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseRouibaPetLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseRouibaPetHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }


        if (salepoint.getPurchaseWaterLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseWaterHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }


        if (salepoint.getPurchaseJuiceLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseJuiceHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseJuicePetLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseJuicePetHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseEnergyDrinkLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseEnergyDrinkHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getPurchaseOtherLow().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getPurchaseOtherHigh().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getHasWarmStock() >= 0) {
            started = true;
        } else {
            completd = false;

        }

        Log.e("to", started + " " + completd);

        return completd;
    }


    private boolean checkAudit() {
        boolean started = false;
        boolean completd = true;

        if (salepoint.getBestDeliveryCompany().length() > 0) {
            started = true;
        } else {
            completd = false;

        }
        if (salepoint.getDeliveryRating() > 0) {
            started = true;
        } else {
            completd = false;

        }

        if (salepoint.getDeliveryPoints().size() > 0) {
            started = true;
        } else {
            completd = false;

        }


        return completd;
    }


    private boolean checkLocation() {
        boolean started = false;
        boolean completd = true;

        if (salepoint.getLatitude() != 0.0) {
            started = true;
        } else {
            completd = false;
        }
        if (salepoint.getLongitude() != 0.0) {

            started = true;
        } else {
            completd = false;
        }
        if (salepoint.getCommune() > 0) {

            started = true;
        } else {
            completd = false;
        }
        if (salepoint.getBarcode().length() > 0) {

            started = true;
        } else {
            completd = false;
        }

        if (salepoint.getLandmark().length() > 0) {

            started = true;
        } else {
            completd = false;
        }

        Photo photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_POS).findFirst();
        if (photo != null) {
            started = true;

        } else {
            completd = false;
        }
        Photo photo2 = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_BARCODE).findFirst();
        if (photo2 != null) {
            started = true;

        } else {
            completd = false;
        }

        return completd;

    }

    private boolean checkRgb() {

        boolean started = false;
        boolean completd = true;


        if (salepoint.getHasRgb() > 0) {
            started = true;
            if (salepoint.getHasKoRgb() > 0) {

//                if (salepoint.getFullKoRgb().length() > 0) {
//                    started = true;
//                } else {
//                    completd = false;
//                }
//                if (salepoint.getEmptyKoRgb().length() > 0) {
//                    started = true;
//                } else {
//                    completd = false;
//                }

                if (salepoint.getRgbLitterEmty().length() > 0) {
                    started = true;
                } else {
                    completd = false;
                }
                if (salepoint.getRgbLitterFull().length() > 0) {
                    started = true;
                } else {
                    completd = false;
                }
                if (salepoint.getRgbSmallEmpty().length() > 0) {
                    started = true;
                } else {
                    completd = false;
                }
                if (salepoint.getRgbSmallFull().length() > 0) {
                    started = true;
                } else {
                    completd = false;
                }
            } else if (salepoint.getHasKoRgb() == 0) {
                started = true;
            } else {
                completd = false;
            }

        } else if (salepoint.getHasRgb() == 0) {
            started = true;
            //   completd = false;
        } else {
            completd = false;
        }


        return completd;

    }

    private boolean checkExternalPlv() {

        boolean started = false;
        boolean completd = true;


        if (salepoint.getTindas().size() > 0)
            started = true;
        if (salepoint.getPotence().size() > 0)
            started = true;
        if (salepoint.getEars().size() > 0)
            started = true;
        if (salepoint.getWindowwrap().size() > 0)
            started = true;
        Photo photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL).findFirst();
        if (photo != null) {
            started = true;

        } else {
            completd = false;
        }

        if (salepoint.getSalepointType() == Constants.TYPE_CAFE || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD) {
            if (salepoint.getChaires().size() > 0)
                started = true;
            if (salepoint.getTables().size() > 0)
                started = true;
            if (salepoint.getParasols().size() > 0)
                started = true;
            if (salepoint.getSidewalkStop().size() > 0)
                started = true;
            Photo photo2 = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_EXTERNAL2).findFirst();
            if (photo2 != null) {
                started = true;

            } else {
                completd = false;
            }

        }


        return completd;

    }


    private boolean checkInternalPlv() {

        boolean started = false;
        boolean completd = true;


        if (salepoint.getMetalRacks().size() > 0)
            started = true;
        if (salepoint.getForexRacks().size() > 0)
            started = true;
        if (salepoint.getWrapedSkids().size() > 0)
            started = true;
        if (salepoint.getWrapedLinear().size() > 0)
            started = true;

//        if (salepoint.getCocacolaCombo() >= 0) {
//            started = true;
//
//        } else {
//            completd = false;
//        }
        Photo photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_Internal).findFirst();
        if (photo != null) {
            started = true;

        } else {
            completd = false;
        }

        if (salepoint.getSalepointType() == Constants.TYPE_CAFE || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD) {
            return true;
        }


        return completd;

    }


    private boolean checkFridges() {
        boolean started = false;
        boolean completd = true;
        if (salepoint.getFridgeCount().length() > 0) {
            started = true;
        } else {
            completd = false;
        }
        if (salepoint.getPepsiFridges().size() > 0) {
            started = true;
        }
        if (salepoint.getHamoudFridges().size() > 0) {
            started = true;
        }
        if (salepoint.getOtherFridges().size() > 0) {
            started = true;
        }


        if (salepoint.getCocaColaFridges().size() > 0) {
            started = true;
            for (Fridge fridge : salepoint.getCocaColaFridges()) {

                if (!validateCocaColaFridge(fridge))
                    completd = false;
            }


        }


        return completd;

    }

    private boolean validateCocaColaFridge(Fridge fridge) {
        boolean valid = true;
        if (fridge.getAbused() < 0) {
            return false;
        }
        if (fridge.getFridgeOwner() == 0) {
            return false;
        }
        if (fridge.getFridgeModel() == 0) {
            return false;
        }
//        if (fridge.getFridgeSerial().length() == 0) {
//            return false;
//        }
        if (fridge.getBarCode().length() == 0) {
            return false;
        }
        if (fridge.getFridgeState() == 0) {
            return false;
        }
        if (fridge.getFridgeState() == 2) {
            if (fridge.getBreakDownType() == 0)
                return false;
        }
        if (fridge.getIsOn() < 0) {

            return false;
        }
        if (fridge.getIsOn() == 1) {
            if (fridge.getFridgeTemp().length() == 0)
                return false;
        }
        if (fridge.getExternal() == -1) {

            return false;
        }

        Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();
        if (photo == null) {
            return false;
        }

        return valid;

    }

    @OnClick(R.id.fabNext)
    public void save() {
        setData();
        if (checkSurveyValidity()) {


            Toasty.success(getContext(), "Formulaire Valide", 7000, true).show();

            realm.beginTransaction();
            long currentMilis = System.currentTimeMillis() / 1000;
            if (salepoint.getCreatedMobileDate() == 0)
                salepoint.setCreatedMobileDate(currentMilis);
            salepoint.setMobileModificationDate(currentMilis);
            if (salepoint.getFinishedMobileDate() == 0)
                salepoint.setFinishedMobileDate(currentMilis);

            salepoint.setSynced(false);
            realm.insertOrUpdate(salepoint);
            session.clearSalepoint();
            getActivity().finish();

            realm.commitTransaction();


        } else {
            Toasty.error(getContext(), "Formulaire incomplet ", 7000, true).show();

        }


    }

//    private boolean checkSurveyValidity() {
//
//        boolean posinfo, turnover, fridges, audit, eplv, iplv, rgb, location;
//        if (salepoint.getHasFridge() <= 0) {
//            fridges = true;
//        } else {
//            fridges = checkFridges();
//        }
//        if (salepoint.getSellSoda() <= 0) {
//
//            turnover = true;
//            rgb = true;
//        } else {
//            turnover = checkTurnOver();
//            rgb = checkRgb();
//        }
//
//        if (salepoint.getSalepointType() == Constants.TYPE_BT || salepoint.getSalepointType() == Constants.TYPE_THE || salepoint.getSalepointType() == Constants.TYPE_PATISSERIE) {
//
//
//            eplv = true;
//            iplv = true;
//            audit = true;
//
//        } else {
//
//            eplv = checkExternalPlv();
//            iplv = checkInternalPlv();
//            audit = checkAudit();
//        }
//
//        posinfo = checkPosInformation();
//        location = checkLocation();
//
//        return (posinfo && turnover && fridges && audit && eplv && iplv && rgb && location);
//
//    }

    private boolean checkSurveyValidity() {

        if (salepoint.getClosed() == 0) {

            boolean posinfo, turnover, fridges, audit, eplv, iplv, rgb, location;
            if (salepoint.getHasFridge() <= 0) {
                fridges = true;
            } else {
                fridges = checkFridges();
            }
            if (salepoint.getSellSoda() <= 0) {

                turnover = true;
                rgb = true;
            } else {
                turnover = checkTurnOver();
                rgb = checkRgb();
            }

            if (salepoint.getSalepointType() == Constants.TYPE_BT || salepoint.getSalepointType() == Constants.TYPE_THE || salepoint.getSalepointType() == Constants.TYPE_PATISSERIE) {


                eplv = true;
                iplv = true;
                audit = true;

            } else if (salepoint.getSalepointType() == Constants.TYPE_RESTAURANT || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD || salepoint.getSalepointType() == Constants.TYPE_CAFE) {
                eplv = checkExternalPlv();
                iplv = true;
                audit = checkAudit();
            } else {

                eplv = checkExternalPlv();
                iplv = checkInternalPlv();
                audit = checkAudit();
            }

            posinfo = checkPosInformation();
            location = checkLocation();

            return (posinfo && turnover && fridges && audit && eplv && iplv && rgb && location);
        } else if (salepoint.getClosed() == 1) {
            return checkLocation();
        } else {
            return false;
        }

        //  return false;
    }


}
