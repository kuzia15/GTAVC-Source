package com.wardrumstudios.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class WarServiceNotifyAlarm extends Service {
    public int icon;
    private NotificationManager mManager;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    public void onStart(Intent intent, int startId) {
        Intent intent1;
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        String string = extras.getString("subject");
        String message = extras.getString("message");
        int icon2 = extras.getInt("icon");
        int smallicon = extras.getInt("smallicon");
        int notifyId = extras.getInt("notifyId");
        String appClassString = extras.getString("class");
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
        e.remove("Alarm" + notifyId);
        e.commit();
        Resources res = getApplicationContext().getResources();
        Context applicationContext = getApplicationContext();
        getApplicationContext();
        this.mManager = (NotificationManager) applicationContext.getSystemService("notification");
        try {
            intent1 = new Intent(getApplicationContext(), Class.forName(appClassString));
        } catch (ClassNotFoundException e2) {
            intent1 = new Intent();
        }
        intent1.addFlags(603979776);

    }

    public void onDestroy() {
        super.onDestroy();
    }
}
