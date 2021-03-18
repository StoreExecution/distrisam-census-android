package com.storexecution.cocacola.ui.newpos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class SodaSurvey extends Fragment {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rgSellSoda)
    RadioGroup rgSellSoda;
    @BindView(R.id.rbSellSodaYes)
    RadioButton rbSellSodaYes;
    @BindView(R.id.rbSellSodaNo)
    RadioButton rbSellSodaNo;
    @BindView(R.id.llWsellSoda)
    LinearLayout llWsellSoda;
    @BindView(R.id.rgwSellSoda)
    RadioGroup rgwSellSoda;
    @BindView(R.id.rbwSellSodaYes)
    RadioButton rbwSellSodaYes;
    @BindView(R.id.rbwSellSodaNo)
    RadioButton rbwSellSodaNo;
    @BindView(R.id.llHasFridge)
    LinearLayout llHasFridge;
    @BindView(R.id.rgfridge)
    RadioGroup rgfridge;
    @BindView(R.id.rbFridgeYes)
    RadioButton rbFridgeYes;
    @BindView(R.id.rbFridgeNo)
    RadioButton rbFridgeNo;
    @BindView(R.id.fabPrev)
    com.google.android.material.floatingactionbutton.FloatingActionButton fabPrev;
    @BindView(R.id.fabNext)
    com.google.android.material.floatingactionbutton.FloatingActionButton fabNext;
    /**
     * ButterKnife Code
     **/

    Salepoint salepoint;
    Session session;

    public SodaSurvey() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_soda_survey, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();

        rgSellSoda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbSellSodaYes.isChecked()) {
                    llHasFridge.setVisibility(View.VISIBLE);
                    llWsellSoda.setVisibility(View.GONE);
                } else if (rbSellSodaNo.isChecked()) {
                    llWsellSoda.setVisibility(View.VISIBLE);
                    llHasFridge.setVisibility(View.GONE);
                }


            }
        });


        if (salepoint.getSellSoda() == 1)
            rbSellSodaYes.setChecked(true);
        else if (salepoint.getSellSoda() == 0)
            rbSellSodaNo.setChecked(true);

        if (salepoint.getWantToSellSoda() == 1)
            rbwSellSodaYes.setChecked(true);
        else if (salepoint.getWantToSellSoda() == 0)
            rbwSellSodaNo.setChecked(true);

        if (salepoint.getHasFridge() == 1)
            rbFridgeYes.setChecked(true);
        else if (salepoint.getHasFridge() == 0)
            rbFridgeNo.setChecked(true);

        return v;
    }

    private void setData() {
        if (rbSellSodaYes.isChecked()) {
            salepoint.setSellSoda(1);

            if (rbFridgeYes.isChecked())
                salepoint.setHasFridge(1);
            else if (rbFridgeNo.isChecked())
                salepoint.setHasFridge(0);
            salepoint.setWantToSellSoda(1);

        } else if (rbSellSodaNo.isChecked()) {
            salepoint.setHasFridge(0);
            salepoint.setSellSoda(0);
            if (rbwSellSodaYes.isChecked())
                salepoint.setWantToSellSoda(1);
            else if (rbwSellSodaNo.isChecked())
                salepoint.setWantToSellSoda(0);
            salepoint.setHasFridge(-1);
        }


        session.setSalepoint(salepoint);
    }

    @OnClick(R.id.fabNext)
    public void save() {
        Fragment fragment = new SurveyHeadingMenu();
        Bundle b = new Bundle();
        b.putBoolean("LunchLocation", true);
        fragment.setArguments(b);
        setData();
        if (check()) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment, "storeInfo")
                    // .addToBackStack(getClass().getName())
                    .commit();
        } else {
            Toasty.warning(getActivity(), "Veuillez saisir tous les champs obligatoire").show();
        }


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private boolean check() {

        boolean valid = true;
        if (rgSellSoda.getCheckedRadioButtonId() == -1) {
            valid = false;
        } else {

            if (rbSellSodaYes.isChecked())
                if (rgfridge.getCheckedRadioButtonId() == -1)
                    valid = false;

                else if (rbwSellSodaNo.isChecked())
                    if (rgwSellSoda.getCheckedRadioButtonId() == -1)
                        valid = false;


        }
        return valid;
    }


}
