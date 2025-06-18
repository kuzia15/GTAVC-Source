package com.rockstargames.gtavc;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import com.wardrumstudios.utils.WarDownloaderService;
import com.wardrumstudios.utils.WarMedia;
import java.io.File;
import com.bytedance.shadowhook.ShadowHook;

public class GTAVC extends WarMedia {
    public static GTAVC mySelf = null;
    final boolean UseADX = true;
    boolean UseExpansionPack = true;

    static {

        ShadowHook.init(new ShadowHook.ConfigBuilder()
                .setMode(ShadowHook.Mode.UNIQUE)
                .build());


        System.out.println("**** Loading SO's");
        try {
            System.loadLibrary("ImmEmulatorJ");
        } catch (ExceptionInInitializerError e) {
        } catch (UnsatisfiedLinkError e2) {
        }
        System.loadLibrary("GTAVC");
        System.loadLibrary("VCMP");
    }

    public void onCreate(Bundle savedInstanceState) {
        System.out.println("GTAVC onCreate");
        mySelf = this;
        this.HELLO_ID = 123324;
        this.appIntent = new Intent(this, GTAVC.class);
        this.appTickerText = "GTA3 Vice City";
        this.appContentTitle = "Vice City";
        this.appContentText = "Running - Return to Game?";
        this.appStatusIcon = R.mipmap.ic_launcher;
        this.expansionFileName = "main.11.com.rockstargames.gtavc.obb";
        this.baseDirectory = GetGameBaseDirectory();
        Resources resources = getResources();
        if (new File(Environment.getExternalStorageDirectory() + "/GTAVC/" + this.expansionFileName).exists()) {
            this.UseExpansionPack = false;
            this.expansionFileName = "/GTAVC/" + this.expansionFileName;
        }
        WarDownloaderService.BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkGNYE/MizDPktonusH3WUl935fErABUJ2LUuM/N/kdblucD48Wcu2GtCMG8ypyFFSTUsl1tlRr/DRZYNtJxQnMXOG1la2SKgagNF2wfbtO8NAWeg4JMjoxjBVhwNkVTx+RWOBYN+F9j/VpLyfpsvE28buR/oGq97fJJBakrwT/KdRZMBB3+qLQ1m+9Qgi92vFGuI4rvbmuCYRce7GXRmc7VdM/xX4JwHDieYwMRTS+XuWnvb0X0xVS+d/VuZ93PpwL4Zp1YysBXj0MGvsT+v8n581Cs2RMkw5ccUlkTLpzOnc/fPAkVuRVkBfgRw6BE2BlsGLCSpiVRj+GECd1USLQIDAQAB";
        WarDownloaderService.SALT = new byte[]{1, 42, -12, -1, 54, 98, -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84};
        if (this.UseExpansionPack) {
            this.xAPKS = new XAPKFile[1];
            this.xAPKS[0] = new XAPKFile(true, 11, 1484069186);
        }
        this.AddMovieExtension = false;
        this.wantsMultitouch = true;
        this.wantsAccelerometer = true;
        super.onCreate(savedInstanceState);
    }

    public boolean ServiceAppCommand(String cmd, String args) {
        if (!cmd.equalsIgnoreCase("ADXEvent")) {
            return false;
        }
        return false;
    }
}
