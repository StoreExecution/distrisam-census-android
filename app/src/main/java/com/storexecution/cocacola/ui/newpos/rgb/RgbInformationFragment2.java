package com.storexecution.cocacola.ui.newpos.rgb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RgbInformationFragment2 extends Fragment {


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.etRgbLiterEmpty)
    EditText etRgbLiterEmpty;
    @BindView(R.id.etRgbLiterFull)
    EditText etRgbLiterFull;
    @BindView(R.id.btMinusRgbEmpty)
    Button btMinusRgbEmpty;
    @BindView(R.id.etRgbEmpty)
    EditText etRgbEmpty;
    @BindView(R.id.btPlusRgbEmpty)
    Button btPlusRgbEmpty;
    @BindView(R.id.btMinusRgbFull)
    Button btMinusRgbFull;
    @BindView(R.id.etRgbFull)
    EditText etRgbFull;
    @BindView(R.id.btPlusRgbFull)
    Button btPlusRgbFull;

    /**
     * ButterKnife Code
     **/
    Session session;
    Salepoint salepoint;

    public RgbInformationFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rgb_information2, container, false);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        ButterKnife.bind(this, v);

        if (salepoint.getRgbLitterEmty().length() > 0)
            etRgbLiterEmpty.setText(salepoint.getRgbLitterEmty());
        if (salepoint.getRgbLitterFull().length() > 0)
            etRgbLiterFull.setText(salepoint.getRgbLitterFull());

        if (salepoint.getRgbSmallEmpty().length() > 0)
            etRgbEmpty.setText(salepoint.getRgbSmallEmpty());

        if (salepoint.getRgbSmallFull().length() > 0)
            etRgbFull.setText(salepoint.getRgbSmallFull());

        return v;
    }


    @OnClick(R.id.fabNext)
    public void save() {
        setData();
        getActivity().finish();

    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private void setData() {

        if (etRgbLiterEmpty.getText().toString().length() > 0)
            salepoint.setRgbLitterEmty(etRgbLiterEmpty.getText().toString());

        if (etRgbLiterFull.getText().toString().length() > 0)
            salepoint.setRgbLitterFull(etRgbLiterFull.getText().toString());


        if (etRgbEmpty.getText().toString().length() > 0)
            salepoint.setRgbSmallEmpty(etRgbEmpty.getText().toString());

        if (etRgbFull.getText().toString().length() > 0)
            salepoint.setRgbSmallFull(etRgbFull.getText().toString());

        session.setSalepoint(salepoint);

    }
}
