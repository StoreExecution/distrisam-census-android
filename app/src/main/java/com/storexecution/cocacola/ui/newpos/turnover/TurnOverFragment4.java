package com.storexecution.cocacola.ui.newpos.turnover;

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

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TurnOverFragment4 extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.ivbox)
    ImageView ivbox;
    @BindView(R.id.btMinusEnergyDrinkLow)
    Button btMinusEnergyDrinkLow;
    @BindView(R.id.etEnergyDrinkLow)
    EditText etEnergyDrinkLow;
    @BindView(R.id.btPlusEnergyDrinkLow)
    Button btPlusEnergyDrinkLow;
    @BindView(R.id.btMinusEnergyDrinkHigh)
    Button btMinusEnergyDrinkHigh;
    @BindView(R.id.etEnergyDrinkHigh)
    EditText etEnergyDrinkHigh;
    @BindView(R.id.btPlusEnergyDrinkHigh)
    Button btPlusEnergyDrinkHigh;
    @BindView(R.id.btMinusOtherLow)
    Button btMinusOtherLow;
    @BindView(R.id.etOtherLow)
    EditText etOtherLow;
    @BindView(R.id.btPlusOtherLow)
    Button btPlusOtherLow;
    @BindView(R.id.btMinusOtherHigh)
    Button btMinusOtherHigh;
    @BindView(R.id.etOtherHigh)
    EditText etOtherHigh;
    @BindView(R.id.btPlusOtherHigh)
    Button btPlusOtherHigh;
    @BindView(R.id.rgWarmStock)
    RadioGroup rgWarmStock;
    @BindView(R.id.rbWarmStockYes)
    RadioButton rbWarmStockYes;
    @BindView(R.id.rbWarmStockNo)
    RadioButton rbWarmStockNo;
    @BindView(R.id.llNavigation)
    LinearLayout llNavigation;
    @BindView(R.id.fabPrev)
    ImageView fabPrev;
    @BindView(R.id.fabNext)
    ImageView fabNext;
    /**
     * ButterKnife Code
     **/

    Salepoint salepoint;
    Session session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turn_over4, container, false);
        ButterKnife.bind(this, v);

        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();

        etEnergyDrinkLow.setText(salepoint.getPurchaseEnergyDrinkLow());
        etEnergyDrinkHigh.setText(salepoint.getPurchaseEnergyDrinkHigh());
        etOtherLow.setText(salepoint.getPurchaseOtherLow());
        etOtherHigh.setText(salepoint.getPurchaseOtherHigh());
        if (salepoint.getHasWarmStock() == 1)
            rbWarmStockYes.setChecked(true);
        else if (salepoint.getHasWarmStock() == 0)
            rbWarmStockNo.setChecked(true);


        btMinusEnergyDrinkLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEnergyDrinkLow.getText().length() > 0) {

                    int count = Integer.parseInt(etEnergyDrinkLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etEnergyDrinkLow.setText(String.valueOf(count));
                } else {
                    etEnergyDrinkLow.setText("0");
                }

            }
        });


        btPlusEnergyDrinkLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEnergyDrinkLow.getText().length() > 0) {

                    int count = Integer.parseInt(etEnergyDrinkLow.getText().toString());

                    count += 1;
                    etEnergyDrinkLow.setText(String.valueOf(count));
                } else {
                    etEnergyDrinkLow.setText("0");
                }
            }
        });


        btMinusEnergyDrinkHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEnergyDrinkHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etEnergyDrinkHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etEnergyDrinkHigh.setText(String.valueOf(count));
                } else {
                    etEnergyDrinkHigh.setText("0");
                }

            }
        });


        btPlusEnergyDrinkHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEnergyDrinkHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etEnergyDrinkHigh.getText().toString());

                    count += 1;
                    etEnergyDrinkHigh.setText(String.valueOf(count));
                } else {
                    etEnergyDrinkHigh.setText("0");
                }
            }
        });


        btMinusOtherLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherLow.getText().length() > 0) {

                    int count = Integer.parseInt(etOtherLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etOtherLow.setText(String.valueOf(count));
                } else {
                    etOtherLow.setText("0");
                }

            }
        });


        btPlusOtherLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherLow.getText().length() > 0) {

                    int count = Integer.parseInt(etOtherLow.getText().toString());

                    count += 1;
                    etOtherLow.setText(String.valueOf(count));
                } else {
                    etOtherLow.setText("0");
                }
            }
        });


        btMinusOtherHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etOtherHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etOtherHigh.setText(String.valueOf(count));
                } else {
                    etOtherHigh.setText("0");
                }

            }
        });


        btPlusOtherHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtherHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etOtherHigh.getText().toString());

                    count += 1;
                    etOtherHigh.setText(String.valueOf(count));
                } else {
                    etOtherHigh.setText("0");
                }
            }
        });
        return v;
    }


    private void setData() {

        if (etEnergyDrinkLow.getText().toString().length() > 0) {
            salepoint.setPurchaseEnergyDrinkLow(etEnergyDrinkLow.getText().toString());
            int count = Integer.parseInt(etEnergyDrinkLow.getText().toString());
            salepoint.setPurchaseEnergyDrinkHigh(String.valueOf(count * 2));
        }

        // salepoint.setPurchaseEnergyDrinkHigh(etEnergyDrinkHigh.getText().toString());
        if (etOtherLow.getText().toString().length() > 0) {

            salepoint.setPurchaseOtherLow(etOtherLow.getText().toString());
            int count = Integer.parseInt(etOtherLow.getText().toString());
            salepoint.setPurchaseOtherHigh(String.valueOf(count * 2));
        }
       // salepoint.setPurchaseOtherHigh(etOtherHigh.getText().toString());
        if (rbWarmStockYes.isChecked())
            salepoint.setHasWarmStock(1);
        else if (rbWarmStockNo.isChecked())
            salepoint.setHasWarmStock(0);
        session.setSalepoint(salepoint);
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
}
