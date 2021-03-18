package com.storexecution.cocacola.ui.newpos.rgb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RgbInformationFragment extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.rgRgb)
    RadioGroup rgRgb;
    @BindView(R.id.rbRgbYes)
    RadioButton rbRgbYes;
    @BindView(R.id.rbRgbNo)
    RadioButton rbRgbNo;
    @BindView(R.id.rgRgbKo)
    RadioGroup rgRgbKo;
    @BindView(R.id.rbRgbKoYes)
    RadioButton rbRgbKoYes;
    @BindView(R.id.rbRgbKoNo)
    RadioButton rbRgbKoNo;
    @BindView(R.id.rgStock)
    RadioGroup rgStock;
    @BindView(R.id.rbStockYes)
    RadioButton rbStockYes;
    @BindView(R.id.rbStockNo)
    RadioButton rbStockNo;
    @BindView(R.id.ivbox)
    ImageView ivbox;
    @BindView(R.id.btMinusRgbEmpty)
    Button btMinusRgbEmpty;
    @BindView(R.id.etRgbEmpty)
    EditText etRgbEmpty;
    @BindView(R.id.btPlusRgbEmpty)
    Button btPlusRgbEmpty;
    @BindView(R.id.btMinusRgbrFull)
    Button btMinusRgbrFull;
    @BindView(R.id.etRgbrFull)
    EditText etRgbrFull;
    @BindView(R.id.btPlusRgbrFull)
    Button btPlusRgbrFull;
    @BindView(R.id.etBarCode)
    TextView etBarCode;
    @BindView(R.id.barCode)
    ImageView barCode;
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

    public RgbInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rgb_information, container, false);
        ButterKnife.bind(this, v);
        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        if (salepoint.getHasRgb() == 0) {
            rbRgbNo.setChecked(true);
            rgRgbKo.clearCheck();
            enableRadioGroup(rgRgbKo, false);
            etRgbrFull.setText("");
            etRgbEmpty.setText("");
            etRgbrFull.setEnabled(false);
            etRgbEmpty.setEnabled(false);


            btMinusRgbEmpty.setEnabled(false);
            btMinusRgbrFull.setEnabled(false);
            btPlusRgbEmpty.setEnabled(false);
            btPlusRgbrFull.setEnabled(false);
            fabNext.setImageResource(R.drawable.ic_save);

        } else if (salepoint.getHasRgb() == 1) {
            rbRgbYes.setChecked(true);
            enableRadioGroup(rgRgbKo, true);
            rgRgbKo.clearCheck();
            etRgbrFull.setText("");
            etRgbEmpty.setText("");
            etRgbrFull.setEnabled(true);
            etRgbEmpty.setEnabled(true);


            btMinusRgbEmpty.setEnabled(true);
            btMinusRgbrFull.setEnabled(true);
            btPlusRgbEmpty.setEnabled(true);
            btPlusRgbrFull.setEnabled(true);

            fabNext.setImageResource(R.drawable.ic_arrow_forward);
        }

        if (salepoint.getHasKoRgb() == 0) {
            rbRgbKoNo.setChecked(true);
            etRgbrFull.setText("");
            etRgbEmpty.setText("");
            etRgbrFull.setEnabled(false);
            etRgbEmpty.setEnabled(false);


            btMinusRgbEmpty.setEnabled(false);
            btMinusRgbrFull.setEnabled(false);
            btPlusRgbEmpty.setEnabled(false);
            btPlusRgbrFull.setEnabled(false);
            fabNext.setImageResource(R.drawable.ic_save);

        } else if (salepoint.getHasKoRgb() == 1) {
            rbRgbKoYes.setChecked(true);
            etRgbrFull.setText("");
            etRgbEmpty.setText("");
            etRgbrFull.setEnabled(true);
            etRgbEmpty.setEnabled(true);


            btMinusRgbEmpty.setEnabled(true);
            btMinusRgbrFull.setEnabled(true);
            btPlusRgbEmpty.setEnabled(true);
            btPlusRgbrFull.setEnabled(true);
            fabNext.setImageResource(R.drawable.ic_arrow_forward);
        }

        if (salepoint.getHasWarmStock() == 0)
            rbStockNo.setChecked(true);
        else if (salepoint.getHasWarmStock() == 1)
            rbStockYes.setChecked(true);

        if (salepoint.getEmptyKoRgb().length() > 0)
            etRgbEmpty.setText(salepoint.getEmptyKoRgb());
        if (salepoint.getFullKoRgb().length() > 0)
            etRgbrFull.setText(salepoint.getFullKoRgb());

        rgRgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbRgbNo.getId() == i) {
                    rgRgbKo.clearCheck();
                    enableRadioGroup(rgRgbKo, false);
                    etRgbrFull.setText("");
                    etRgbEmpty.setText("");
                    etRgbrFull.setEnabled(false);
                    etRgbEmpty.setEnabled(false);


                    btMinusRgbEmpty.setEnabled(false);
                    btMinusRgbrFull.setEnabled(false);
                    btPlusRgbEmpty.setEnabled(false);
                    btPlusRgbrFull.setEnabled(false);
                    fabNext.setImageResource(R.drawable.ic_save);
                } else if (rbRgbYes.getId() == i) {
                    fabNext.setImageResource(R.drawable.ic_arrow_forward);
                    enableRadioGroup(rgRgbKo, true);
                    rgRgbKo.clearCheck();
                    etRgbrFull.setText("");
                    etRgbEmpty.setText("");
                    etRgbrFull.setEnabled(true);
                    etRgbEmpty.setEnabled(true);


                    btMinusRgbEmpty.setEnabled(true);
                    btMinusRgbrFull.setEnabled(true);
                    btPlusRgbEmpty.setEnabled(true);
                    btPlusRgbrFull.setEnabled(true);
                }
            }
        });

        rgRgbKo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbRgbKoNo.getId() == i) {
                    //rbRgbKoNo.setChecked(true);
                    etRgbrFull.setText("");
                    etRgbEmpty.setText("");
                    etRgbrFull.setEnabled(false);
                    etRgbEmpty.setEnabled(false);


                    btMinusRgbEmpty.setEnabled(false);
                    btMinusRgbrFull.setEnabled(false);
                    btPlusRgbEmpty.setEnabled(false);
                    btPlusRgbrFull.setEnabled(false);
                    fabNext.setImageResource(R.drawable.ic_save);

                } else if (rbRgbKoYes.getId() == i) {
                    fabNext.setImageResource(R.drawable.ic_arrow_forward);
                    etRgbrFull.setText("");
                    etRgbEmpty.setText("");
                    etRgbrFull.setEnabled(true);
                    etRgbEmpty.setEnabled(true);


                    btMinusRgbEmpty.setEnabled(true);
                    btMinusRgbrFull.setEnabled(true);
                    btPlusRgbEmpty.setEnabled(true);
                    btPlusRgbrFull.setEnabled(true);
                }
            }
        });


        return v;


    }


    @OnClick(R.id.fabNext)
    public void save() {
        setData();

        if (salepoint.getHasKoRgb() == 1) {
            RgbInformationFragment2 fragment = new RgbInformationFragment2();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment, "storeInfo")
                    .addToBackStack(getClass().getName())
                    .commit();
        } else {
            getActivity().onBackPressed();
        }


    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();


    }

    private void disbleRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRadioGroup(RadioGroup radioGroup, boolean enable) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(enable);
        }
    }

    private void setData() {


        if (rbRgbYes.isChecked())
            salepoint.setHasRgb(1);
        else if (rbRgbNo.isChecked())
            salepoint.setHasRgb(0);

        if (rbRgbKoYes.isChecked())
            salepoint.setHasKoRgb(1);
        else if (rbRgbKoNo.isChecked())
            salepoint.setHasKoRgb(0);
        else
            salepoint.setHasKoRgb(-1);

        if (rbStockYes.isChecked())
            salepoint.setHasWarmStock(1);
        else if (rbStockNo.isChecked())
            salepoint.setHasWarmStock(0);

        if (etRgbEmpty.getText().toString().length() > 0)
            salepoint.setEmptyKoRgb(etRgbEmpty.getText().toString());

        if (etRgbrFull.getText().toString().length() > 0)
            salepoint.setFullKoRgb(etRgbrFull.getText().toString());

        session.setSalepoint(salepoint);

    }
}
