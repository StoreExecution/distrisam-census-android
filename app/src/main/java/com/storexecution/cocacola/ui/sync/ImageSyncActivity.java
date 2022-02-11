package com.storexecution.cocacola.ui.sync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.storexecution.cocacola.MainActivity;
import com.storexecution.cocacola.R;
import com.storexecution.cocacola.adapter.ImageSyncAdapter;
import com.storexecution.cocacola.adapter.SynchAdapter;
import com.storexecution.cocacola.model.ActivityChange;
import com.storexecution.cocacola.model.Photo;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Suivi;
import com.storexecution.cocacola.model.User;
import com.storexecution.cocacola.network.ApiEndpointInterface;
import com.storexecution.cocacola.network.RetrofitClient;
import com.storexecution.cocacola.util.Base64Util;
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

public class ImageSyncActivity extends AppCompatActivity {
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
    RealmList<ActivityChange> changes;
    RealmList<Photo> photos;
    ImageSyncAdapter synchAdapter;


    RetrofitClient retrofitClient;
    Gson gson;
    Map<String, String> headers;
    SharedPrefUtil sharedPrefUtil;
    User user;

    ApiEndpointInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_inage);


        ButterKnife.bind(this);
        gson = new Gson();
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        service = RetrofitClient.getRetrofitInstance().create(ApiEndpointInterface.class);
        sharedPrefUtil = SharedPrefUtil.getInstance(getApplicationContext());
        headers = new HashMap<>();
        headers.put("Authorization", "bearer " + sharedPrefUtil.getString(Constants.ACCESS_TOKEN, ""));
        // updateNotSynced();

        photos = new RealmList<>();
        photos.addAll(realm.where(Photo.class).equalTo("synced", false).sort("id", Sort.DESCENDING).findAll());


        synchAdapter = new ImageSyncAdapter(this, photos, new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ImagePreview(position);
            }
        });

        rvSalepoints.setLayoutManager(new LinearLayoutManager(this));
        rvSalepoints.setAdapter(synchAdapter);


    }


    public void ImagePreview(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Photo photo = photos.get(position);
                photos.clear();
                photos.add(photo);
                pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Sychronisation");
                pDialog.setCancelable(false);
                pDialog.show();

                synced = 0;
                error = 0;
                failed = 0;
                attemp = 0;
                success = 0;
                errorimage = 0;
                errorserver = 0;
                size = photos.size();
                syncPhotos(photos, 0);

            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_image, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

                ImageView iv = (ImageView) dialog.findViewById(R.id.ivImage);

                Photo photo = photos.get(position);
                if (photo != null) {
                    Bitmap img = Base64Util.Base64ToBitmap(photo.getImage(), 1);
                    img = getResizedBitmap(img, 700);
                    iv.setImageBitmap(img);
                }


            }
        });

        dialog.show();
    }

    public void updateNotSynced() {
        long countSalepoint = realm.where(Salepoint.class).equalTo("synced", false).count();
        long countTracking = realm.where(Suivi.class).equalTo("synced", false).count();
        long countPhoto = realm.where(Photo.class).equalTo("synced", false).equalTo("user_id", user.getId()).count();
        long countchange = realm.where(ActivityChange.class).equalTo("synced", false).count();

        tvSync.setText("S: " + countSalepoint + " / P: " + countPhoto + " / T: " + countTracking + " / C: " + countchange);
    }

    SweetAlertDialog pDialog;
    long size;
    int synced = 0;
    int limit = 100;
    int error = 0;
    int failed = 0;

    int success;
    int errorimage;
    int errorserver;

    @OnClick(R.id.fabSync)
    public void sync() {
        pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sychronisation");
        pDialog.setCancelable(false);
        pDialog.show();

        photos.clear();

        synced = 0;
        error = 0;
        failed = 0;
        attemp = 0;
        success = 0;
        errorimage = 0;
        errorserver = 0;

        photos.addAll(realm.where(Photo.class).equalTo("synced", false).and().notEqualTo("error", true).findAll());
        size = photos.size();
        // syncTrackings(suivis, 0);
        //syncTracks();

        syncPhotos(photos, 0);
        //syncSalepoints(salepoints, 0);
    }

    int attemp = 0;





    private boolean isAllSurveysAndPhotosSynced() {
        long count = realm.where(Suivi.class).equalTo("synced", false).count() + realm.where(Photo.class).equalTo("synced", false).count();
        return count == 0;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private void syncPhotos(RealmList<Photo> toSync, int index) {


        Log.e("syncPhotos", index + " ");
        Photo photo = new Photo();
        if (index < toSync.size()) {
            synced++;

            pDialog.setContentText("Sychronisation Photos " + synced + "/" + toSync.size());
            photo = realm.copyFromRealm(toSync.get(index));
            Photo photo2 = realm.copyFromRealm(toSync.get(index));

            if (photo.getImage().getBytes().length >= (1024 * 500) || photo.isError()) {
                Bitmap img = Base64Util.Base64ToBitmap(photo.getImage(), 1);
                img = getResizedBitmap(img, 700);
                photo.setImage(Base64Util.bitmapToBase64String(img, 90));
            }

            final String photostring = photo.getImage();


            photo2.setImage("");
            Log.e("sphotoe", gson.toJson(photo2));

            service.postImage(headers, gson.toJson(photo)).enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    Log.e("syncPhotos", gson.toJson(response.body()) + " ");
                    Log.e("syncPhotos", gson.toJson(response.code()) + " ");

                    if (response.code() == 200) {
                        realm.beginTransaction();


                        toSync.get(index).setSynced(true);
                        success++;
                        realm.commitTransaction();

                        if (index + 1 < toSync.size())
                            syncPhotos(toSync, (index + 1));
                        else {
                            if (pDialog != null && pDialog.isShowing())
                                pDialog.dismiss();
                            pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.NORMAL_TYPE);
                            pDialog.setContentText("Reussi :" + success + " Erreur  :" + errorimage + " Erreur de connexion :" + errorserver);
                            pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    photos.clear();
                                    photos.addAll(realm.where(Photo.class).equalTo("synced", false).sort("id", Sort.DESCENDING).findAll());

                                    synchAdapter.notifyDataSetChanged();
                                }
                            });
                            pDialog.show();

                        }
                    } else if (response.code() == 403) {
                        realm.beginTransaction();
                        if (!toSync.get(index).isError()) {


                            toSync.get(index).setSynced(false);
                            toSync.get(index).setError(true);
                        } else {

                         //   toSync.get(index).setImage(photostring);
                        }

                        realm.commitTransaction();

                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();
                        pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        pDialog.setContentText("Reussi :" + success + " Erreur  :" + errorimage + " Erreur de connexion :" + errorserver);
                        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                photos.clear();
                                photos.addAll(realm.where(Photo.class).equalTo("synced", false).sort("id", Sort.DESCENDING).findAll());

                                synchAdapter.notifyDataSetChanged();
                            }
                        });
                        pDialog.show();


                    } else {
                        errorimage++;
                        if (response.code() == 400 || response.code() == 500)
                            triggerRebirth(ImageSyncActivity.this, response.code(), response.errorBody().toString());
                        else
                            syncPhotos(toSync, (index + 1));

                    }

                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    errorserver++;
                    t.printStackTrace();
                    if (index + 1 < toSync.size())
                        syncPhotos(toSync, (index + 1));
                    else {

                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();
                        pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        pDialog.setContentText("Reussi :" + success + " Erreur  :" + errorimage + " Erreur de connexion :" + errorserver);
                        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                photos.clear();
                                photos.addAll(realm.where(Photo.class).equalTo("synced", false).sort("id", Sort.DESCENDING).findAll());

                                synchAdapter.notifyDataSetChanged();
                            }
                        });
                        pDialog.show();

                    }

                }
            });
        } else {
            // syncSalepoints(salepoints, 0);
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new SweetAlertDialog(ImageSyncActivity.this, SweetAlertDialog.NORMAL_TYPE);
            pDialog.setContentText("Reussi :" + success + " Erreur  :" + errorimage + " Erreur de connexion :" + errorserver);
            pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    photos.clear();
                    photos.addAll(realm.where(Photo.class).equalTo("synced", false).sort("id", Sort.DESCENDING).findAll());

                    synchAdapter.notifyDataSetChanged();
                }
            });
            pDialog.show();
        }

//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
//        pDialog.setContentText("Reussi :" + success + " Erreur  :" + errorimage + " Erreur de connexion :" + errorserver);
//        pDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                sweetAlertDialog.dismiss();
//            }
//        });
//        pDialog.show();
        synchAdapter.notifyDataSetChanged();

    }






    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        super.onDestroy();
    }

    public void triggerRebirth(Context context, int status, String error) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        Toasty.error(getApplicationContext(), "Vous etes deconnecter - erreur code : " + status + " " + error, 5000).show();
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
