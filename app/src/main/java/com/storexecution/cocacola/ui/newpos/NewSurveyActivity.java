package com.storexecution.cocacola.ui.newpos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.ui.newpos.SalepointTypeFragment;
import com.storexecution.cocacola.util.Session;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class NewSurveyActivity extends AppCompatActivity {

    Session session;
    Salepoint salepoint;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_survey);
        ButterKnife.bind(this);
        session = new Session(getApplicationContext());
        salepoint = session.getSalepoint();
        realm = Realm.getDefaultInstance();
        // ButterKnife.bind(this);
        int rtm_id = 0;
        int notification_id = 0;
        if (getIntent().getExtras() != null) {

            rtm_id = getIntent().getExtras().getInt("rtmId", 0);
            notification_id = getIntent().getExtras().getInt("notificationId", 0);

        }

        SalepointTypeFragment salepointTypeFragment = new SalepointTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("rtmId", rtm_id);
        bundle.putInt("notification_id", notification_id);

        salepointTypeFragment.setArguments(bundle);
        if (savedInstanceState == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, salepointTypeFragment)
                    .commit();

    }

    @Override
    public void onBackPressed() {

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            Salepoint exist = realm.where(Salepoint.class).equalTo("mobile_id", salepoint.getMobile_id()).findFirst();
            if (exist != null) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Modification")

                        .setContentText("Modification en cours , les données ne seront pas enregistrées , voulez-vous quitter ? ")
                        .setConfirmButton("Oui", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Toasty.warning(getApplicationContext(), "Modifications Annulées", 6000).show();

                                session.clearSalepoint();
                                finish();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelButton("Non", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else {
                super.onBackPressed();
            }


        } else {
            super.onBackPressed();
        }
        //super.onBackPressed();

    }
}
