package com.storexecution.cocacola.ui.newpos.turnover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
public class TurnOverFragment2 extends Fragment {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.ivbox)
    ImageView ivbox;
    @BindView(R.id.btMinusRouibaLow)
    Button btMinusRouibaLow;
    @BindView(R.id.etRouibaLow)
    EditText etRouibaLow;
    @BindView(R.id.btPlusRouibaLow)
    Button btPlusRouibaLow;
    @BindView(R.id.btMinusRouibaHigh)
    Button btMinusRouibaHigh;
    @BindView(R.id.etRouibaHigh)
    EditText etRouibaHigh;
    @BindView(R.id.btPlusRouibaHigh)
    Button btPlusRouibaHigh;
    @BindView(R.id.btMinusRouibaPetLow)
    Button btMinusRouibaPetLow;
    @BindView(R.id.etRouibaPetLow)
    EditText etRouibaPetLow;
    @BindView(R.id.btPlusRouibaPetLow)
    Button btPlusRouibaPetLow;
    @BindView(R.id.btMinusRouibaPetHigh)
    Button btMinusRouibaPetHigh;
    @BindView(R.id.etRouibaPetPetHigh)
    EditText etRouibaPetPetHigh;
    @BindView(R.id.btPlusRouibaPetHigh)
    Button btPlusRouibaPetHigh;
    @BindView(R.id.cvRouiba)
    androidx.cardview.widget.CardView cvRouiba;
    @BindView(R.id.cbRouiba)
    CheckBox cbRouiba;
    @BindView(R.id.cvRouibapet)
    androidx.cardview.widget.CardView cvRouibapet;
    @BindView(R.id.cbRouibapet)
    CheckBox cbRouibapet;

    /**
     * ButterKnife Code
     **/
    Salepoint salepoint;
    Session session;

    public TurnOverFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turn_over2, container, false);
        ButterKnife.bind(this, v);

        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();

        if (salepoint.getPurchaseRouibaLow().length() > 0)
            etRouibaLow.setText(salepoint.getPurchaseRouibaLow());
        if (salepoint.getPurchaseRouibaHigh().length() > 0)
            etRouibaHigh.setText(salepoint.getPurchaseRouibaHigh());

        if (salepoint.getPurchaseRouibaPetLow().length() > 0)
            etRouibaPetLow.setText(salepoint.getPurchaseRouibaPetLow());


        if (salepoint.getPurchaseRouibaPetHigh().length() > 0)
            etRouibaPetPetHigh.setText(salepoint.getPurchaseRouibaPetHigh());


        cbRouiba.setChecked(salepoint.isDnRouiba());
        cbRouibapet.setChecked(salepoint.isDnRouibaPet());


        btMinusRouibaLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaLow.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etRouibaLow.setText(String.valueOf(count));
                } else {
                    etRouibaLow.setText("0");
                }

            }
        });


        btPlusRouibaLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaLow.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaLow.getText().toString());

                    count += 1;
                    etRouibaLow.setText(String.valueOf(count));
                } else {
                    etRouibaLow.setText("0");
                }
            }
        });

        btMinusRouibaHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etRouibaHigh.setText(String.valueOf(count));
                } else {
                    etRouibaHigh.setText("0");
                }

            }
        });


        btPlusRouibaHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaHigh.getText().toString());

                    count += 1;
                    etRouibaHigh.setText(String.valueOf(count));
                } else {
                    etRouibaHigh.setText("0");
                }
            }
        });


        btMinusRouibaPetLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaPetLow.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaPetLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etRouibaPetLow.setText(String.valueOf(count));
                } else {
                    etRouibaPetLow.setText("0");
                }

            }
        });


        btPlusRouibaPetLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaPetLow.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaPetLow.getText().toString());

                    count += 1;
                    etRouibaPetLow.setText(String.valueOf(count));
                } else {
                    etRouibaPetLow.setText("0");
                }
            }
        });


        btMinusRouibaPetHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaPetPetHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaPetPetHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etRouibaPetPetHigh.setText(String.valueOf(count));
                } else {
                    etRouibaPetPetHigh.setText("0");
                }

            }
        });


        btPlusRouibaPetHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRouibaPetPetHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etRouibaPetPetHigh.getText().toString());

                    count += 1;
                    etRouibaPetPetHigh.setText(String.valueOf(count));
                } else {
                    etRouibaPetPetHigh.setText("0");
                }
            }
        });


        return v;
    }

    @OnClick(R.id.cvRouiba)
    public void rouiba() {

        cbRouiba.setChecked(!cbRouiba.isChecked());
    }

    @OnClick(R.id.cvRouibapet)
    public void cvRouibapet() {

        cbRouibapet.setChecked(!cbRouibapet.isChecked());
    }


    private void setData() {
        if (etRouibaLow.getText().toString().length() > 0) {
            salepoint.setPurchaseRouibaLow(etRouibaLow.getText().toString());
            int count = Integer.parseInt(etRouibaLow.getText().toString());
            salepoint.setPurchaseRouibaHigh(String.valueOf(count * 2));
        }
       // salepoint.setPurchaseRouibaHigh(etRouibaHigh.getText().toString());

        if (etRouibaLow.getText().toString().length() > 0) {
        salepoint.setPurchaseRouibaPetLow(etRouibaPetLow.getText().toString());
            int count = Integer.parseInt(etRouibaPetLow.getText().toString());
            salepoint.setPurchaseRouibaPetHigh(String.valueOf(count * 2));

        }
      //  salepoint.setPurchaseRouibaPetHigh(etRouibaPetPetHigh.getText().toString());

        salepoint.setDnRouiba(cbRouiba.isChecked());
        salepoint.setDnRouibaPet(cbRouibapet.isChecked());
        session.setSalepoint(salepoint);

    }


    @OnClick(R.id.fabNext)
    public void save() {
        TurnOverFragment3 fragment = new TurnOverFragment3();
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
}
