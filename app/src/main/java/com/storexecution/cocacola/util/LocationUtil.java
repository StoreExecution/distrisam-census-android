package com.storexecution.cocacola.util;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by koceila on 19/07/2017.
 */

public class LocationUtil {


    public static boolean isGpsActive(Context context) {


        //  LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //..return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled(context);
    }

    public static boolean isGPSEnabled(Context context) {
        //  Context context = Session.getInstance().getCurrentPresenter().getViewContext();

        LocationManager locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean GPS_Sts = locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return GPS_Sts;
    }
}
