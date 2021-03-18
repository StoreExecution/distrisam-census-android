package com.storexecution.cocacola.network;


import com.storexecution.cocacola.model.LoginResponse;
import com.storexecution.cocacola.model.RequestResponse;
import com.storexecution.cocacola.model.Salepoint;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
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
}
