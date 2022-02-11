package com.storexecution.cocacola.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.storexecution.cocacola.service.LocationTracker;
import com.storexecution.cocacola.service.TrackingService;
import com.storexecution.cocacola.util.Constants;
import com.storexecution.cocacola.util.DateUtils;
import com.storexecution.cocacola.util.SharedPrefUtil;

import java.text.ParseException;


/**
 * Created by Koceila on 23/02/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 41411;
    SharedPrefUtil sharedPrefUtil;


    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPrefUtil = SharedPrefUtil.getInstance(context);
        //session = new Session("");
        // Log.e(REQUEST_CODE + " ", "alarm " + session.getExpireTime());


        // launch tracking
        //  if (sharedPrefUtil.getBoolean("logged", false) && sharedPrefUtil.getBoolean("working",false)) {
        Log.e("Alarm", " in time2");

        //try {
         //   if (DateUtils.isTimeBetweenTwoTime(Constants.START_TIME, Constants.END_TIME, DateUtils.currentTime())) {
//            if (true) {
//                Log.e("Alarm", " in time");
//                Intent i = new Intent(context, LocationTracker.class);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    // context.startForegroundService(i);
//                    context.startForegroundService(i);
//                } else {
//                    context.startService(i);
//                }
//            } else {
//                Log.e("Alarm", "not in time");
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        // }

    }
}