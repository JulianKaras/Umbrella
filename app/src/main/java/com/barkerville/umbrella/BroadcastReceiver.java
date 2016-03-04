package com.barkerville.umbrella;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends BroadcastReceiver {
    public BroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Alarm alarm = new Alarm();
            alarm.setAlarm(context);

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
