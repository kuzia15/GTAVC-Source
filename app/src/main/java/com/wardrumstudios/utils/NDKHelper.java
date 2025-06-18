package com.wardrumstudios.utils;

import android.annotation.TargetApi;
import android.app.NativeActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;

@TargetApi(9)
public class NDKHelper {
    private static boolean loadedSO = false;
    NativeActivity activity;

    public native void RunOnUiThreadHandler(long j);

    public NDKHelper(NativeActivity act) {
        this.activity = act;
    }

    public void loadLibrary(String soname) {
        if (!soname.isEmpty()) {
            System.loadLibrary(soname);
            loadedSO = true;
        }
    }

    public static Boolean checkSOLoaded() {
        if (loadedSO) {
            return true;
        }
        Log.e("NDKHelper", "--------------------------------------------\n.so has not been loaded. To use JUI helper, please initialize with \nNDKHelper::Init( ANativeActivity* activity, const char* helper_class_name, const char* native_soname);\n--------------------------------------------\n");
        return false;
    }

    private int nextPOT(int i) {
        int pot = 1;
        while (pot < i) {
            pot <<= 1;
        }
        return pot;
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if (bitmapToScale == null) {
            return null;
        }
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / ((float) width), newHeight / ((float) height));
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    public boolean loadTexture(String path) {
        Bitmap bitmap;
        String str = path;
        try {
            if (!path.startsWith("/")) {
                str = "/" + path;
            }
            File file = new File(this.activity.getExternalFilesDir((String) null), str);
            if (file.canRead()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } else {
                bitmap = BitmapFactory.decodeStream(this.activity.getResources().getAssets().open(path));
            }
            if (bitmap != null) {
                GLUtils.texImage2D(3553, 0, bitmap, 0);
            }
            return true;
        } catch (Exception e) {
            Log.w("NDKHelper", "Coundn't load a file:" + path);
            return false;
        }
    }

    public Bitmap openBitmap(String path, boolean iScalePOT) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(this.activity.getResources().getAssets().open(path));
            if (!iScalePOT) {
                return bitmap;
            }
            int originalWidth = getBitmapWidth(bitmap);
            int originalHeight = getBitmapHeight(bitmap);
            int width = nextPOT(originalWidth);
            int height = nextPOT(originalHeight);
            if (originalWidth == width && originalHeight == height) {
                return bitmap;
            }
            return scaleBitmap(bitmap, (float) width, (float) height);
        } catch (Exception e) {
            Log.w("NDKHelper", "Coundn't load a file:" + path);
            return null;
        }
    }

    public int getBitmapWidth(Bitmap bmp) {
        return bmp.getWidth();
    }

    public int getBitmapHeight(Bitmap bmp) {
        return bmp.getHeight();
    }

    public void getBitmapPixels(Bitmap bmp, int[] pixels) {
        int w = bmp.getWidth();
        bmp.getPixels(pixels, 0, w, 0, 0, w, bmp.getHeight());
    }

    public void closeBitmap(Bitmap bmp) {
        bmp.recycle();
    }

    public String getNativeLibraryDirectory(Context appContext) {
        ApplicationInfo ai = this.activity.getApplicationInfo();
        Log.w("NDKHelper", "ai.nativeLibraryDir:" + ai.nativeLibraryDir);
        if ((ai.flags & 128) != 0 || (ai.flags & 1) == 0) {
            return ai.nativeLibraryDir;
        }
        return "/system/lib/";
    }

    public String getApplicationName() {
        ApplicationInfo ai;
        PackageManager pm = this.activity.getPackageManager();
        try {
            ai = pm.getApplicationInfo(this.activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    @TargetApi(17)
    public int getNativeAudioBufferSize() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Integer.parseInt(((AudioManager) this.activity.getSystemService("audio")).getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER"));
        }
        return 0;
    }

    public int getNativeAudioSampleRate() {
        return AudioTrack.getNativeOutputSampleRate(1);
    }

    public void runOnUIThread(final long p) {
        if (checkSOLoaded().booleanValue()) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    NDKHelper.this.RunOnUiThreadHandler(p);
                }
            });
        }
    }
}
