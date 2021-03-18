package com.storexecution.cocacola.ui.sync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.storexecution.cocacola.MainActivity;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.SynchAdapter;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.GsonUtils;
import com.storexecution.cocacola.util.RecyclerItemClickListener;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.rlTinda)
    RelativeLayout rlTinda;

    @BindView(R.id.tvSync)
    TextView tvSync;
    @BindView(R.id.rvSalepoints)
    androidx.recyclerview.widget.RecyclerView rvSalepoints;
    /**
     * ButterKnife Code
     **/
    Realm realm;
    RealmList<Salepoint> salepoints;
    RealmList<Salepoint> allSalepoints;
    RealmList<Suivi> suivis;
    RealmList<Photo> photos;
    SynchAdapter synchAdapter;

    SharedPrefUtil sharedPrefUtil;
    User user;

    ApiEndpointInterface service;
    RetrofitClient retrofitClient;
    Gson gson;
    Map<String, String> headers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        ButterKnife.bind(this);
        gson = new Gson();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        salepoints = new RealmList<>();
        suivis = new RealmList<>();
        allSalepoints = new RealmList<>();
        photos = new RealmList<>();
        allSalepoints.addAll(realm.where(Salepoint.class).sort("createdMobileDate", Sort.DESCENDING).findAll());


        synchAdapter = new SynchAdapter(this, allSalepoints, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        rvSalepoints.setLayoutManager(new LinearLayoutManager(this));
        rvSalepoints.setAdapter(synchAdapter);
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        updateNotSynced();

    }

    public void updateNotSynced() {
        long countSalepoint = realm.where(Salepoint.class).equalTo("synced", false).count();
        long countTracking = realm.where(Suivi.class).equalTo("synced", false).count();
        long countPhoto = realm.where(Photo.class).equalTo("synced", false).count();

        tvSync.setText("S: " + countSalepoint + " / P: " + countPhoto + " / T: " + countTracking);
    }

    SweetAlertDialog pDialog;
    int size;
    int synced = 0;
    int limit = 100;

    @OnClick(R.id.fabSync)
    public void sync() {
        pDialog = new SweetAlertDialog(SyncActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sychronisation");
        pDialog.setCancelable(false);
        pDialog.show();
        salepoints.clear();
        suivis.clear();
        photos.clear();

        synced = 0;
        salepoints.addAll(realm.where(Salepoint.class).equalTo("synced", false).findAll());

        suivis.addAll(realm.where(Suivi.class).equalTo("synced", false).findAll());
        photos.addAll(realm.where(Photo.class).equalTo("synced", false).findAll());
        size = salepoints.size() + suivis.size() + photos.size();
        Log.e("sizes ", " " + salepoints.size() + " " + suivis.size() + " " + photos.size());
        // syncTrackings(suivis, 0);
        //syncTracks();
        syncPhotos(photos, 0);
        //syncSalepoints(salepoints, 0);
    }

    public synchronized void syncTracks() {
        suivis.clear();
        suivis.addAll(realm.where(Suivi.class).equalTo("synced", false).limit(limit).findAll());
        if (suivis.size() > 0)
            syncTrackings2(suivis);
        else {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            synchAdapter.notifyDataSetChanged();
        }

        updateNotSynced();
    }


    private void syncTrackings2(RealmList<Suivi> toSync) {

        ArrayList<Suivi> s = new ArrayList<>(realm.copyFromRealm(toSync));
        Log.e("syncTrackings", GsonUtils.trackingListToJson(s) + " ");
        Suivi suivi = new Suivi();


        pDialog.setContentText("Sychronisation " + synced + "/" + size);


        service.postSuivi(headers, GsonUtils.trackingListToJson(s)).enqueue(new Callback<RequestResponse>() {
            @Override
            public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                try {
                    Log.e("Synchsuivie", gson.toJson(response.body()) + " " + response.code() + " " + call.request().body().contentLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response.code() == 200) {
                    realm.beginTransaction();
                    for (Suivi suivi1 : toSync)

                        suivi1.setSynced(true);
                    realm.commitTransaction();
                    synced += limit;

                    syncTracks();

                } else {
                    if (response.code() == 400 || response.code() == 500)
                        triggerRebirth(SyncActivity.this);
                    else
                        syncTracks();


                }

            }

            @Override
            public void onFailure(Call<RequestResponse> call, Throwable t) {
                t.printStackTrace();
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                synchAdapter.notifyDataSetChanged();

            }
        });
        updateNotSynced();


    }


    private void syncTrackings(RealmList<Suivi> toSync, int index) {


        Log.e("syncTrackings", index + " ");
        Suivi suivi = new Suivi();
        if (index < toSync.size()) {
            synced++;
            pDialog.setContentText("Sychronisation " + synced + "/" + size);
            suivi = realm.copyFromRealm(toSync.get(index));


            Log.e("ssuivie", gson.toJson(suivi));

            service.postSuivi(headers, gson.toJson(suivi)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("Synchsuivie", gson.toJson(response.body()) + " " + response.code());

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncTrackings(toSync, (index + 1));
                        else {

                            if (pDialog != null && pDialog.isShowing())
                                pDialog.dismiss();
                            synchAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (response.code() == 400 || response.code() == 500)
                            triggerRebirth(SyncActivity.this);
                        else
                            syncTrackings(toSync, (index + 1));


                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncTrackings(toSync, (index + 1));
                    else {
                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();
                        synchAdapter.notifyDataSetChanged();
                    }

                }
            });
        } else {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            synchAdapter.notifyDataSetChanged();
        }


    }

    private boolean isAllSurveysAndPhotosSynced() {
        long count = realm.where(Suivi.class).equalTo("synced", false).count() + realm.where(Photo.class).equalTo("synced", false).count();
        return count == 0;
    }

    private void syncPhotos(RealmList<Photo> toSync, int index) {


        Log.e("syncPhotos", index + " ");
        Photo photo = new Photo();
        if (index < toSync.size()) {
            synced++;
            pDialog.setContentText("Sychronisation Photos" + synced + "/" + size);
            photo = realm.copyFromRealm(toSync.get(index));
            Photo photo2 = realm.copyFromRealm(toSync.get(index));

            photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.postImage(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("syncPhotos", gson.toJson(response.body()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncPhotos(toSync, (index + 1));
                        else {
                            syncSalepoints(salepoints, 0);

                        }
                    } else {
                        if (response.code() == 400 || response.code() == 500)
                            triggerRebirth(SyncActivity.this);
                        else
                            syncPhotos(toSync, (index + 1));

                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncPhotos(toSync, (index + 1));
                    else {
                        syncSalepoints(salepoints, 0);

                    }

                }
            });
        } else {
            syncSalepoints(salepoints, 0);

        }

        synchAdapter.notifyDataSetChanged();

    }


    private void syncSalepoints(RealmList<Salepoint> toSync, int index) {


        Log.e("syncSalepoints", index + " ");

        Salepoint photo = new Salepoint();
        if (index < toSync.size()) {
            synced++;
            pDialog.setContentText("Sychronisation POS" + synced + "/" + size);
            photo = realm.copyFromRealm(toSync.get(index));
            Salepoint photo2 = realm.copyFromRealm(toSync.get(index));

            //photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.syncSalepoint(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("syncSalepoints", gson.toJson(response.body()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();

                        toSync.get(index).setSynced(true);
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncSalepoints(toSync, (index + 1));
                        else {
                            syncTracks();
//                            if (pDialog != null && pDialog.isShowing())
//                                pDialog.dismiss();
//                            synchAdapter.notifyDataSetChanged();
                        }
                    } else {
                        //  Toasty.error(getApplicationContext(), "Vous etes deconnecter", 5000).show();
                        if (response.code() == 400 || response.code() == 500)
                            triggerRebirth(SyncActivity.this);
                        else {
                            syncSalepoints(toSync, (index + 1));
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncSalepoints(toSync, (index + 1));
                    else {
                        syncTracks();
//                        if (pDialog != null && pDialog.isShowing())
//                            pDialog.dismiss();
//                        synchAdapter.notifyDataSetChanged();


                    }
                }
            });
        } else {
            syncTracks();
//            if (pDialog != null && pDialog.isShowing())
//                pDialog.dismiss();
//            synchAdapter.notifyDataSetChanged();


        }

        synchAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        super.onDestroy();
    }

    public void triggerRebirth(Context context) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        Toasty.error(getApplicationContext(), "Vous etes deconnecter", 5000).show();
        SharedPrefUtil sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());


        sharedPrefUtil.putString(Constants.ACCESS_TOKEN, "");
        sharedPrefUtil.putString(Constants.TOKEN_TYPE, "");
        // sharedPrefUtil.putInt(Constants.TOKEN_TYPE, loginResponse.getUser()());
        //sharedPrefUtil.putString(Constants.TOKEN_TYPE, loginResponse.getTokenType());
        sharedPrefUtil.putBoolean(Constants.LOGGED, false);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(Intent.KEY_RESTART_INTENT, nextIntent);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        //  Runtime.getRuntime().exit(0);
    }
}
