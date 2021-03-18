package com.storexecution.cocacola.network;


import android.util.Log;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static RateLimiter limiter = RateLimiter.create(3);

//Define the base URL//


    //private static final String BASE_URL = "http://projet.livraison.store-execution.com//api/mobile/";
    //  public static final String HOST = "http://jti.hello-dev.com/";
   // public static final String HOST = "http://192.168.1.89:8000";
    //  private static final String HOST = "https://campagne.se-census.com/";
    // private static final String HOST = "http://castel.store-execution.com";
    private static final String HOST = "http://castel.se-census.com";
    private static final String BASE_URL = HOST + "/api/";


    //Create the Retrofit instance//
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Response response = chain.proceed(chain.request());
//
//                    Log.e("Response", " /  " + response.body().string());
//
//                    return response;
//                }
//            }) // This is used to add ApplicationInterceptor.
            //This is used to add NetworkInterceptor.
            .build();


    public static Retrofit getRetrofitInstance() {
        GsonBuilder builder = new GsonBuilder();
        // builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        Gson gson = builder.create();
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
//Add the converter//
                    .addConverterFactory(GsonConverterFactory.create())
//Build the Retrofit instance//
                    .build();
        }
        return retrofit;
    }

}
