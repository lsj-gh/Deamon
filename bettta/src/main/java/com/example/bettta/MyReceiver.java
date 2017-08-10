package com.example.bettta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "android.intent.action.BOOT_COMPLETED") {
            Log.d(TAG, "onReceive: ------------------------开机了");
            context.startService(new Intent(context, MyService.class));
        }
    }
}
