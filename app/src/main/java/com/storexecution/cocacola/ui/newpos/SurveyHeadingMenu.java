package com.storexecution.cocacola.ui.newpos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Fridge;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.LocationUtil;
import com.storexecution.cocacola.util.SalepointTypeUtils;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyHeadingMenu extends Fragment {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.tvalepointType)
    TextView tvalepointType;
    @BindView(R.id.cvPosInfo)
    androidx.cardview.widget.CardView cvPosInfo;
    @BindView(R.id.tvInfoPos)
    TextView tvInfoPos;
    @BindView(R.id.flPosInfoIndicator)
    FrameLayout flPosInfoIndicator;
    @BindView(R.id.cvTurnOver)
    androidx.cardview.widget.CardView cvTurnOver;
    @BindView(R.id.tvPurchase)
    TextView tvPurchase;
    @BindView(R.id.flTurnoverIndicator)
    FrameLayout flTurnoverIndicator;
    @BindView(R.id.cvFridge)
    androidx.cardview.widget.CardView cvFridge;
    @BindView(R.id.tvFridge)
    TextView tvFridge;
    @BindView(R.id.flFridgeIndicator)
    FrameLayout flFridgeIndicator;
    @BindView(R.id.cvAudit)
    androidx.cardview.widget.CardView cvAudit;
    @BindView(R.id.flAuditIndicator)
    FrameLayout flAuditIndicator;
    @BindView(R.id.cvExternalPlv)
    androidx.cardview.widget.CardView cvExternalPlv;
    @BindView(R.id.tvExternalPlv)
    TextView tvExternalPlv;
    @BindView(R.id.flExtrnalPlvIndicator)
    FrameLayout flExtrnalPlvIndicator;
    @BindView(R.id.cvInternalPlv)
    androidx.cardview.widget.CardView cvInternalPlv;
    @BindView(R.id.tvInternalPlv)
    TextView tvInternalPlv;
    @BindView(R.id.flInternalPlvIndicator)
    FrameLayout flInternalPlvIndicator;
    @BindView(R.id.cvRgb)
    androidx.cardview.widget.CardView cvRgb;
    @BindView(R.id.flRgbIndicator)
    FrameLayout flRgbIndicator;
    @BindView(R.id.cvLocation)
    androidx.cardview.widget.CardView cvLocation;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvRgb)
    TextView tvRgb;
    @BindView(R.id.flLocationIndicator)
    FrameLayout flLocationIndicator;
    @BindView(R.id.fabNext)
    com.google.android.material.floatingactionbutton.FloatingActionButton fabNext;
    @BindView(R.id.fabBarcode)
    com.google.android.material.floatingactionbutton.FloatingActionButton fabBarcode;
    /**
     * ButterKnife Code
     **/
    Session session;
    Salepoint salepoint;
    Realm realm;
    User user;

    boolean infosEnabled = true;
    boolean turnoverEnabled = true;
    boolean fridgesEnabled = true;
    boolean surveyEnabled = true;
    boolean externalPlvEnabled = true;
    boolean internallPlvEnabled = true;
    boolean rgbEnabled = true;
    boolean locationEnabled = true;

    Notification notification;

    public SurveyHeadingMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_survey_heading_menu, container, false);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        ButterKnife.bind(this, v);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        if (salepoint.getNotificationId() != 0) {
            notification = realm.where(Notification.class).equalTo("id", salepoint.getNotificationId()).findFirst();
        }

        Log.e("REALM", Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()) + " ");

        //setCategories();
//        Bundle b = getArguments();
//        if (b != null) {
//
//            if (b.getBoolean("LunchLocation", false))
//                Location();
//        }
        tvalepointType.setText(SalepointTypeUtils.getType(salepoint.getSalepointType()));
        return v;
    }

    @OnClick(R.id.cvPosInfo)
    public void PosInfo() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (infosEnabled)
                startSurvey(Constants.TAG_POSINFO);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();

        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }

    }

    @OnClick(R.id.cvTurnOver)
    public void TurnOver() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (turnoverEnabled) {

                if (salepoint.getClassification() > 0)
                    startSurvey(Constants.TAG_TURNOVER);
                else
                    Toasty.error(getActivity(), "Veuillez selectionner la classification du POS dans Info POS", 8000).show();

            } else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvFridge)
    public void Fridge() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (fridgesEnabled)
                startSurvey(Constants.TAG_FRIDGE);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvAudit)
    public void Audit() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (surveyEnabled)
                startSurvey(Constants.TAG_AUDIT);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvExternalPlv)
    public void ExternalPlv() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (externalPlvEnabled)
                startSurvey(Constants.TAG_EXTERNALPLV);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvInternalPlv)
    public void InternalPlv() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (internallPlvEnabled)
                startSurvey(Constants.TAG_INTERNALPLV);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvRgb)
    public void Rgb() {
        if (LocationUtil.isGpsActive(getActivity())) {
            if (rgbEnabled)
                startSurvey(Constants.TAG_RGB);
            else
                Toasty.warning(getContext(), "Categorie non disponible pour ce point de vente", 7000, true).show();

        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.cvLocation)
    public void Location() {
        if (LocationUtil.isGpsActive(getActivity())) {
            startSurvey(Constants.TAG_LOCATION);
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @OnClick(R.id.fabNext)
    public void fbNext() {

    }

    private void startSurvey(String tag) {
        if (LocationUtil.isGpsActive(getActivity())) {

            Intent intent = new Intent(getActivity(), SalepointFormActivity.class);
            intent.putExtra("tag", tag);
            startActivity(intent);
        } else {
            Toasty.error(getActivity(), "Veuillez activer votre gps", 5000).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        salepoint = session.getSalepoint();
        setCategories();
        checkPosInformation();
        checkTurnOver();
        checkAudit();
        checkLocation();
        checkRgb();
        checkExternalPlv();
        checkInternalPlv();
        checkFridges();
        checkNotification();
    }


    private void checkNotification() {
        Log.e("checkNotification", "checkNotification");

        if (notification != null) {
            for (ValidationConditon validationConditon : notification.getConditions()) {
                Log.e("checkNotification", validationConditon.getDataType() + " ");

                if (validationConditon.getStatus() == 0 && (validationConditon.getDataType().equals(Constants.IMG_PLV_EXTERNAL) || validationConditon.getDataType().equals(Constants.IMG_PLV_EXTERNAL2))) {
                    tvExternalPlv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }
                if (validationConditon.getStatus() == 0 && (validationConditon.getDataType().equals(Constants.IMG_PLV_Internal))) {
                    tvInternalPlv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }

                if (validationConditon.getStatus() == 0 &&
                        (validationConditon.getDataType().equals(Constants.IMG_CFRIDGE) || validationConditon.getDataType().equals(Constants.IMG_FRIDGE_BARCODE) || validationConditon.getDataType().equals(Constants.IMG_FRIDGE))) {
                    tvFridge.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }
                if (validationConditon.getStatus() == 0 && (validationConditon.getDataType().equals(Constants.IMG_POS))) {
                    tvLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }

                if (validationConditon.getStatus() == 0 && (validationConditon.getDataType().equals(Constants.IMG_RGB))) {
                    tvRgb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }
                if (validationConditon.getStatus() == 0 && (validationConditon.getDataType().equals(Constants.IMG_RGB_KO))) {
                    tvRgb.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_action_warning, 0);
                }

            }


        }

    }

    private void setCategories() {


        if (salepoint.getClosed() == 0 && salepoint.getRefuse() == 0) {
            internallPlvEnabled = true;
            cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            cvExternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));


            cvAudit.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            externalPlvEnabled = true;
            internallPlvEnabled = true;
            surveyEnabled = true;
            cvTurnOver.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            cvRgb.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            turnoverEnabled = true;
            rgbEnabled = true;
            cvFridge.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            fridgesEnabled = true;

            cvPosInfo.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            flPosInfoIndicator.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            infosEnabled = true;

        }
        if (salepoint.getClosed() == 0 && salepoint.getRefuse() == 0) {
            if (salepoint.getHasFridge() <= 0) {
                cvFridge.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
                flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                fridgesEnabled = false;
            } else {
                fridgesEnabled = true;
            }
            if (salepoint.getSellSoda() <= 0) {
                cvTurnOver.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
                flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                cvRgb.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
                flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                turnoverEnabled = false;
                rgbEnabled = false;
            } else {
                turnoverEnabled = true;
                rgbEnabled = true;
            }

            if (salepoint.getSalepointType() == Constants.TYPE_BT || salepoint.getSalepointType() == Constants.TYPE_THE || salepoint.getSalepointType() == Constants.TYPE_PATISSERIE) {
                cvExternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
                flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));

//                cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
//                flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));


                cvAudit.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
                flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                externalPlvEnabled = false;
                internallPlvEnabled = true;
                surveyEnabled = false;

            } else {
                externalPlvEnabled = true;
                internallPlvEnabled = true;
                surveyEnabled = true;
            }
            if (salepoint.getSalepointType() == Constants.TYPE_RESTAURANT || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD || salepoint.getSalepointType() == Constants.TYPE_CAFE) {
//                internallPlvEnabled = true;
//                cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
//                flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));

            }
        } else if (salepoint.getClosed() == 1 || salepoint.getRefuse() == 1) {
            internallPlvEnabled = false;
            cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            cvExternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));

            cvInternalPlv.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));


            cvAudit.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));

            externalPlvEnabled = false;
            internallPlvEnabled = false;
            surveyEnabled = false;
            cvTurnOver.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            cvRgb.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            turnoverEnabled = false;
            rgbEnabled = false;
            cvFridge.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            fridgesEnabled = false;

            cvPosInfo.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            flPosInfoIndicator.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            infosEnabled = false;

        } else {
            Location();
        }


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


        if (salepoint.getFacades() > 0) {
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

        Log.e("se", started + " " + completd);

        if (completd)
            flPosInfoIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flPosInfoIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flPosInfoIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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


//        if (salepoint.getPurchaseWaterLow().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }
//
//        if (salepoint.getPurchaseWaterHigh().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }


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

//        if (salepoint.getPurchaseJuicePetLow().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }
//        if (salepoint.getPurchaseJuicePetHigh().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }

//        if (salepoint.getPurchaseEnergyDrinkLow().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }
//        if (salepoint.getPurchaseEnergyDrinkHigh().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }
//
//        if (salepoint.getPurchaseOtherLow().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }
//        if (salepoint.getPurchaseOtherHigh().length() > 0) {
//            started = true;
//        } else {
//            completd = false;
//
//        }

        if (salepoint.getHasWarmStock() >= 0) {
            started = true;
        } else {
            completd = false;

        }

        Log.e("to", started + " " + completd);
        if (completd)
            flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flTurnoverIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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

        if (completd)
            flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flAuditIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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
        Log.e("CompleteedLocation", completd + " ");
        if (salepoint.getCommune() > 0) {

            started = true;
        } else {
            completd = false;
        }
//        if (salepoint.getBarcode().length() > 0) {
//
//            started = true;
//        } else {
//            completd = false;
//        }
        Log.e("CompleteedLocation2", completd + " ");
        if (salepoint.getClosed() >= 0) {
            started = true;

        } else {
            completd = false;
        }
        Log.e("CompleteedLocation3", completd + " ");
        if (salepoint.getClosed() == 1) {
            if (salepoint.getClosedReason() == 0)
                completd = false;
            else
                started = true;
        }
        Log.e("CompleteedLocation4", completd + " ");


        if (salepoint.getClosed() == 0) {
            if (salepoint.getRefuse() < 0)
                completd = false;
            else {
                started = true;
                if (salepoint.getRefuse() == 1) {
                    if (salepoint.getRefuseReason() == 0)
                        completd = false;
                    else {
                        started = true;
                        if (salepoint.getRefuseReason() == 5) {
                            if (salepoint.getOtherRefuseReason().length() == 0)
                                completd = false;
                            else
                                started = true;
                        }
                    }
                }
            }
        }
        Log.e("CompleteedLocation5", completd + " ");
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


        if (completd)
            flLocationIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flLocationIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flLocationIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

        return completd;

    }

    private boolean checkRgb() {

        boolean started = false;
        boolean completd = true;


        if (salepoint.getHasRgb() > 0) {
            Photo photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_RGB).findFirst();
            if (photo != null)
                started = true;
            else
                completd = false;
            started = true;
            if (salepoint.getHasKoRgb() > 0) {
                Photo photo2 = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_RGB_KO).findFirst();
                if (photo2 != null)
                    started = true;
                else
                    completd = false;

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


        if (completd)
            flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flRgbIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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

        if (completd)
            flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flExtrnalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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
        long photo = realm.where(Photo.class).equalTo("TypeID", salepoint.getMobile_id()).and().equalTo("Type", Constants.IMG_PLV_Internal).count();
        if (photo > 0) {
            started = true;

        }
        if (photo < 2) {
            completd = false;
        }

        if (completd)
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flInternalPlvIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

//        if (salepoint.getSalepointType() == Constants.TYPE_CAFE || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD) {
//            return true;
//        }
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


        if (completd)
            flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorGreen2, null));
        else if (started)
            flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorYellow, null));

        else
            flFridgeIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));

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
//        if (fridge.getFridgeState() == 0) {
//            return false;
//        }
//        if (fridge.getFridgeState() == 2) {
//            if (fridge.getBreakDownType() == 0)
//                return false;
//        }
        if (fridge.getIsOn() < 0) {

            return false;
        }
        if (fridge.getIsOn() == 1) {
//            if (fridge.getFridgeTemp().length() == 0)
//                return false;
        }
        if (fridge.getExternal() == -1) {

            return false;
        }

        Photo photo = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE).findFirst();
        if (photo == null) {
            return false;
        }


        Photo photo2 = realm.where(Photo.class).equalTo("TypeID", fridge.getMobileId()).and().equalTo("Type", Constants.IMG_FRIDGE_BARCODE).findFirst();
        if (photo2 == null) {
            valid = false;
        }

        return valid;

    }

    @OnClick(R.id.fabNext)
    public void save() {
        if (checkSurveyValidity()) {

            if (salepoint.getRefuse() == 0 && salepoint.getClosed() == 0) {

                if (salepoint.getSalepointType() == Constants.TYPE_AG || salepoint.getSalepointType() == Constants.TYPE_SUP || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD || salepoint.getSalepointType() == Constants.TYPE_RESTAURANT || salepoint.getSalepointType() == Constants.TYPE_CAFE) {
                    BarCodeFragment fragment = new BarCodeFragment();
                    //setData();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content, fragment, "storeInfo")
                            .addToBackStack(getClass().getName())
                            .commit();
                } else {

                    if (salepoint.getWantToSellSoda() == 1) {
                        BarCodeFragment fragment = new BarCodeFragment();
                        //setData();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content, fragment, "storeInfo")
                                .addToBackStack(getClass().getName())
                                .commit();
                    } else {

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
                    }
                }
            } else {
                Toasty.success(getContext(), "Formulaire Valide", 7000, true).show();

                realm.beginTransaction();
                long currentMilis = System.currentTimeMillis() / 1000;
                if (salepoint.getCreatedMobileDate() == 0)
                    salepoint.setCreatedMobileDate(currentMilis);
                salepoint.setMobileModificationDate(currentMilis);
                if (salepoint.getFinishedMobileDate() == 0)
                    salepoint.setFinishedMobileDate(currentMilis);
                salepoint.setSynced(false);
                salepoint.setUser_id(user.getId());
                realm.insertOrUpdate(salepoint);
                session.clearSalepoint();
                getActivity().finish();

                realm.commitTransaction();
            }

        } else {
            Toasty.error(getContext(), "Formulaire incomplet ", 7000, true).show();

        }


    }

    private boolean checkSurveyValidity() {

        if (salepoint.getClosed() == 0 && salepoint.getRefuse() == 0) {

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
                iplv = checkInternalPlv();
                audit = true;

            } else if (salepoint.getSalepointType() == Constants.TYPE_RESTAURANT || salepoint.getSalepointType() == Constants.TYPE_FASTFOOD || salepoint.getSalepointType() == Constants.TYPE_CAFE) {
                eplv = checkExternalPlv();
                iplv = checkInternalPlv();
                audit = checkAudit();
            } else {

                eplv = checkExternalPlv();
                iplv = checkInternalPlv();
                audit = checkAudit();
            }

            posinfo = checkPosInformation();
            location = checkLocation();

            return (posinfo && turnover && fridges && audit && eplv && iplv && rgb && location);
        } else if (salepoint.getClosed() == 1 || salepoint.getRefuse() == 1) {
            return checkLocation();
        } else {
            Location();
        }

        return false;
    }


}
