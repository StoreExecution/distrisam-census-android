package com.storexecution.cocacola.ui.newpos.storeinformation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.ui.newpos.turnover.TurnOverFragment;
import com.storexecution.cocacola.util.Session;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInformationFragment2 extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.spSurface)
    Spinner spSurface;
    @BindView(R.id.spSourceOfPurchase)
    Spinner spSourceOfPurchase;
    @BindView(R.id.spFrequencyOfPurchase)
    Spinner spFrequencyOfPurchase;
    @BindView(R.id.btVisitDays)
    Button btVisitDays;
    @BindView(R.id.tgVisitDays)
    co.lujun.androidtagview.TagContainerLayout tgVisitDays;
    @BindView(R.id.spZone)
    Spinner spZone;
    @BindView(R.id.spClassification)
    Spinner spClassification;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    @BindView(R.id.llPurchase)

    LinearLayout llPurchase;
    /**
     * ButterKnife Code
     **/
    Salepoint salepoint;
    Session session;
    ArrayList<MultiSelectModel> days;
    ArrayList<String> daysText;
    ArrayList<Integer> selectedDays;

    public StoreInformationFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_store_information2, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        days = new ArrayList<>();
        selectedDays = new ArrayList<>();
        daysText = new ArrayList<>();

        if (salepoint.getSellSoda() == 0) {
            llPurchase.setVisibility(View.GONE);
        } else {
            llPurchase.setVisibility(View.VISIBLE);
        }

        days.add(new MultiSelectModel(1, "Samedi"));
        days.add(new MultiSelectModel(2, "Dimanche"));
        days.add(new MultiSelectModel(3, "Lundi"));
        days.add(new MultiSelectModel(4, "Mardi"));
        days.add(new MultiSelectModel(5, "Mercredi"));
        days.add(new MultiSelectModel(6, "Jeudi"));
        days.add(new MultiSelectModel(7, "Vendredi"));

        daysText.add("Samedi");
        daysText.add("Dimanche");
        daysText.add("Lundi");
        daysText.add("Mardi");
        daysText.add("Mercredi");
        daysText.add("Jeudi");
        daysText.add("Vendredi");


        spSourceOfPurchase.setSelection(salepoint.getPurchaseSource());
        spSurface.setSelection(salepoint.getSalepointSurface());
        spFrequencyOfPurchase.setSelection(salepoint.getPurchaseFrequency());
        spZone.setSelection(salepoint.getSalepointZone());
        selectedDays.addAll(salepoint.getVisitdays());
        spClassification.setSelection(salepoint.getClassification());
        setTags();

        return v;
    }


    @OnClick(R.id.btVisitDays)
    public void add() {
        MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                .title("Commentaire POS") //setting title for dialog
                .titleSize(25)
                .positiveText("OK")
                .negativeText("Annuler")

                .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                .setMaxSelectionLimit(days.size()) //you can set maximum checkbox selection limit (Optional)
                .preSelectIDsList(selectedDays) //List of ids that you need to be selected
                .multiSelectList(days) // the multi select model list with ids and name
                .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        setTags();


                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "Dialog cancelled");
                    }


                });

        multiSelectDialog.show(getActivity().getSupportFragmentManager(), "multiSelectDialog");
    }


    @OnClick(R.id.fabNext)
    public void save() {
        TurnOverFragment fragment = new TurnOverFragment();
        boolean valid = true;
        if (spFrequencyOfPurchase.getSelectedItemPosition() > 0)
            if (spFrequencyOfPurchase.getSelectedItemPosition() == 1 || spFrequencyOfPurchase.getSelectedItemPosition() == 2) {
                if (selectedDays.size() != 1)
                    valid = false;

            } else if (spFrequencyOfPurchase.getSelectedItemPosition() - 1 != selectedDays.size())
                valid = false;

        if (valid) {
            setData();
            getActivity().finish();
        } else {
            Toasty.error(getActivity(), "Erreur entre frequance d'achat et jour de visite", Toasty.LENGTH_LONG).show();
        }


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private void setTags() {
        tgVisitDays.removeAllTags();
        for (int i : selectedDays)
            tgVisitDays.addTag(daysText.get(i - 1));
    }

    private void setData() {

        salepoint.setSalepointSurface(spSurface.getSelectedItemPosition());
        salepoint.setPurchaseFrequency(spFrequencyOfPurchase.getSelectedItemPosition());
        salepoint.setPurchaseSource(spSourceOfPurchase.getSelectedItemPosition());
        salepoint.setVisitdays(selectedDays);
        salepoint.setSalepointZone(spZone.getSelectedItemPosition());
        salepoint.setClassification(spClassification.getSelectedItemPosition());
        session.setSalepoint(salepoint);


    }
}
