package com.wardrumstudios.utils;

import android.app.Activity;
import android.media.tv.AdRequest;
import android.view.SurfaceView;
import android.widget.RelativeLayout;


public class WarAdMob {
    private static final int AD_Failed = 2;
    private static final int AD_FailedToLoad = 3;
    private static final int AD_LoadInit = 0;
    private static final int AD_Loaded = 2;
    private static final int AD_Requested = 1;
    private static final int AD_Shown = 1;
    private static final int AD_ShownInit = 0;
    private static final String TAG = "WarAdMob";
    private String TestDeviceId;
    protected WarAdMob WarAds;
    /* access modifiers changed from: private */
    public int adLoadResult;
    /* access modifiers changed from: private */
    public int adShowResult;
    /* access modifiers changed from: private */
//    public InterstitialAd mInterstitialAd;
    public SurfaceView surfaceView;
    public Activity warActivity;

    public native void jniWarAdMob();

    public WarAdMob(WarBase activity, SurfaceView warsurface, String DeviceID, String UnitID) {
        this.TestDeviceId = "";
        this.WarAds = null;
        this.adLoadResult = 0;
        this.adShowResult = 0;
        this.WarAds = this;
        jniWarAdMob();
        this.warActivity = activity;
        this.surfaceView = warsurface;
        this.TestDeviceId = DeviceID;
//        this.mInterstitialAd = new InterstitialAd(activity);
//        this.mInterstitialAd.setAdUnitId(UnitID);
//        requestNewInterstitial();
//        this.mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdClosed() {
//                System.out.println("onAdClosed");
//                int unused = WarAdMob.this.adShowResult = 1;
//                WarAdMob.this.requestNewInterstitial();
//                int unused2 = WarAdMob.this.adLoadResult = 1;
//            }
//
//            public void onAdLoaded() {
//                System.out.println("onAdLoaded");
//                int unused = WarAdMob.this.adLoadResult = 2;
//            }
//
//            public void onAdFailedToLoad(int errorCode) {
//                System.out.println("onAdFailedToLoad errorCode " + errorCode);
//                int unused = WarAdMob.this.adLoadResult = 3;
//            }
//        });
        new RelativeLayout(activity).addView(this.surfaceView);
    }

    public void ShowInterstitialAd() {
        this.warActivity.runOnUiThread(new Runnable() {
            public void run() {
//                if (WarAdMob.this.mInterstitialAd.isLoaded()) {
//                    WarAdMob.this.mInterstitialAd.show();
//                    int unused = WarAdMob.this.adShowResult = 1;
//                    return;
//                }
                System.out.println("Ad not loaded for showing.");
                int unused2 = WarAdMob.this.adShowResult = 2;
            }
        });
    }

    /* access modifiers changed from: private */
//    public void requestNewInterstitial() {
//        AdRequest adRequest;
//        if (this.TestDeviceId.length() == 0) {
//            adRequest = new AdRequest.Builder().build();
//        } else {
//            adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(this.TestDeviceId).build();
//        }
//        this.mInterstitialAd.loadAd(adRequest);
//    }

    public void ShowAdPopup() {
        if (this.WarAds != null) {
            this.WarAds.ShowInterstitialAd();
        } else {
            System.out.println("WarAds is null.");
        }
    }

    public int GetAdState(int AdStateType) {
        if (AdStateType == 0) {
            return this.adLoadResult;
        }
        return this.adShowResult;
    }
}
