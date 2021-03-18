package com.storexecution.cocacola.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by koceila on 12/03/16.
 */
public class Base64Util {

    public static String bitmapToBase64String(Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 40, baos);


        byte[] b = baos.toByteArray();

        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        try {
            baos.close();
            baos = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encoded;
    }

    public static Bitmap Base64ToBitmap(String photo) {

        byte[] imgb = (byte[]) Base64.decode(photo, Base64.DEFAULT);
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        return BitmapFactory.decodeByteArray(imgb, 0, imgb.length, options);

    }
    public static Bitmap Base64ToBitmap(String photo, int size) {

        byte[] imgb = (byte[]) Base64.decode(photo, Base64.DEFAULT);
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeByteArray(imgb, 0, imgb.length, options);

    }

}

