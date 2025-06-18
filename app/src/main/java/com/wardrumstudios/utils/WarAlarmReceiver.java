package com.wardrumstudios.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class WarAlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        System.out.println("WarAlarmReceiver startDownloadServiceIfRequired");
        //DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent, (Class<?>) WarDownloaderService.class);
    }
}
