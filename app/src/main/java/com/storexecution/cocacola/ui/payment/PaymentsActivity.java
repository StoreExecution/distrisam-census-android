package com.storexecution.cocacola.ui.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.PaymentDetailAdapter;
import com.storexecution.cocacola.model.PaymentDetail;
import com.storexecution.cocacola.model.PaymentSummary;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.GsonUtil;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsActivity extends AppCompatActivity {

    /**
     * ButterKnife Code
     **/
    @BindView(R.id.swiperefresh)
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout swiperefresh;
    @BindView(R.id.tvLastUpdate)
    TextView tvLastUpdate;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvPayments)
    androidx.recyclerview.widget.RecyclerView rvPayments;

    PaymentDetailAdapter adapter;
    PaymentSummary paymentSummary;
    SharedPrefUtil sharedPrefUtil;
    ArrayList<PaymentDetail> paymentDetails;
    Gson gson;

    ApiEndpointInterface service;
    User user;
    Map<String, String> headers;
    final String TAG_PAYMENTSUMMARY = "paymentSummary";
    Realm realm;
    SweetAlertDialog sweetAlertDialog;

    /**
     * ButterKnife Code
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        gson = new Gson();
        realm = Realm.getDefaultInstance();
        sharedPrefUtil = SharedPrefUtil.getInstance(this);
        paymentDetails = new ArrayList<>();

        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        user = realm.where(User.class).findFirst();
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPaymentSummary();

            }
        });

        String json = sharedPrefUtil.getString(TAG_PAYMENTSUMMARY, null);
        adapter = new PaymentDetailAdapter(this, paymentDetails, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        rvPayments.setLayoutManager(new LinearLayoutManager(this));

        rvPayments.setAdapter(adapter);

        if (json != null) {
            Log.e("gsonn", json);
            paymentSummary = gson.fromJson(json, PaymentSummary.class);
                 loadSummary(paymentSummary);


        } else {

            fetchPaymentSummary();

        }



    }

    private void fetchPaymentSummary() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitle("Mise a jour");
        sweetAlertDialog.show();
        service.fetchPaymentSummary(headers, user.getId()).enqueue(new Callback<PaymentSummary>() {
            @Override
            public void onResponse(Call<PaymentSummary> call, Response<PaymentSummary> response) {

                Log.e("url", call.request().url().toString());
                Log.e("url", gson.toJson(response.body()));
                swiperefresh.setRefreshing(false);
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismiss();
                if (response.body() != null) {
                    sharedPrefUtil.putString(TAG_PAYMENTSUMMARY, gson.toJson(response.body()));
                    loadSummary(response.body());
                    Toasty.success(PaymentsActivity.this, "Mise a jour reussi", 3000).show();
                } else {
                    Toasty.warning(PaymentsActivity.this, "Erreur serveur", 3000).show();

                }
            }

            @Override
            public void onFailure(Call<PaymentSummary> call, Throwable t) {
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismiss();

                swiperefresh.setRefreshing(false);
                Toasty.error(PaymentsActivity.this, "Erreur de connexion", 3000).show();

            }
        });
    }


    private void loadSummary(PaymentSummary paymentSummary) {


        paymentDetails.clear();
        paymentDetails.addAll(paymentSummary.getPaymentDetails());

        tvTotal.setText(String.format("%,.2f", Double.valueOf(paymentSummary.getTotalAmount())) + " DZD");
        tvLastUpdate.setText("Derniere mise a jour : " + paymentSummary.getLastUpdate());

        adapter.notifyDataSetChanged();
    }
}