package com.storexecution.cocacola.ui.newpos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.storexecution.cocacola.LocalityFragment;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.ui.newpos.audit.SatisfactionAuditFragment;
import com.storexecution.cocacola.ui.newpos.externalplv.ExternalPlvFragment;
import com.storexecution.cocacola.ui.newpos.fridge.FridgeFragment;
import com.storexecution.cocacola.ui.newpos.internalplv.InternalPlvFragment;
import com.storexecution.cocacola.ui.newpos.location.LocationFragment;
import com.storexecution.cocacola.ui.newpos.rgb.RgbInformationFragment;
import com.storexecution.cocacola.ui.newpos.storeinformation.StoreInformationFragment;
import com.storexecution.cocacola.ui.newpos.turnover.TurnOverFragment;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.Session;

import es.dmoral.toasty.Toasty;

public class SalepointFormActivity extends AppCompatActivity {
    Session session;
    Salepoint salepoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salepoint_form);
        String tag = getIntent().getStringExtra("tag");
        Fragment fragment;

        session = new Session(getApplicationContext());
        salepoint = session.getSalepoint();

        // String tag ="tag";
        switch (tag) {
            case Constants.TAG_POSINFO:
                fragment = new StoreInformationFragment();
                //  fragment = new SalepointTypeFragment();

                break;
            case Constants.TAG_TURNOVER:
                fragment = new TurnOverFragment();
                break;

            case Constants.TAG_FRIDGE:
                fragment = new FridgeFragment();
                break;
            case Constants.TAG_AUDIT:
                fragment = new SatisfactionAuditFragment();
                break;
            case Constants.TAG_EXTERNALPLV:
                fragment = new ExternalPlvFragment();
                break;
            case Constants.TAG_INTERNALPLV:
                fragment = new InternalPlvFragment();
                break;
            case Constants.TAG_RGB:
                fragment = new RgbInformationFragment();
                break;
            case Constants.TAG_LOCATION:
                fragment = new LocationFragment();

                break;
            case Constants.TAG_BARCODE:
                fragment = new BarCodeFragment();
                break;

            default:
                fragment = new LocalityFragment();


        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, tag)

                .commit();
    }

    @Override
    public void onBackPressed() {

        salepoint = session.getSalepoint();

        LocationFragment myFragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag(Constants.TAG_LOCATION);
        if (myFragment != null && myFragment.isVisible()) {

            if (salepoint.getClosed() >= 0) {
                super.onBackPressed();
            } else

                Toasty.error(this, "Veuillez specifier si le magsin est fermé", 5000).show();

        } else {
            super.onBackPressed();
        }

    }
}
