package com.storexecution.cocacola.util;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by koceila on 26/11/15.
 */
public class PicassoSingleton {
    private static Picasso instance;

    public static Picasso with(Context context) {
        if (instance == null) {
            instance = new Picasso.Builder(context.getApplicationContext()).build();
        }
        return instance;
    }

    private PicassoSingleton() {
        throw new AssertionError("No instances.");
    }
}

