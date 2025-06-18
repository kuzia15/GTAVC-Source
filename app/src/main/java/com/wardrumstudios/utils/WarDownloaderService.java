package com.wardrumstudios.utils;

//import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class WarDownloaderService /*extends DownloaderService*/ {
    public static String BASE64_PUBLIC_KEY = "";
    public static byte[] SALT = {1, 42, -12, -1, 54, 98, -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84};

    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    public byte[] getSALT() {
        return SALT;
    }

    public String getAlarmReceiverClassName() {
        return WarAlarmReceiver.class.getName();
    }
}
