package com.wardrumstudios.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class WarBootNotify extends Service {
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void CreateAlarmIntent(int id, String alarm) {
        String[] parts = alarm.split("\\|");
        if (parts.length == 6) {
            long epochTime = Long.parseLong(parts[0]);
            int smallicon = Integer.parseInt(parts[1]);
            int icon = Integer.parseInt(parts[2]);
            String title = parts[3];
            String message = parts[4];
            String appClassString = parts[5];
            long currentTimeMillis = System.currentTimeMillis();
            long delay = epochTime - System.currentTimeMillis();
            if (delay < 0) {
                delay = 10000;
            }
            Intent newIntent = new Intent(this, WarServiceNotifyReceiver.class);
            newIntent.putExtra("class", appClassString);
            newIntent.putExtra("icon", icon);
            newIntent.putExtra("smallicon", smallicon);
            //newIntent.putExtra(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, title);
            newIntent.putExtra("message", message);
            newIntent.putExtra("notifyId", id);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(0, System.currentTimeMillis() + delay, PendingIntent.getBroadcast(this, id, newIntent, 0));
        }
    }

    public int onStartCommand(Intent pIntent, int flags, int startId) {
        for (int i = 0; i < 3; i++) {
            String alarm = PreferenceManager.getDefaultSharedPreferences(this).getString("Alarm" + i, "");
            if (alarm.length() > 10) {
                CreateAlarmIntent(i, alarm);
            }
        }
        return super.onStartCommand(pIntent, flags, startId);
    }
}
