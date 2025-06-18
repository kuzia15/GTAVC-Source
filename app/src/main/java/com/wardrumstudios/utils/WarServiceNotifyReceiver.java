package com.wardrumstudios.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WarServiceNotifyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        String subject = extras.getString("subject");
        String message = extras.getString("message");
        int icon = extras.getInt("icon");
        int smallicon = extras.getInt("smallicon");
        int notifyId = extras.getInt("notifyId");
        String appClass = extras.getString("class");
        Intent service1 = new Intent(context, WarServiceNotifyAlarm.class);
        service1.putExtra("subject", subject);
        service1.putExtra("message", message);
        service1.putExtra("icon", icon);
        service1.putExtra("smallicon", smallicon);
        service1.putExtra("class", appClass);
        service1.putExtra("notifyId", notifyId);
        context.startService(service1);
    }
}
