package com.storexecution.cocacola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.storexecution.cocacola.model.Commune;
import com.storexecution.cocacola.model.Daira;
import com.storexecution.cocacola.model.LoginResponse;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.model.Wilaya;
import com.storexecution.cocacola.network.ApiEndpointInterface;

import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.AlarmTask;
import com.storexecution.cocacola.util.AppVersion;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPasssword)
    EditText etPasssword;
    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.btSignUp)
    Button btSignUp;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    /**
     * ButterKnife Code
     **/
    SharedPrefUtil sharedPrefUtil;
    User user;
    Realm realm;
    ApiEndpointInterface service;
    RetrofitClient retrofitClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        tvVersion.setText("Version : " + AppVersion.getVersionName(this));

        if (!sharedPrefUtil.getBoolean("wilaya", false))
            try {
                InputStream inputStream = getAssets().open("algeria_cities.json");

                realm.beginTransaction();
                realm.delete(Wilaya.class);
                realm.delete(Daira.class);
                realm.delete(Commune.class);
                realm.createOrUpdateAllFromJson(Wilaya.class, inputStream);
                realm.commitTransaction();
                sharedPrefUtil.putBoolean("wilaya", true);
                Wilaya wilaya = realm.where(Wilaya.class).equalTo("id", 16).findFirst();
                Daira daira = wilaya.getDairas().first();
                Log.e("wila", daira.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (sharedPrefUtil.getBoolean(Constants.LOGGED, false) == false) {
            Toasty.warning(getApplicationContext(), "Veuillez vous connecter ", 3000).show();
        } else {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        etUserName.setText(sharedPrefUtil.getString(Constants.USERNAME, ""));
        etPasssword.setText(sharedPrefUtil.getString(Constants.PASSWORD, ""));
    }


    @OnClick(R.id.btLogin)
    public void login() {

        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Connexion");
        pDialog.setCancelable(false);
        pDialog.show();
        Call<LoginResponse> auth = service.authentification(etUserName.getText().toString(), etPasssword.getText().toString());
        auth.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("request ", response.message());
                //  pDialog.dismissWithAnimation();
                if (response.code() == 200) {
                    sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext(), response.body().getUser().getId());
                    pDialog.setTitleText("Authentification reussi")
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    LoginResponse loginResponse = response.body();
                    Log.e("token", loginResponse.getAccessToken());
                    sharedPrefUtil.putString(Constants.USERNAME, etUserName.getText().toString());
                    sharedPrefUtil.putString(Constants.PASSWORD, etPasssword.getText().toString());
                    sharedPrefUtil.putString(Constants.ACCESS_TOKEN, loginResponse.getAccessToken());
                    sharedPrefUtil.putString(Constants.TOKEN_TYPE, loginResponse.getTokenType());
                    // sharedPrefUtil.putInt(Constants.TOKEN_TYPE, loginResponse.getUser()());
                    //sharedPrefUtil.putString(Constants.TOKEN_TYPE, loginResponse.getTokenType());
                    sharedPrefUtil.putBoolean(Constants.LOGGED, true);
                    new AlarmTask(LoginActivity.this).run();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            // This will create a new object in Realm or throw an exception if the
                            // object already exists (same primary key)
                            // realm.copyToRealm(obj);

                            // This will update an existing object with the same primary key
                            // or create a new object if an object with no primary key = 42
                            realm.delete(User.class);
                            realm.copyToRealmOrUpdate(loginResponse.getUser());
                        }
                    });
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();


                } else if (response.code() == 401) {

                    pDialog.setTitleText("Veuillez verifier vos identifients")
                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);

                } else {

                    pDialog.setTitleText(response.body() + "\n code : " + response.code())
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);


                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.setTitleText("Verifier votre connexion internet")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                Log.e("login", "failed" + t.toString());

            }
        });

    }

    @OnClick(R.id.btSignUp)
    public void signup() {

        startActivity(new Intent(this, SignupActivity.class));
    }

}
