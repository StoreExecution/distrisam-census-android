package com.storexecution.cocacola.receiver;

import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.storexecution.cocacola.util.AlarmTask;


/**
 * Created by Koceila on 23/02/2017.
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


// si la tablette est redemarre redemare le tracking
        new AlarmTask(context).run();
        // Launch the specified service when this message is received

        // Intent startServiceIntent = new Intent(context, MyTestService.class);
        // startWakefulService(context, startServiceIntent);
    }
}