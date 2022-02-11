package com.storexecution.cocacola.network;


import com.storexecution.cocacola.model.ActivityChange;
import com.storexecution.cocacola.model.LoginResponse;
import com.storexecution.cocacola.model.Notification;
import com.storexecution.cocacola.model.PaymentSummary;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Salepoint;
import com.storexecution.cocacola.model.Sector;
import com.storexecution.cocacola.model.UserTrack;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpointInterface {


    @Headers("X-Requested-With: XMLhttpRequest")
    @POST("auth/login")
    Call<LoginResponse> authentification(@Query("username") String username,
                                         @Query("password") String password);

//    @Headers("X-Requested-With: XMLhttpRequest")
//    @FormUrlEncoded
//    @POST("/api/data")
//    Call<RequestResponse> syncSalepoint(@HeaderMap Map<String, String> headers, @Field("data") String form);


    @Headers("X-Requested-With: XMLhttpRequest")
    @FormUrlEncoded
    @POST("/api/salepoint/save")
    Call<RequestResponse> syncSalepoint(@HeaderMap Map<String, String> headers, @Field("data") String form);


    @Headers("X-Requested-With: XMLhttpRequest")
    @FormUrlEncoded
    @POST("/api/tracking")
    Call<RequestResponse> postSuivi(@HeaderMap Map<String, String> headers, @Field("data") String form);


    @Headers("X-Requested-With: XMLhttpRequest")
    @FormUrlEncoded
    @POST("/api/photo")
    Call<RequestResponse> postImage(@HeaderMap Map<String, String> headers, @Field("data") String form);

    @Headers("X-Requested-With: XMLhttpRequest")
    @FormUrlEncoded
    @POST("/api/activitychange")
    Call<RequestResponse> postActivityChange(@HeaderMap Map<String, String> headers, @Field("data") String form);

    @Headers("X-Requested-With: XMLhttpRequest")
    @GET("/api/sector/filter")
    Call<ArrayList<Sector>> fetchSectors(@HeaderMap Map<String, String> headers, @Query("filter") String filter);

    @Headers("X-Requested-With: XMLhttpRequest")
    @GET("/api/payment/summary/{user}")
    Call<PaymentSummary> fetchPaymentSummary(@HeaderMap Map<String, String> headers, @Path("user") int user);

    @Headers("X-Requested-With: XMLhttpRequest")
    @GET("/api/sector/filter")
    Call<ArrayList<UserTrack>> fetchUserTracks(@HeaderMap Map<String, String> headers, @Query("filter") String filter);

    @Headers("X-Requested-With: XMLhttpRequest")
    @FormUrlEncoded
    @POST("/api/user/store")
    Call<RequestResponse> signup(@Field("name") String name,
                                 @Field("username") String username,
                                 @Field("phone") String phone,
                                 @Field("email") String email,
                                 @Field("wilaya") int wilaya,
                                 @Field("password") String password,
                                 @Field("role") int role
    );
    @Headers("X-Requested-With: XMLhttpRequest")
    @GET("/api/notification")
    Call<ArrayList<Notification>> fetchNotification(@HeaderMap Map<String, String> headers);
}
