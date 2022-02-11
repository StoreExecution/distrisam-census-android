package com.storexecution.cocacola.util;

import android.os.Debug;

import com.storexecution.cocacola.BuildConfig;

public class Constants {

    public final static String TAG_POSINFO = "TAG_POSINFO";
    public final static String TAG_TURNOVER = "TAG_TURNOVER";
    public final static String TAG_FRIDGE = "TAG_FRIDGE";
    public final static String TAG_AUDIT = "TAG_AUDIT";
    public final static String TAG_EXTERNALPLV = "TAG_EXTERNALPLV";
    public final static String TAG_INTERNALPLV = "TAG_INTERNALPLV";
    public final static String TAG_RGB = "TAG_RGB";
    public final static String TAG_LOCATION = "TAG_LOCATION";
    public final static String TAG_BARCODE = "TAG_BARCODE";
    public final static String URL_UPDATE = "http://castel.store-execution.com/upload/app/app.apk";
    public final static String PROD_URL = "http://castel.storexecution-dz.com";


    public final static String TEST_URL = "http://192.168.1.3:8000";
//  public final static String TEST_URL = "http://castel2.storexecution-dz.com";

    //public final static String URL_SECTOR = BASE_URL+"/upload/sector/";
    public final static String URL_SECTOR = PROD_URL + "/upload/sector/";
    //public final static String URL_UPDATE = "http://se-census.com/bel_rtm/update/app.apk";
    public final static String URL_RTM = PROD_URL + "/api/rtm/affection?selectedUser=";
    public final static String URL_RTM_TEST = TEST_URL + "/api/rtm/affection?selectedUser=";


    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String ACCESS_TOKEN = "access_token";
    public static String LOGGED = "logged";
    public static String TOKEN_TYPE = "token_type";


    public final static int ZONE_A = 1;
    public final static int ZONE_B = 2;
    public final static int ZONE_C = 3;


    public final static String IMG_POS = "pos";
    public final static String START_TIME = "07:00:00";
    public final static String END_TIME = "20:00:00";
    public final static String IMG_PLV_EXTERNAL = "plv_external";
    public final static String IMG_PLV_EXTERNAL2 = "plv_external2";
    public final static String IMG_PLV_Internal = "plv_internal";

    public final static String IMG_FRIDGE = "fridge";
    public final static String IMG_FRIDGE_BARCODE = "fridge_barcode";
    public final static String IMG_BARCODE = "barcode";
    public final static String IMG_IPLV_METAL_RACK = "metal_rack";
    public final static String IMG_IPLV_FOREX_RACK = "forex_rack";
    public final static String IMG_IPLV_LINEAR = "linear";
    public final static String IMG_IPLV_SKID = "skid";
    public final static String IMG_CFRIDGE = "c_fridge";
    public final static String IMG_ACTIVITYCHANGE = "activity_change";
    public final static String IMG_RGB = "rgb";
    public final static String IMG_RGB_KO = "rgb_ko";

    public static String TILES_BING = "bing";
    public static String TILES_DEFAULT = "default";

    public final static int TYPE_AG = 1;
    public final static int TYPE_SUP = 2;
    public final static int TYPE_FASTFOOD = 3;
    public final static int TYPE_CAFE = 4;
    public final static int TYPE_RESTAURANT = 5;

    public final static int TYPE_THE = 6;
    public final static int TYPE_PATISSERIE = 7;
    public final static int TYPE_BT = 8;

}
