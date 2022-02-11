package com.storexecution.cocacola.ui.newpos.storeinformation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInformationFragment extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.etPosName)
    EditText etPosName;
    @BindView(R.id.etOwnerName)
    EditText etOwnerName;
    @BindView(R.id.etOwnerPhone)
    EditText etOwnerPhone;
    @BindView(R.id.etManagerName)
    EditText etManagerName;
    @BindView(R.id.etManagerPhone)
    EditText etManagerPhone;
    @BindView(R.id.rgPosSystem)
    RadioGroup rgPosSystem;
    @BindView(R.id.rbPosSystemYes)
    RadioButton rbPosSystemYes;
    @BindView(R.id.rbPosSystemNo)
    RadioButton rbPosSystemNo;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.spFacade)
    Spinner spFacade;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    /**
     * ButterKnife Code
     **/

    Salepoint salepoint;
    Session session;
    /** ButterKnife Code **/
    /**
     * ButterKnife Code
     **/
    public StoreInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_store_information, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        etPosName.setText(salepoint.getPosName());
        etOwnerName.setText(salepoint.getOwnerName());
        etOwnerPhone.setText(salepoint.getOwnerPhone());
        etManagerName.setText(salepoint.getManagerName());
        etManagerPhone.setText(salepoint.getManagerPhone());
        spFacade.setSelection(salepoint.getFacades());

        if (salepoint.getPosSystem() == 1)
            rbPosSystemYes.setChecked(true);
        else if (salepoint.getPosSystem() == 0)
            rbPosSystemNo.setChecked(true);


        return v;
    }

    @OnClick(R.id.fabNext)
    public void save() {
        StoreInformationFragment2 fragment = new StoreInformationFragment2();
        setData();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, "storeInfo")
                .addToBackStack(getClass().getName())
                .commit();


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private void setData() {

        salepoint.setPosName(etPosName.getText().toString());
        salepoint.setOwnerName(etOwnerName.getText().toString());
        salepoint.setOwnerPhone(etOwnerPhone.getText().toString());
        salepoint.setManagerName(etManagerName.getText().toString());
        salepoint.setManagerPhone(etManagerPhone.getText().toString());
        salepoint.setFacades(spFacade.getSelectedItemPosition());
        if (rbPosSystemYes.isChecked())
            salepoint.setPosSystem(1);
        else if (rbPosSystemNo.isChecked())
            salepoint.setPosSystem(0);

        session.setSalepoint(salepoint);
    }
}
