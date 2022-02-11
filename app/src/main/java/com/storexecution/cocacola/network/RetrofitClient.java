package com.storexecution.cocacola.network;


import android.util.Log;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.storexecution.cocacola.BuildConfig;
import com.storexecution.cocacola.util.Constants;

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

    private static String BASE_URL;


    //Create the Retrofit instance//
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());

//                    Log.e("respone1",response.request().url().toString());
//            //        Log.e("respone2",response.request().body().toString());
//                    Log.e("Response3", " /  " + response.body().string());

                    return response;
                }
            })

            // This is used to add ApplicationInterceptor.
            //This is used to add NetworkInterceptor.
            .build();


    public static Retrofit getRetrofitInstance() {

        if (BuildConfig.DEBUG) {
            BASE_URL = Constants.TEST_URL + "/api/";
        } else {
            BASE_URL = Constants.PROD_URL + "/api/";

        }
        Log.e("BaseUTL", BASE_URL);
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
