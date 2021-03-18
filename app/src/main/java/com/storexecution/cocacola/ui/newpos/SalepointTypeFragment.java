package com.storexecution.cocacola.ui.newpos;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.Session;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;


public class SalepointTypeFragment extends Fragment {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.cvAg)
    androidx.cardview.widget.CardView cvAg;
    @BindView(R.id.ivAg)
    ImageView ivAg;
    @BindView(R.id.tvAg)
    TextView tvAg;
    @BindView(R.id.cvSup)
    androidx.cardview.widget.CardView cvSup;
    @BindView(R.id.ivSup)
    ImageView ivSup;
    @BindView(R.id.tvSup)
    TextView tvSup;
    @BindView(R.id.cvFf)
    androidx.cardview.widget.CardView cvFf;
    @BindView(R.id.ivFf)
    ImageView ivFf;
    @BindView(R.id.tvFf)
    TextView tvFf;
    @BindView(R.id.cvCafe)
    androidx.cardview.widget.CardView cvCafe;
    @BindView(R.id.ivCafe)
    ImageView ivCafe;
    @BindView(R.id.tvCafe)
    TextView tvCafe;
    @BindView(R.id.cvRestaurant)
    androidx.cardview.widget.CardView cvRestaurant;
    @BindView(R.id.ivRestaurant)
    ImageView ivRestaurant;
    @BindView(R.id.tvRestaurant)
    TextView tvRestaurant;
    @BindView(R.id.cvThe)
    androidx.cardview.widget.CardView cvThe;
    @BindView(R.id.ivThe)
    ImageView ivThe;
    @BindView(R.id.tvThe)
    TextView tvThe;
    @BindView(R.id.cvPatisserie)
    androidx.cardview.widget.CardView cvPatisserie;
    @BindView(R.id.ivPatisserie)
    ImageView ivPatisserie;
    @BindView(R.id.tvPatisserie)
    TextView tvPatisserie;
    @BindView(R.id.cvBT)
    androidx.cardview.widget.CardView cvBT;
    @BindView(R.id.ivBt)
    ImageView ivBt;
    @BindView(R.id.tvBt)
    TextView tvBt;
    @BindView(R.id.fabNext)
    com.google.android.material.floatingactionbutton.FloatingActionButton fabNext;
    /**
     * ButterKnife Code
     **/
    ArrayList<androidx.cardview.widget.CardView> views;
    ArrayList<ImageView> images;
    ArrayList<TextView> textViews;
    Salepoint salepoint;
    int selected;
    Session session;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salepoint_type, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        realm = Realm.getDefaultInstance();
        salepoint = session.getSalepoint();
        if (salepoint.getMobile_id() == null || salepoint.getMobile_id().length() == 0)
            salepoint.setMobile_id("pos_" + UUID.randomUUID());
        if (salepoint.getStartedMobileDate() == 0)
            salepoint.setStartedMobileDate(System.currentTimeMillis() / 1000);
        views = new ArrayList<>();
        images = new ArrayList<>();
        textViews = new ArrayList<>();

        views.add(cvAg);
        views.add(cvSup);
        views.add(cvFf);
        views.add(cvCafe);
        views.add(cvRestaurant);
        views.add(cvThe);
        views.add(cvPatisserie);
        views.add(cvBT);


        images.add(ivAg);
        images.add(ivSup);
        images.add(ivFf);
        images.add(ivCafe);
        images.add(ivRestaurant);
        images.add(ivThe);
        images.add(ivPatisserie);
        images.add(ivBt);

        textViews.add(tvAg);
        textViews.add(tvSup);
        textViews.add(tvFf);
        textViews.add(tvCafe);
        textViews.add(tvRestaurant);
        textViews.add(tvThe);
        textViews.add(tvPatisserie);
        textViews.add(tvBt);

        if (salepoint.getSalepointType() > 0)
            select(salepoint.getSalepointType());
        return v;
    }

    @OnClick(R.id.cvAg)
    public void ag() {
        select(1);
    }

    @OnClick(R.id.cvSup)
    public void sup() {
        select(2);
    }

    @OnClick(R.id.cvFf)
    public void ff() {
        select(3);
    }

    @OnClick(R.id.cvCafe)
    public void cafe() {
        select(4);
    }

    @OnClick(R.id.cvRestaurant)
    public void restaurant() {
        select(5);
    }

    @OnClick(R.id.cvThe)
    public void the() {
        select(6);
    }

    @OnClick(R.id.cvPatisserie)
    public void patisserie() {
        select(7);
    }

    @OnClick(R.id.cvBT)
    public void bt() {
        select(8);
    }

    private void unSelectAll() {


        for (ImageView iv : images)
            iv.clearColorFilter();
        for (androidx.cardview.widget.CardView cv : views)
            cv.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        for (TextView tv : textViews)
            tv.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
    }

    private void select(int index) {

        unSelectAll();

        views.get(index - 1).setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        selected = index;

        images.get(index - 1).setColorFilter(Color.argb(255, 255, 255, 255));
        textViews.get(index - 1).setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

    }

    private boolean setData() {
        Salepoint exist = realm.where(Salepoint.class).equalTo("mobile_id", salepoint.getMobile_id()).findFirst();


        boolean valide = true;
        if (exist != null) {
            Log.e("ca", salepoint.getSalepointType() + " " + selected);
            Log.e("cats", getCategory(salepoint.getSalepointType()) + " " + getCategory(selected));
            if (getCategory(salepoint.getSalepointType()) == getCategory(selected)) {
                Log.e("ca1", salepoint.getSalepointType() + " " + selected);
                valide = true;
            } else {
                valide = false;
                Log.e("ca2", salepoint.getSalepointType() + " " + selected);
            }

        } else {
            Log.e("ca3", salepoint.getSalepointType() + " " + selected);
            valide = true;
        }
        if (valide) {
            salepoint.setSalepointType(selected);
        }
        session.setSalepoint(salepoint);

        return valide;
    }


    @OnClick(R.id.fabNext)
    public void save() {
        Fragment fragment = null;
        if (setData()) {

            int type = salepoint.getSalepointType();
            if (type == 6 || type == 7 || type == 8) {
                fragment = new SodaSurvey();
            } else if (type != 0) {
                salepoint.setSellSoda(1);
                salepoint.setHasFridge(1);
                setData();
                fragment = new SurveyHeadingMenu();
                Bundle b = new Bundle();
                b.putBoolean("LunchLocation", true);
                fragment.setArguments(b);
            }

            if (fragment != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, fragment, "storeInfo")
                        // .addToBackStack(getClass().getName())
                        .commit();
            } else {

                Toasty.error(getActivity(), "Veuillez selectioner un type de magasin").show();
            }
        } else {
            Toasty.error(getContext(), "Vous ne pouvez pas changer vers ce type de POS", 7000).show();

        }


    }

    private int getCategory(int type) {
        if (type == Constants.TYPE_AG || type == Constants.TYPE_SUP)
            return 1;
        else if (type == Constants.TYPE_CAFE || type == Constants.TYPE_FASTFOOD || type == Constants.TYPE_RESTAURANT) {
            return 2;

        } else if (type == Constants.TYPE_THE || type == Constants.TYPE_BT || type == Constants.TYPE_PATISSERIE) {
            return 3;

        }

        return 0;
    }


}
