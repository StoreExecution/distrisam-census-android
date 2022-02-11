package com.storexecution.cocacola.ui.newpos.turnover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TurnOverFragment3 extends Fragment {


    /**
     * ButterKnife Code
     **/
    @BindView(R.id.ivbox)
    ImageView ivbox;
    @BindView(R.id.btMinusWaterLow)
    Button btMinusWaterLow;
    @BindView(R.id.etWaterLow)
    EditText etWaterLow;
    @BindView(R.id.btPlusWaterLow)
    Button btPlusWaterLow;
    @BindView(R.id.btMinusWaterHigh)
    Button btMinusWaterHigh;
    @BindView(R.id.etWaterHigh)
    EditText etWaterHigh;
    @BindView(R.id.btPlusWaterHigh)
    Button btPlusWaterHigh;
    @BindView(R.id.btMinusJuiceLow)
    Button btMinusJuiceLow;
    @BindView(R.id.etJuiceLow)
    EditText etJuiceLow;
    @BindView(R.id.btPlusJuiceLow)
    Button btPlusJuiceLow;
    @BindView(R.id.btMinusJuiceHigh)
    Button btMinusJuiceHigh;
    @BindView(R.id.etJuiceHigh)
    EditText etJuiceHigh;
    @BindView(R.id.btPlusJuiceHigh)
    Button btPlusJuiceHigh;
    @BindView(R.id.btMinusJuicePetLow)
    Button btMinusJuicePetLow;
    @BindView(R.id.etJuicePetLow)
    EditText etJuicePetLow;
    @BindView(R.id.btPlusJuicePetLow)
    Button btPlusJuicePetLow;
    @BindView(R.id.btMinusJuicePetHigh)
    Button btMinusJuicePetHigh;
    @BindView(R.id.etJuicePetHigh)
    EditText etJuicePetHigh;
    @BindView(R.id.btPlusJuicePetHigh)
    Button btPlusJuicePetHigh;
    @BindView(R.id.rgWarmStock)
    RadioGroup rgWarmStock;
    @BindView(R.id.rbWarmStockYes)
    RadioButton rbWarmStockYes;
    @BindView(R.id.rbWarmStockNo)
    RadioButton rbWarmStockNo;

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
        View v = inflater.inflate(R.layout.fragment_turn_over3, container, false);
        ButterKnife.bind(this, v);

        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();

        etWaterLow.setText(salepoint.getPurchaseWaterLow());
        etWaterHigh.setText(salepoint.getPurchaseWaterHigh());
        etJuiceLow.setText(salepoint.getPurchaseJuiceLow());
        etJuiceHigh.setText(salepoint.getPurchaseJuiceHigh());
        etJuicePetLow.setText(salepoint.getPurchaseJuicePetLow());
        etJuicePetHigh.setText(salepoint.getPurchaseJuicePetHigh());

        if (salepoint.getHasWarmStock() == 1)
            rbWarmStockYes.setChecked(true);
        else if (salepoint.getHasWarmStock() == 0)
            rbWarmStockNo.setChecked(true);

        btMinusWaterLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etWaterLow.getText().length() > 0) {

                    int count = Integer.parseInt(etWaterLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etWaterLow.setText(String.valueOf(count));
                } else {
                    etWaterLow.setText("0");
                }

            }
        });


        btPlusWaterLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etWaterLow.getText().length() > 0) {

                    int count = Integer.parseInt(etWaterLow.getText().toString());

                    count += 1;
                    etWaterLow.setText(String.valueOf(count));
                } else {
                    etWaterLow.setText("0");
                }
            }
        });


        btMinusWaterHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etWaterHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etWaterHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etWaterHigh.setText(String.valueOf(count));
                } else {
                    etWaterHigh.setText("0");
                }

            }
        });


        btPlusWaterHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etWaterHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etWaterHigh.getText().toString());

                    count += 1;
                    etWaterHigh.setText(String.valueOf(count));
                } else {
                    etWaterHigh.setText("0");
                }
            }
        });


        btMinusJuiceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuiceLow.getText().length() > 0) {

                    int count = Integer.parseInt(etJuiceLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etJuiceLow.setText(String.valueOf(count));
                } else {
                    etJuiceLow.setText("0");
                }

            }
        });


        btPlusJuiceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuiceLow.getText().length() > 0) {

                    int count = Integer.parseInt(etJuiceLow.getText().toString());

                    count += 1;
                    etJuiceLow.setText(String.valueOf(count));
                } else {
                    etJuiceLow.setText("0");
                }

            }
        });


        btMinusJuiceHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuiceHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etJuiceHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etJuiceHigh.setText(String.valueOf(count));
                } else {
                    etJuiceHigh.setText("0");
                }

            }
        });


        btPlusJuiceHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuiceHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etJuiceHigh.getText().toString());

                    count += 1;
                    etJuiceHigh.setText(String.valueOf(count));
                } else {
                    etJuiceHigh.setText("0");
                }
            }
        });


        btMinusJuicePetLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuicePetLow.getText().length() > 0) {

                    int count = Integer.parseInt(etJuicePetLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etJuicePetLow.setText(String.valueOf(count));
                } else {
                    etJuicePetLow.setText("0");
                }

            }
        });


        btPlusJuicePetLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuicePetLow.getText().length() > 0) {

                    int count = Integer.parseInt(etJuicePetLow.getText().toString());

                    count += 1;
                    etJuicePetLow.setText(String.valueOf(count));
                } else {
                    etJuicePetLow.setText("0");
                }

            }
        });


        btMinusJuicePetHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuicePetHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etJuicePetHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etJuicePetHigh.setText(String.valueOf(count));
                } else {
                    etJuicePetHigh.setText("0");
                }

            }
        });


        btPlusJuicePetHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJuicePetHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etJuicePetHigh.getText().toString());

                    count += 1;
                    etJuicePetHigh.setText(String.valueOf(count));
                } else {
                    etJuicePetHigh.setText("0");
                }

            }
        });


        return v;
    }


    private void setData() {


        if (etWaterLow.getText().toString().length() > 0) {
            salepoint.setPurchaseWaterLow(etWaterLow.getText().toString());
            int count = Integer.parseInt(etWaterLow.getText().toString());
            salepoint.setPurchaseWaterHigh(String.valueOf(count * 2));

        }
        //salepoint.setPurchaseWaterHigh(etWaterHigh.getText().toString());
        if (etJuiceLow.getText().toString().length() > 0) {
            salepoint.setPurchaseJuiceLow(etJuiceLow.getText().toString());
            int count = Integer.parseInt(etJuiceLow.getText().toString());
            salepoint.setPurchaseJuiceHigh(String.valueOf(count * 2));
        }


        //salepoint.setPurchaseJuiceHigh(etJuiceHigh.getText().toString());

        if (etJuicePetLow.getText().toString().length() > 0) {
            salepoint.setPurchaseJuicePetLow(etJuicePetLow.getText().toString());
            int count = Integer.parseInt(etJuicePetLow.getText().toString());
            salepoint.setPurchaseJuicePetHigh(String.valueOf(count * 2));
        }
       // salepoint.setPurchaseJuicePetHigh(etJuicePetHigh.getText().toString());

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
