package com.storexecution.cocacola.ui.newpos.fridge;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Base64Util;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class FridgeBarCodePhoto extends DialogFragment {


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
    Fridge fridge;


    public FridgeBarCodePhoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fridge_bar_code_photo, container, false);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        etBarCode.setText(salepoint.getBarcode());

        photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_FRIDGE_BARCODE).findFirst();
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
                            if (barcode.rawValue.toString().toLowerCase().startsWith("cde")) {
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

    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }
}
