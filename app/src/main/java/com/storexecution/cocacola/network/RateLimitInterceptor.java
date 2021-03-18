package com.storexecution.cocacola.network;

import java.io.IOException;

import com.google.common.util.concurrent.RateLimiter;

import okhttp3.Interceptor;
import okhttp3.Response;

public class RateLimitInterceptor implements Interceptor {
    private RateLimiter limiter = RateLimiter.create(1);

    @Override
    public Response intercept(Chain chain) throws IOException {
        limiter.acquire(1);
        return chain.proceed(chain.request());
    }
}