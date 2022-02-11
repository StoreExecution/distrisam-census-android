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
import android.widget.RelativeLayout;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class TurnOverFragment extends Fragment {


    /**
     * ButterKnife Code
     **/
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.ivbox)
    ImageView ivbox;
    @BindView(R.id.btMinusCocaColaLow)
    Button btMinusCocaColaLow;
    @BindView(R.id.etToCocaColaLow)
    EditText etToCocaColaLow;
    @BindView(R.id.btPlusCocaColaLow)
    Button btPlusCocaColaLow;
    @BindView(R.id.btMinusCocaColaHigh)
    Button btMinusCocaColaHigh;
    @BindView(R.id.etToCocaColaHigh)
    EditText etToCocaColaHigh;
    @BindView(R.id.btPlusCocaColaHigh)
    Button btPlusCocaColaHigh;
    @BindView(R.id.btMinusPepsiLow)
    Button btMinusPepsiLow;
    @BindView(R.id.etToPepsiLow)
    EditText etToPepsiLow;
    @BindView(R.id.btPlusPepsiLow)
    Button btPlusPepsiLow;
    @BindView(R.id.btMinusPepsiHigh)
    Button btMinusPepsiHigh;
    @BindView(R.id.etToPepsiHigh)
    EditText etToPepsiHigh;
    @BindView(R.id.btPlusPepsiHigh)
    Button btPlusPepsiHigh;
    @BindView(R.id.btMinushammoudLow)
    Button btMinushammoudLow;
    @BindView(R.id.ethammoudLow)
    EditText ethammoudLow;
    @BindView(R.id.btPlushammoudLow)
    Button btPlushammoudLow;
    @BindView(R.id.btMinushammoudHigh)
    Button btMinushammoudHigh;
    @BindView(R.id.ethammoudHigh)
    EditText ethammoudHigh;
    @BindView(R.id.btPlushammoudHigh)
    Button btPlushammoudHigh;
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

    /**
     * ButterKnife Code
     **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turn_over, container, false);
        ButterKnife.bind(this, v);

        session = new Session(getActivity().getApplicationContext());
        salepoint = session.getSalepoint();
        if (salepoint.getPurchaseCocaColaLow().length() > 0)
            etToCocaColaLow.setText(salepoint.getPurchaseCocaColaLow());


        if (salepoint.getPurchaseCocaColaHigh().length() > 0)
            etToCocaColaHigh.setText(salepoint.getPurchaseCocaColaHigh());


        if (salepoint.getPurchasePepsiLow().length() > 0)
            etToPepsiLow.setText(salepoint.getPurchasePepsiLow());
        if (salepoint.getPurchasePepsiHigh().length() > 0)
            etToPepsiHigh.setText(salepoint.getPurchasePepsiHigh());

        if (salepoint.getPurchaseHamoudLow().length() > 0)
            ethammoudLow.setText(salepoint.getPurchaseHamoudLow());
        if (salepoint.getPurchaseHamoudHigh().length() > 0)
            ethammoudHigh.setText(salepoint.getPurchaseHamoudHigh());
        if (salepoint.getPurchaseSodaLow().length() > 0)
            etOtherLow.setText(salepoint.getPurchaseSodaLow());

        if (salepoint.getPurchaseSodaHigh().length() > 0)
            etOtherHigh.setText(salepoint.getPurchaseSodaHigh());


        btMinusCocaColaLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToCocaColaLow.getText().length() > 0) {

                    int count = Integer.parseInt(etToCocaColaLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etToCocaColaLow.setText(String.valueOf(count));
                } else {
                    etToCocaColaLow.setText("0");
                }

            }
        });


        btPlusCocaColaLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToCocaColaLow.getText().length() > 0) {

                    int count = Integer.parseInt(etToCocaColaLow.getText().toString());

                    count += 1;
                    etToCocaColaLow.setText(String.valueOf(count));
                } else {
                    etToCocaColaLow.setText("0");
                }
            }
        });


        btMinusCocaColaHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToCocaColaHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etToCocaColaHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etToCocaColaHigh.setText(String.valueOf(count));
                } else {
                    etToCocaColaHigh.setText("0");
                }

            }
        });


        btPlusCocaColaHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToCocaColaHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etToCocaColaHigh.getText().toString());

                    count += 1;
                    etToCocaColaHigh.setText(String.valueOf(count));
                } else {
                    etToCocaColaHigh.setText("0");
                }
            }
        });


        btMinusPepsiLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToPepsiLow.getText().length() > 0) {

                    int count = Integer.parseInt(etToPepsiLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etToPepsiLow.setText(String.valueOf(count));
                } else {
                    etToPepsiLow.setText("0");
                }

            }
        });


        btPlusPepsiLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToPepsiLow.getText().length() > 0) {

                    int count = Integer.parseInt(etToPepsiLow.getText().toString());

                    count += 1;
                    etToPepsiLow.setText(String.valueOf(count));
                } else {
                    etToPepsiLow.setText("0");
                }
            }
        });


        btMinusPepsiHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToPepsiHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etToPepsiHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    etToPepsiHigh.setText(String.valueOf(count));
                } else {
                    etToPepsiHigh.setText("0");
                }

            }
        });


        btPlusPepsiHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etToPepsiHigh.getText().length() > 0) {

                    int count = Integer.parseInt(etToPepsiHigh.getText().toString());

                    count += 1;
                    etToPepsiHigh.setText(String.valueOf(count));
                } else {
                    etToPepsiHigh.setText("0");
                }
            }
        });


        btMinushammoudLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ethammoudLow.getText().length() > 0) {

                    int count = Integer.parseInt(ethammoudLow.getText().toString());
                    if (count > 0)
                        count -= 1;
                    ethammoudLow.setText(String.valueOf(count));
                } else {
                    ethammoudLow.setText("0");
                }

            }
        });


        btPlushammoudLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ethammoudLow.getText().length() > 0) {

                    int count = Integer.parseInt(ethammoudLow.getText().toString());

                    count += 1;
                    ethammoudLow.setText(String.valueOf(count));
                } else {
                    ethammoudLow.setText("0");
                }
            }
        });


        btMinushammoudHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ethammoudHigh.getText().length() > 0) {

                    int count = Integer.parseInt(ethammoudHigh.getText().toString());
                    if (count > 0)
                        count -= 1;
                    ethammoudHigh.setText(String.valueOf(count));
                } else {
                    ethammoudHigh.setText("0");
                }

            }
        });


        btPlushammoudHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ethammoudHigh.getText().length() > 0) {

                    int count = Integer.parseInt(ethammoudHigh.getText().toString());

                    count += 1;
                    ethammoudHigh.setText(String.valueOf(count));
                } else {
                    ethammoudHigh.setText("0");
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

        if (etToCocaColaLow.getText().toString().length() > 0) {
            int count = Integer.parseInt(etToCocaColaLow.getText().toString());
            salepoint.setPurchaseCocaColaLow(etToCocaColaLow.getText().toString());
            salepoint.setPurchaseCocaColaHigh(String.valueOf(count * 2));

        }

//        if (etToCocaColaHigh.getText().toString().length() > 0)
//            salepoint.setPurchaseCocaColaHigh(etToCocaColaHigh.getText().toString());


        if (etToPepsiLow.getText().toString().length() > 0) {

            salepoint.setPurchasePepsiLow(etToPepsiLow.getText().toString());
            int count = Integer.parseInt(etToPepsiLow.getText().toString());
            salepoint.setPurchasePepsiHigh(String.valueOf(count * 2));


        }
//        if (etToPepsiHigh.getText().toString().length() > 0)
//            salepoint.setPurchasePepsiHigh(etToPepsiHigh.getText().toString());


        if (ethammoudLow.getText().toString().length() > 0) {
            salepoint.setPurchaseHamoudLow(ethammoudLow.getText().toString());
            int count = Integer.parseInt(ethammoudLow.getText().toString());
            salepoint.setPurchaseHamoudHigh(String.valueOf(count * 2));
        }
//        if (ethammoudHigh.getText().toString().length() > 0)
//            salepoint.setPurchaseHamoudHigh(ethammoudHigh.getText().toString());

        if (etOtherLow.getText().toString().length() > 0) {
            salepoint.setPurchaseSodaLow(etOtherLow.getText().toString());
            int count = Integer.parseInt(etOtherLow.getText().toString());
            salepoint.setPurchaseSodaHigh(String.valueOf(count * 2));
        }

//        if (etOtherHigh.getText().toString().length() > 0)
//            salepoint.setPurchaseSodaHigh(etOtherHigh.getText().toString());

        session.setSalepoint(salepoint);
    }


    @OnClick(R.id.fabNext)
    public void save() {

        if(checkLimits()){
        TurnOverFragment2 fragment = new TurnOverFragment2();
        setData();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, "storeInfo")
                .addToBackStack(getClass().getName())
                .commit();
              }



    }

    @OnClick(R.id.fabPrev)
    public void prev() {
        setData();
        getActivity().onBackPressed();

    }

    public boolean checkLimits() {
        boolean valid = true;

        switch (salepoint.getSalepointType()) {

            case Constants.TYPE_AG:
                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:
                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;

                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 100) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 200) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 100) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 200) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }
                        break;

                    case Constants.ZONE_B:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 70) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 140) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 70) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 140) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 70) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 140) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:
                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 50) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 100) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 20) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 40) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 50) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 100) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 50) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 100) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }


                break;
            case Constants.TYPE_SUP:

                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 150) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 300) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 100) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 200) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 120) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 240) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 120) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 240) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        break;

                    case Constants.ZONE_B:
                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 80) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 160) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 100) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 200) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 100) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 200) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }
                        break;

                    case Constants.ZONE_C:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 70) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 140) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 100) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 200) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        break;

                }


                break;
            case Constants.TYPE_CAFE:

                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 130) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 260) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 80) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 160) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 100) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 200) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 140) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 280) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        break;

                    case Constants.ZONE_B:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 70) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 140) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 100) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 200) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 80) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 160) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 60) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 120) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 80) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 160) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }

                break;
            case Constants.TYPE_FASTFOOD:


                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 80) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 160) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_B:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 80) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 160) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 60) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 120) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 60) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 120) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 60) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 20) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 40) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 80) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 40) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 80) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }

                break;
            case Constants.TYPE_RESTAURANT:

                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 130) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 260) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 80) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 160) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 100) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 200) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 140) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 280) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_B:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 110) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 220) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 80) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 160) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 60) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 120) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 80) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 160) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }


                break;
            case Constants.TYPE_PATISSERIE:

                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:


                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 80) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 160) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_B:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 80) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 160) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 60) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 120) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 60) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 120) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:
                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 60) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 120) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 20) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 40) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 40) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 80) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 40) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 80) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }

                break;
            case Constants.TYPE_BT:
                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 100) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 200) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 60) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 120) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 80) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 160) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 100) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 200) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_B:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 80) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 160) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 50) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 120) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 60) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 120) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 60) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 120) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 20) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 40) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 40) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 80) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 40) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 80) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }

                break;
            case Constants.TYPE_THE:

                switch (salepoint.getClassification()) {

                    case Constants.ZONE_A:
                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 60) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 120) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 40) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 80) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 40) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 80) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 40) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 80) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        break;

                    case Constants.ZONE_B:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 40) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 80) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 30) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 60) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 30) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 60) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 30) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 60) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                    case Constants.ZONE_C:

                        if (etToCocaColaLow.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaLow.getText().toString()) >= 30) {
                            etToCocaColaLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                            Toasty.error(getActivity(), "error coca", 3000).show();
                        }
                        if (etToCocaColaHigh.getText().toString().length() > 0 && Integer.valueOf(etToCocaColaHigh.getText().toString()) >= 60) {
                            etToCocaColaHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etToPepsiLow.getText().toString().length() > 0 && Integer.valueOf(etToPepsiLow.getText().toString()) >= 20) {
                            etToPepsiLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (etToPepsiHigh.getText().toString().length() > 0 && Integer.valueOf(etToPepsiHigh.getText().toString()) >= 40) {
                            etToPepsiHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        if (ethammoudLow.getText().toString().length() > 0 && Integer.valueOf(ethammoudLow.getText().toString()) >= 20) {
                            ethammoudLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (ethammoudHigh.getText().toString().length() > 0 && Integer.valueOf(ethammoudHigh.getText().toString()) >= 40) {
                            ethammoudHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherLow.getText().toString().length() > 0 && Integer.valueOf(etOtherLow.getText().toString()) >= 20) {
                            etOtherLow.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }

                        if (etOtherHigh.getText().toString().length() > 0 && Integer.valueOf(etOtherHigh.getText().toString()) >= 40) {
                            etOtherHigh.setError("veuillez verifier ce chiffre");
                            valid = false;
                        }


                        break;

                }


                break;
        }

        return valid;


    }
}
