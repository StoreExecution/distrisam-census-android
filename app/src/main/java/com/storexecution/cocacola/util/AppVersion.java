package com.storexecution.cocacola.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by koceila on 19/07/2017.
 */

public class AppVersion {


    /**
     * Function to get Application version name
     * @param context
     * @return version name or empty string if exception
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            return packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Function to get Application version Code
     * @param context
     * @return version code Or 0 if exception
     */

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
