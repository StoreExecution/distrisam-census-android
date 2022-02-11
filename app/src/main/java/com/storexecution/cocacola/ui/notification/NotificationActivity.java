package com.storexecution.cocacola.ui.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.SynchAdapter;
import com.storexecution.cocacola.model.ConcurrentFridge;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.ValidationConditon;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.ui.newpos.NewSurveyActivity;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.Session;
import com.storexecution.cocacola.util.SharedPrefUtil;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class NotificationActivity extends AppCompatActivity {


    @BindView(R.id.rvSalepoints)
    androidx.recyclerview.widget.RecyclerView rvSalepoints;

    Realm realm;
    RealmList<Salepoint> salepoints;
    User user;
    Gson gson;
    ApiEndpointInterface service;
    SharedPrefUtil sharedPrefUtil;
    RealmList<Notification> notifications;
    SynchAdapter synchAdapter;
    Session session;
    PrivacyPolicyDialog dialog;
    Salepoint salepoint;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        gson = new Gson();

        dialog = new PrivacyPolicyDialog(this,
                "https://localhost",
                "https://localhost", false);

        dialog.forceReset();
        dialog.setTitle("Elements a modifiés");
        dialog.setAcceptText("Ouvrir");
        dialog.setCancelText("Fermer");


        dialog.setTitleTextColor(Color.parseColor("#222222"));
        dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));

        //  Title


        //  {terms}Terms of Service{/terms} is replaced by a link to your terms
        //  {privacy}Privacy Policy{/privacy} is replaced by a link to your privacy policy
        dialog.setTermsOfServiceSubtitle("");


        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                Log.e("MainActivity", "Policies accepted");

                // session.setSalepoint(new Salepoint());
                session.setSalepoint(realm.copyFromRealm(salepoint));
                //session.setSalepoint(salepoints.get(position));
                Notification notification = realm.where(Notification.class).equalTo("status", 0).and().equalTo("mobileId", salepoint.getMobile_id()).findFirst();

                Intent intent = new Intent(NotificationActivity.this, NewSurveyActivity.class);
                intent.putExtra("notificationId", notification.getId());
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.e("MainActivity", "Policies not accepted");
                finish();
            }
        });
        salepoints = new RealmList<>();
        notifications = new RealmList<>();
        session = new Session(getApplicationContext());
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());

        notifications.addAll(realm.where(Notification.class).equalTo("status", 0).and().equalTo("userId", user.getId()).findAll());

        RealmQuery realmQuery = realm.where(Salepoint.class);

        for (Notification notification : notifications) {
            Salepoint salepoint = realm.where(Salepoint.class).equalTo("mobile_id", notification.getMobileId()).findFirst();
            if (salepoint != null)
                salepoints.add(salepoint);
        }

//        salepoints.addAll(realmQuery.findAll());
        synchAdapter = new SynchAdapter(this, salepoints, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                dialog.forceReset();
                dialog.reset();
                salepoint = salepoints.get(position);
                Notification notification = realm.where(Notification.class).equalTo("status", 0).and().equalTo("mobileId", salepoints.get(position).getMobile_id()).findFirst();
                dialog.addPoliceLine("<ul> </ul> ");

                for (ValidationConditon validationConditon : notification.getConditions()) {
                    if (validationConditon.getStatus() == 0)
                        dialog.addPoliceLine("<li><big> <b>" + getElementText(validationConditon) + "</b></big><li> ");
                }

                dialog.show();


            }
        });


        rvSalepoints.setLayoutManager(new LinearLayoutManager(this));
        rvSalepoints.setAdapter(synchAdapter);


    }

    private String getElementText(ValidationConditon validationConditon) {

        switch (validationConditon.getDataType()) {
            case Constants.IMG_BARCODE:
                return "Photo Code  barre POS";
            case Constants.IMG_POS:
                return "Photo POS";
            case Constants.IMG_PLV_EXTERNAL:
                return "Photo Plv Exterieur page 1";
            case Constants.IMG_PLV_EXTERNAL2:
                return "Photo Plv Exterieur page 2";
            case Constants.IMG_PLV_Internal:
                return "Photo Plv Interieur";
            case Constants.IMG_FRIDGE:
                return "Photo Frigo Coca Cola ";
            case Constants.IMG_RGB:
                return "RGB Non Valide";
            case Constants.IMG_RGB_KO:
                return "RGB Coca-Cola Non Valide";
            case Constants.IMG_FRIDGE_BARCODE:
                return "Photo code barre Frigo Coca Cola ";
            case Constants.IMG_CFRIDGE:
                String brand = "";
                ConcurrentFridge concurrentFridge = realm.where(ConcurrentFridge.class).equalTo("mobile_id", validationConditon.getDataId()).findFirst();
                if (concurrentFridge != null)
                    brand = concurrentFridge.getBrand();
                return "Photo Frigo BG : " + brand;
        }

        return "";


    }
}