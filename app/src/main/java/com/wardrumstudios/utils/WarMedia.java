package com.wardrumstudios.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.Process;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

import com.nvidia.devtech.NvUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class WarMedia extends WarGamepad implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {
    static final int REQUEST_READ_EXTERNAL_STORAGE = 8001;
    public boolean AddMovieExtension;
    protected boolean AllowLongPressForExit;
    String DeviceCountry;
    public String DeviceLocale;
    boolean DisplayMovieTextOnTap;
    final boolean DoLog;
    boolean ForceSize = false;
    boolean GameIsFocused;
    protected int HELLO_ID;
    boolean IsInValidation;
    /* access modifiers changed from: private */
    public boolean IsScreenPaused = false;
    int IsShowingBackMessage;
    boolean KeepAspectRatio = true;
    boolean MovieIsSkippable;
    boolean MovieTextDisplayed;
    protected int SpecialBuildType;
    protected boolean UseExpansionFiles;
    protected boolean UseFTPDownload = false;
    public boolean UseWarDownloader = true;
    final boolean UsingSounds;
    Activity activity;
    protected String apkFileName = "";
    protected CharSequence appContentText;
    protected CharSequence appContentTitle;
    protected Intent appIntent;
    protected int appStatusIcon;
    protected CharSequence appTickerText;
    AudioManager audioManager;
    int audioMax;
    int audioVolume;
    int availableMemory;
    int bIsPlayingMovie;
    boolean bMoviePlayerPaused;
    public String baseDirectory;
    public String baseDirectoryRoot;
    protected int baseDisplayHeight;
    protected int baseDisplayWidth;
    protected int cachedSizeRead;
    protected boolean checkForMaxDisplaySize;
    LinearLayout col1;
    LinearLayout col2;
    LinearLayout col3;
    String currentMovieFilename;
    int currentMovieLength;
    int currentMovieMS;
    int currentMovieOffset;
    float currentMovieVolume;
    int currentTempID;
    SurfaceHolder customMovieHolder;
    SurfaceView customMovieSurface;
    public MediaPlayer dialogPlayer;
    SurfaceHolder downloadHolder;
    SurfaceView downloadView;
    boolean downloadViewCreated;
    AlertDialog exitDialog;
    protected String expansionFileName = "";
    protected boolean hasTouchScreen;
    boolean isCompleting;
    boolean isPhone;
    /* access modifiers changed from: private */
    public boolean isUserPresent = true;
    long lastMovieStop;
    protected int lastNetworkAvailable;
    public LinearLayout llSplashView;
    public String localIp;
    private Locale locale;
    int mAscent;
    private TextView mAverageSpeed;
    Camera mCamera;
    /* access modifiers changed from: private */
    public boolean mCancelValidation;
    /* access modifiers changed from: private */
    public View mCellMessage;
    /* access modifiers changed from: private */
    public View mDashboard;
    private ProgressBar mPB;
    /* access modifiers changed from: private */
    public Button mPauseButton;
    private TextView mProgressFraction;
    private TextView mProgressPercent;
    private final BroadcastReceiver mReceiver;
    /* access modifiers changed from: private */
    private int mState;
    /* access modifiers changed from: private */
    public boolean mStatePaused;
    /* access modifiers changed from: private */
    public TextView mStatusText;
    Paint mTextPaint;
    private TextView mTimeRemaining;
    private Button mWiFiSettingsButton;
    private WifiManager mWifiManager;
    ActivityManager.MemoryInfo memInfo;
    int memoryThreshold;
    protected DisplayMetrics metrics;
    ActivityManager mgr;
    int movieLooping;
    public MediaPlayer moviePlayer;
    String movieSubtitleText;
    boolean movieTextDisplayNow;
    SurfaceHolder movieTextHolder;
    int movieTextScale;
    SurfaceView movieTextSurface;
    TextView movieTextView;
    LinearLayout movieView;
    boolean movieViewCreated;
    int movieViewHeight;
    boolean movieViewIsActive;
    TextView movieViewText;
    int movieViewWidth;
    int movieViewX;
    int movieViewY;
    SurfaceHolder movieWindowHolder;
    SurfaceView movieWindowSurface;
    public MediaPlayer musicPlayer;
    int[] myPid;
    private Vibrator myVib;
    private PowerManager.WakeLock myWakeLock;
    protected boolean overrideExpansionName = false;
    protected String patchFileName = "";
    protected byte[] privateData;
    protected String[] privateDataFiles;
    LinearLayout row1;
    LinearLayout row2;
    LinearLayout row3;
    TextPaint sTextPaint;
    float screenWidthInches;
    boolean skipMovies;
    boolean skipSound;
    boolean soundLog;
    ArrayList<PoolInfo> sounds;
    Paint tPaint;
    TextPaint textPaint;
    int totalMemory;
    long[][] vibrateEffects;
    public boolean waitForPermissions = false;
    public WarDownloader wardown = null;
    public XAPKFile[] xAPKS = null;

    private native void initTouchSense(Context context);

    public native void NativeNotifyNetworkChange(int i);

    public native void setTouchSenseFilepath(String str);

    public WarMedia() {
        this.DoLog = !this.FinalRelease;
        this.moviePlayer = null;
        this.skipSound = false;
        this.skipMovies = false;
        this.isCompleting = false;
        this.bIsPlayingMovie = 0;
        this.soundLog = false;
        this.DisplayMovieTextOnTap = false;
        this.movieSubtitleText = "";
        this.movieTextDisplayNow = false;
        this.SpecialBuildType = 0;
        this.activity = null;
        this.HELLO_ID = 12346;
        this.appStatusIcon = 0;
        this.appTickerText = "MyApp";
        this.appContentTitle = "MyApp";
        this.appContentText = "Running - Return to Game?";
        this.appIntent = null;
        this.UseExpansionFiles = false;
        this.AllowLongPressForExit = false;
        this.hasTouchScreen = true;
        this.isPhone = false;
        this.currentTempID = 100000;
        this.baseDirectory = "/mnt/sdcard/";
        this.baseDirectoryRoot = "/mnt/sdcard/";
        this.AddMovieExtension = true;
        this.IsShowingBackMessage = 0;
        this.exitDialog = null;
        this.cachedSizeRead = 0;
        this.UsingSounds = false;
        this.memoryThreshold = 0;
        this.availableMemory = 0;
        this.totalMemory = 0;
        this.screenWidthInches = 0.0f;
        this.baseDisplayWidth = 1920;
        this.baseDisplayHeight = 1080;
        this.lastNetworkAvailable = -1;
        this.checkForMaxDisplaySize = false;
        this.mWifiManager = null;
        this.downloadViewCreated = false;
        this.GameIsFocused = false;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!WarMedia.this.FinalRelease) {
                    System.out.println("BroadcastReceiver WarMedia " + action.toString());
                }
                if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("BroadcastReceiver ACTION_SCREEN_OFF");
                    }
                    boolean unused = WarMedia.this.isUserPresent = false;
                } else if ("android.intent.action.USER_PRESENT".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("BroadcastReceiver " + action);
                    }
                    KeyguardManager keyguardManager = (KeyguardManager) WarMedia.this.getSystemService(Context.KEYGUARD_SERVICE);
                    if (!WarMedia.this.FinalRelease) {
                        System.out.println("inKeyguardRestrictedInputMode " + keyguardManager.inKeyguardRestrictedInputMode());
                    }
                    if (!keyguardManager.inKeyguardRestrictedInputMode()) {
                        boolean unused2 = WarMedia.this.isUserPresent = true;
                        if (WarMedia.this.IsScreenPaused) {
                            boolean unused3 = WarMedia.this.IsScreenPaused = false;
                            if (WarMedia.this.viewIsActive) {
                                WarMedia.this.resumeEvent();
                                if (WarMedia.this.cachedSurfaceHolder != null) {
                                    WarMedia.this.cachedSurfaceHolder.setKeepScreenOn(true);
                                }
                            }
                            if (!WarMedia.this.paused) {
                                WarMedia.this.PauseMoviePlayer(false);
                            }
                        }
                    }
                } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    WarMedia.this.NetworkChange();
                } else if (WarMedia.this.DoLog) {
                    System.out.println("Received " + action.toString());
                }
            }
        };
        this.bMoviePlayerPaused = false;
        this.currentMovieMS = 0;
        this.currentMovieFilename = "";
        this.currentMovieOffset = 0;
        this.currentMovieLength = 0;
        this.currentMovieVolume = 0.5f;
        this.myVib = null;
        this.vibrateEffects = new long[][]{new long[]{0, 100, 100, 100, 100}, new long[]{0, 100, 50, 75, 100, 50, 100}, new long[]{0, 25, 50, 100, 50, 25, 100}, new long[]{0, 25, 50, 25, 100, 100, 100}, new long[]{0, 50, 50, 50, 50, 25, 100}};
        this.mgr = null;
        this.memInfo = null;
        this.myPid = null;
        this.MovieIsSkippable = false;
        this.lastMovieStop = 0;
        this.movieWindowSurface = null;
        this.movieWindowHolder = null;
        this.movieTextHolder = null;
        this.movieTextSurface = null;
        this.movieViewIsActive = false;
        this.movieViewCreated = false;
        this.customMovieHolder = null;
        this.customMovieSurface = null;
        this.movieViewWidth = 0;
        this.movieViewHeight = 0;
        this.movieViewX = 0;
        this.movieViewY = 0;
        this.movieLooping = 0;
        this.movieView = null;
        this.movieViewText = null;
        this.DeviceLocale = "";
        this.DeviceCountry = "";
        this.locale = null;
        this.IsInValidation = false;
        this.movieTextScale = 32;
        this.movieTextView = null;
        this.MovieTextDisplayed = false;
        this.llSplashView = null;
    }

    public static class XAPKFile {
        public final long mFileSize;
        public final int mFileVersion;
        public final boolean mIsMain;

        public XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            this.mIsMain = isMain;
            this.mFileVersion = fileVersion;
            this.mFileSize = fileSize;
        }
    }

    class PoolInfo {
        float duration;
        String filename;
        float lv;
        float rv;
        int soundID;

        PoolInfo() {
        }
    }

    /* access modifiers changed from: package-private */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void GetMaxDisplaySize() {
        if (Build.VERSION.SDK_INT >= 11) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            System.out.println("width=" + size.x + " height=" + size.y);
            if (this.maxDisplayWidth < size.x) {
                this.maxDisplayWidth = size.x;
                this.maxDisplayHeight = size.y;
            }
            Display.Mode[] modes = display.getSupportedModes();
            for (int i = 0; i < modes.length; i++) {
                Display.Mode mode = modes[i];
                if (this.maxDisplayWidth < mode.getPhysicalWidth()) {
                    this.maxDisplayWidth = mode.getPhysicalWidth();
                    this.maxDisplayHeight = mode.getPhysicalHeight();
                }
                System.out.println("mode " + i + " width=" + mode.getPhysicalWidth() + " height=" + mode.getPhysicalHeight());
            }
        }
    }

    @SuppressLint({"InvalidWakeLockTag", "MissingSuperCall"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        boolean z;
        if (this.isNativeApp) {
            super.onCreate(savedInstanceState);
        }
        if (this.DoLog) {
            System.out.println("****WarMedia onCreate");
        }
        ClearSystemNotification();
        GetGameBaseDirectory();
        this.GetGLExtensions = this.UseFTPDownload;
        this.metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.metrics);
        System.out.println("Display Metrics Info:\n\tdensity:\t\t" + this.metrics.density + "\n\tdensityDPI:\t\t" + this.metrics.densityDpi + "\n\tscaledDensity:\t\t" + this.metrics.scaledDensity + "\n\twidthDPI:\t\t" + this.metrics.xdpi + "\n\theightDPI:\t\t" + this.metrics.ydpi + "\n\twidthPixels:\t\t" + this.metrics.widthPixels + "\n\theightPixels:\t\t" + this.metrics.heightPixels + "\n\tscreenlayout=" + getResources().getConfiguration().screenLayout);
        this.maxDisplayWidth = this.metrics.widthPixels;
        this.maxDisplayHeight = this.metrics.heightPixels;
        this.baseDisplayWidth = this.metrics.widthPixels;
        this.baseDisplayHeight = this.metrics.heightPixels;
        if (Build.MODEL.startsWith("ADT")) {
            this.IsAndroidTV = true;
        }
        if (Build.MANUFACTURER.startsWith("NVIDIA") && Build.MODEL.startsWith("SHIELD Android TV")) {
            if (this.checkForMaxDisplaySize) {
                GetMaxDisplaySize();
            }
            this.isShieldTV = true;
        }
        NvUtil.getInstance().setActivity(this);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT", this.baseDirectory);
        NvUtil.getInstance().setAppLocalValue("STORAGE_ROOT_BASE", this.baseDirectoryRoot);
        if (getResources().getConfiguration().touchscreen != 1) {
            z = true;
        } else {
            z = false;
        }
        this.hasTouchScreen = z;
        System.out.println("hastouchscreen " + this.hasTouchScreen + " touchscreen " + getResources().getConfiguration().touchscreen);
        if (IsPortrait()) {
            if (Build.VERSION.SDK_INT >= 9) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else if (Build.VERSION.SDK_INT >= 9) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        this.activity = this;
        GetRealLocale();
        this.myWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(268435482, "WarEngine");
        this.myWakeLock.setReferenceCounted(false);
        this.isPhone = IsPhone();
        this.screenWidthInches = ((float) this.metrics.widthPixels) / this.metrics.xdpi;
        if (this.isPhone) {
            if (this.screenWidthInches < 3.5f) {
                this.screenWidthInches = 3.5f;
            }
            if (this.screenWidthInches > 6.0f) {
                this.screenWidthInches = 6.0f;
            }
        } else {
            if (this.screenWidthInches < 6.0f) {
                this.screenWidthInches = 6.0f;
            }
            if (this.screenWidthInches > 10.0f) {
                this.screenWidthInches = 10.0f;
            }
        }
        setVolumeControlStream(3);
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.audioMax = this.audioManager.getStreamMaxVolume(3);
        this.audioVolume = this.audioManager.getStreamVolume(3);
        System.out.println("availableProcessors " + Runtime.getRuntime().availableProcessors() + " cpu " + getNumberOfProcessors());
        GetMemoryInfo(true);
        if (this.UseSubtitles) {
            SetPaint(-16711936, 16);
        }
        this.localIp = getLocalIpAddress();
        if (!this.isNativeApp) {
            super.onCreate(savedInstanceState);
        }
        if (!CustomLoadFunction()) {
            localHasGameData();
        }
        NetworkChange();
        try {
            initTouchSense(this);
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public boolean isTV() {
        if (((UiModeManager) getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() == 4) {
            return true;
        }
        return false;
    }

    public boolean isWiFiAvailable() {
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        return this.mWifiManager != null && this.mWifiManager.isWifiEnabled();
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return false;
        }
        return activeNetworkInfo.isConnected();
    }

    /* access modifiers changed from: package-private */
    public void ShowGPDownloadError() {
        runOnUiThread(new Runnable() {
            public void run() {
                /*WarMedia.this.exitDialog = new AlertDialog.Builder(this).setTitle("Unknown download error. Please reinstall from Google Play").setPositiveButton("Quit Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarMedia.this.finish();
                    }
                }).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarMedia.this.runOnUiThread(new Runnable() {
                            public void run() {
                                WarMedia.this.localHasGameData();
                            }
                        });
                    }
                }).setCancelable(false).show();*/
                WarMedia.this.exitDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void ShowDownloadNetworkError() {
        runOnUiThread(new Runnable() {
            public void run() {

            }
        });
    }

    public int downloadFTPFile(String filename, int filesize, boolean check) {
        return 0;
    }

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public boolean localHasGameData() {
        if (!this.FinalRelease) {
            System.out.println("localHasGameData");
        }
        if (this.UseFTPDownload) {
            new Thread(new Runnable() {
                public void run() {
                    WarMedia.this.wardown.ShowEULA();
                }
            }).start();
            return false;
        }
        if (this.xAPKS == null || expansionFilesDelivered()) {
            AfterDownloadFunction();
        } else {
            Intent notifierIntent = new Intent(this, getClass());
            notifierIntent.setFlags(335544320);
        }
        return true;
    }

    public String GetGameBaseDirectory() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                File f = getExternalFilesDir((String) null);
                String base = f.getAbsolutePath();
                this.baseDirectoryRoot = base.substring(0, base.indexOf("/Android"));
                return f.getAbsolutePath() + "/";
            } catch (Exception e) {
            }
        }
        ShowSDErrorDialog();
        return "";
    }

    /* access modifiers changed from: package-private */
    public void ShowSDErrorDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                /*WarMedia.this.exitDialog = new AlertDialog.Builder(this).setTitle("Cannot find storage. Is SDCard mounted?").setPositiveButton("Exit Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarMedia.this.finish();
                    }
                }).setCancelable(false).show();
                WarMedia.this.exitDialog.setCanceledOnTouchOutside(true);*/
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean expansionFilesDelivered() {
        for (XAPKFile xf : this.xAPKS) {
        }
        return true;
    }

    private void initializeDownloadUI() {
        if (!this.FinalRelease) {
            System.out.println("initializeDownloadUI");
        }

        this.downloadView = new SurfaceView(this);
        this.downloadHolder = this.downloadView.getHolder();
        this.downloadHolder.addCallback(new SurfaceHolder.Callback() {
            public final void surfaceCreated(SurfaceHolder holder) {
                WarMedia.this.downloadViewCreated = true;
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (!WarMedia.this.FinalRelease) {
                    System.out.println("surfaceChanged called - subViewSplash");
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (!WarMedia.this.FinalRelease) {
                    System.out.println("surfaceDestroyed called - subViewSplash");
                }
            }
        });
        this.downloadHolder.setType(0);
        setContentView(this.downloadView);
        LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(-2, -2);
        myParams.gravity = 17;

        this.mPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    WarMedia.this.setButtonPausedState(!WarMedia.this.mStatePaused);
                } catch (Exception e) {
                    System.out.println("mPauseButton error " + e.getMessage());
                }
            }
        });
        this.mWiFiSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.ResumeEventDone && this.isUserPresent && this.viewIsActive && !this.IsScreenPaused && !this.paused) {
            if (this.GameIsFocused && !hasFocus) {
                pauseEvent();
            } else if (!this.GameIsFocused && hasFocus) {
                resumeEvent();
            }
            this.GameIsFocused = hasFocus;
        }
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.DoLog) {
            System.out.println("Listener - onConfigurationChanged orient " + newConfig.orientation + " " + newConfig);
        }
        if (this.IsShowingKeyboard && newConfig.keyboard == 2 && 1 == newConfig.hardKeyboardHidden) {
            this.IsShowingKeyboard = false;
            imeClosed();
        }
        super.onConfigurationChanged(newConfig);
    }

    public void onLowMemory() {
        lowMemoryEvent();
    }

    /* access modifiers changed from: protected */
    public void NetworkChange() {
        int curNetwork = isWiFiAvailable() ? 2 : isNetworkAvailable() ? 1 : 0;
        if (curNetwork != this.lastNetworkAvailable) {
            NativeNotifyNetworkChange(curNetwork);
            this.lastNetworkAvailable = curNetwork;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.SCREEN_ON");
        if (this.DoLog) {
            System.out.println("registerReceiver");
        }
        registerReceiver(this.mReceiver, filter);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.DoLog) {
            System.out.println("unregisterReceiver");
        }
        unregisterReceiver(this.mReceiver);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (this.DoLog) {
            System.out.println("WarMedia**** onResume viewIsActive " + this.viewIsActive + " isUserPresent " + this.isUserPresent);
        }
        super.onResume();

        while (!isExternalStorageReadable()) {
            System.out.println("Resuming when Media is not mounted, waiting for sdcard mount");
            mSleep(100);
        }
        if (this.isUserPresent) {
            if (this.viewIsActive && this.ResumeEventDone) {
                resumeEvent();
                if (this.cachedSurfaceHolder != null) {
                    this.cachedSurfaceHolder.setKeepScreenOn(true);
                }
            }
            this.IsScreenPaused = false;
            PauseMoviePlayer(false);
        }
        this.paused = false;
    }

    /* access modifiers changed from: package-private */
    public void PauseMoviePlayer(boolean bPause) {
        if (this.moviePlayer != null) {
            final boolean bfPause = bPause;
            new Thread(new Runnable() {
                public void run() {
                    WarMedia.this.PauseMoviePlayerThread(bfPause);
                }
            }).start();
        }
    }

    /* access modifiers changed from: package-private */
    public void PauseMoviePlayerThread(boolean bPause) {
        if (!this.skipMovies) {
            if (bPause) {
                try {
                    if (this.moviePlayer == null) {
                        return;
                    }
                    if (this.moviePlayer.isPlaying()) {
                        try {
                            this.currentMovieMS = this.moviePlayer.getCurrentPosition();
                            this.moviePlayer.pause();
                            this.bMoviePlayerPaused = true;
                            System.out.println("moviePlayer paused at " + this.currentMovieMS);
                        } catch (Exception ex) {
                            System.out.println("moviePlayer pause failed " + ex.getMessage());
                            try {
                                if (this.moviePlayer != null) {
                                    this.moviePlayer.release();
                                }
                            } catch (Exception e) {
                            }
                            this.moviePlayer = null;
                            ClearVidView();
                            this.bMoviePlayerPaused = false;
                        }
                    } else {
                        System.out.println("moviePlayer not playing");
                        this.moviePlayer.release();
                        this.moviePlayer = null;
                        this.bMoviePlayerPaused = false;
                    }
                } catch (IllegalStateException e2) {
                    System.out.println("PauseMoviePlayerThread err " + e2.getMessage());
                    ClearVidView();
                    this.moviePlayer = null;
                    this.bIsPlayingMovie = 0;
                    this.bMoviePlayerPaused = false;
                }
            } else {
                System.out.println("moviePlayer resume bMoviePlayerPaused " + this.bMoviePlayerPaused + " moviePlayer " + this.moviePlayer);
                if (this.bMoviePlayerPaused && this.moviePlayer == null) {
                    if (this.currentMovieLength > 0) {
                        PlayMovieInFile(this.currentMovieFilename, 1.0f, this.currentMovieOffset, this.currentMovieLength);
                    } else {
                        PlayMovie(this.currentMovieFilename, 1.0f);
                    }
                    this.bMoviePlayerPaused = false;
                } else if (this.bMoviePlayerPaused && this.moviePlayer != null) {
                    int count = 0;
                    while (!IsMovieViewActive()) {
                        if (!this.FinalRelease) {
                            System.out.println("moviePlayer waiting for vidViewIsActive");
                        }
                        mSleep(100);
                        count++;
                        if (count > 5) {
                            break;
                        }
                    }
                    if (count <= 5) {
                        try {
                            System.out.println("moviePlayer paused false");
                            if (this.currentMovieLength > 0) {
                                PlayMovieInFile(this.currentMovieFilename, 1.0f, this.currentMovieOffset, this.currentMovieLength, this.movieWindowHolder);
                            } else {
                                PlayMovie(this.currentMovieFilename, 1.0f);
                            }
                        } catch (Exception ex2) {
                            System.out.println("moviePlayer resume failed " + ex2.getMessage());
                            this.moviePlayer = null;
                            ClearVidView();
                        }
                    } else {
                        this.moviePlayer.release();
                        this.moviePlayer = null;
                        ClearVidView();
                    }
                    this.bMoviePlayerPaused = false;
                }
            }
        }
    }

    public void VibratePhone(int numMilliseconds) {
        if (!this.FinalRelease) {
            System.out.println("VibratePhone " + numMilliseconds);
        }
        if (this.myVib == null) {
            this.myVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (this.myVib != null) {
            this.myVib.vibrate((long) numMilliseconds);
        }
    }

    public void VibratePhoneEffect(int effect) {
        if (!this.FinalRelease) {
            System.out.println("VibratePhoneEffect " + effect);
        }
        if (this.myVib == null) {
            this.myVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (this.myVib != null) {
            this.myVib.vibrate(this.vibrateEffects[effect], -1);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.DoLog) {
            System.out.println("Listener -  onPause");
        }
        if (this.cachedSurfaceHolder != null) {
            this.cachedSurfaceHolder.setKeepScreenOn(false);
        }

        super.onPause();
        PauseMoviePlayer(true);
        GetMemoryInfo(true);
        this.IsScreenPaused = true;
        this.paused = true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.MovieIsSkippable) {
            StopMovie();
        }
        if (this.DisplayMovieTextOnTap && this.movieTextViewIsActive) {
            this.movieTextDisplayNow = true;
            DrawMovieText();
        }
        if (this.IsShowingBackMessage != 2) {
            return super.onTouchEvent(event);
        }
        if (this.DoLog) {
            System.out.println("onTouchEvent exitDialog " + this.exitDialog);
        }
        if (this.exitDialog != null) {
            this.exitDialog.dismiss();
        }
        this.IsShowingBackMessage = 0;
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.AllowLongPressForExit || keyCode != 4 || event.isAltPressed() || !event.isLongPress()) {
            return super.onKeyDown(keyCode, event);
        }
        this.IsShowingBackMessage = 1;
        if (this.DoLog) {
            System.out.println("ShowExitDialog KeyDown");
        }
        ShowExitDialog();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void ShowExitDialog() {
        this.handler.post(new Runnable() {
            public void run() {

                WarMedia.this.exitDialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                while (true) {
                    if (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("Got SocketException " + ex);
        }
        return null;
    }

    public String GetLocalIp() {
        return this.localIp;
    }

    /* access modifiers changed from: protected */
    public void onStart() {

        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (this.DoLog) {
            System.out.println("Listener - onStop");
        }

        super.onStop();
    }

    public void onRestart() {
        if (this.DoLog) {
            System.out.println("Listener - onRestart");
        }
        super.onRestart();
    }

    public void onDestroy() {
        if (this.DoLog) {
            System.out.println("Listener - onDestroy isFinishing " + isFinishing());
        }
        Process.killProcess(Process.myPid());
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    public void finish() {
        onDestroy();
        super.finish();
    }

    public void ExitGame() {
        onDestroy();
        finish();
        Process.killProcess(Process.myPid());
    }

    /* access modifiers changed from: package-private */
    public int GetSoundsIndex(String filename) {
        for (int i = 0; i < this.sounds.size(); i++) {
            PoolInfo pi = this.sounds.get(i);
            if (pi.filename.equals(filename)) {
                return pi.soundID;
            }
        }
        return -1;
    }

    public int GetMemoryInfo(boolean allProcesses) {
        if (this.mgr == null) {
            this.mgr = (ActivityManager) super.getSystemService(Context.ACTIVITY_SERVICE);
            this.memInfo = new ActivityManager.MemoryInfo();
        }
        this.mgr.getMemoryInfo(this.memInfo);
        this.memoryThreshold = (int) (this.memInfo.threshold );
        this.availableMemory = (int) ((this.memInfo.availMem ));
        if (Build.VERSION.SDK_INT >= 16) {
            this.totalMemory = (int) ((this.memInfo.totalMem));
        } else {
            this.totalMemory = 256;
        }
        if (allProcesses) {
            this.mgr.getRunningAppProcesses();
            List<ActivityManager.RunningAppProcessInfo> l = this.mgr.getRunningAppProcesses();
            int[] pids = new int[l.size()];
            for (int i = 0; i < l.size(); i++) {
                pids[i] = l.get(i).pid;
            }
        } else if (this.myPid != null) {
            this.mgr.getProcessMemoryInfo(this.myPid);
        }
        return (int) ((this.memInfo.availMem));
    }

    public void onSeekComplete(MediaPlayer mp) {
        if (this.soundLog) {
            System.out.println("onSeekComplete");
        }
        if (mp == this.moviePlayer) {
            SetVideoAspect(mp);
            mp.start();
            mp.setOnSeekCompleteListener((MediaPlayer.OnSeekCompleteListener) null);
            this.bIsPlayingMovie = 2;
        }
    }

    /* access modifiers changed from: package-private */
    public void SetVideoAspect(MediaPlayer mp) {
        FrameLayout.LayoutParams layoutParams;
        if (this.customMovieSurface == null) {
            try {
                int surfaceView_Width = this.vidView.getWidth();
                int surfaceView_Height = this.vidView.getHeight();
                float video_Width = (float) mp.getVideoWidth();
                float video_Height = (float) mp.getVideoHeight();
                if (video_Width <= 1.0f || video_Height <= 10.0f) {
                    System.out.println("videosize error (" + video_Width + "," + video_Height + ")");
                    return;
                }
                float ratio_width = ((float) surfaceView_Width) / video_Width;
                float ratio_height = ((float) surfaceView_Height) / video_Height;
                float aspectratio = video_Width / video_Height;
                if (this.customMovieSurface == null) {
                    layoutParams = (FrameLayout.LayoutParams) this.vidView.getLayoutParams();
                } else {
                    layoutParams = (FrameLayout.LayoutParams) this.customMovieSurface.getLayoutParams();
                }
                if (this.ForceSize) {
                    layoutParams.width = this.movieViewWidth;
                    layoutParams.height = this.movieViewHeight;
                } else if (!this.KeepAspectRatio) {
                    layoutParams.width = surfaceView_Width;
                    layoutParams.height = surfaceView_Height;
                } else if (ratio_width > ratio_height) {
                    layoutParams.width = (int) (((float) surfaceView_Height) * aspectratio);
                    layoutParams.height = surfaceView_Height;
                } else {
                    layoutParams.width = surfaceView_Width;
                    layoutParams.height = (int) (((float) surfaceView_Width) / aspectratio);
                }
                layoutParams.gravity = 17;
                if (this.customMovieSurface == null) {
                    this.vidView.setLayoutParams(layoutParams);
                } else {
                    this.customMovieSurface.setLayoutParams(layoutParams);
                }
            } catch (IllegalStateException e) {
            } catch (Exception e2) {
            }
        }
    }

    public void onPrepared(MediaPlayer mp) {
        if (!mp.equals(this.moviePlayer)) {
            return;
        }
        if (this.bIsPlayingMovie != 1) {
            System.out.println("trying to start a requested to stop movie");
            try {
                mp.release();
            } catch (IllegalStateException e) {
            }
            this.moviePlayer = null;
            this.bIsPlayingMovie = 0;
            ClearVidView();
            return;
        }
        this.moviePlayer.setVolume(this.currentMovieVolume, this.currentMovieVolume);
        if (this.movieLooping != 0) {
            this.moviePlayer.setLooping(true);
        }
        if (this.currentMovieMS > 0) {
            mp.setOnSeekCompleteListener(this);
            mp.seekTo(this.currentMovieMS);
        } else {
            SetVideoAspect(mp);
            try {
                mp.start();
                this.bIsPlayingMovie = 2;
            } catch (IllegalStateException e2) {
                System.out.println("onPrepared IllegalStateException " + e2.getMessage());
                this.moviePlayer = null;
                this.bIsPlayingMovie = 0;
                ClearVidView();
            }
        }
        this.currentMovieMS = 0;
    }

    public void onCompletion(MediaPlayer mp) {
        if (mp.equals(this.moviePlayer)) {
            System.out.println("onCompletion completed moviePlayer");
            this.moviePlayer = null;
            try {
                mp.release();
            } catch (IllegalStateException e) {
            }
            ClearVidView();
            if (this.bIsPlayingMovie == 2) {
                this.bIsPlayingMovie = 0;
            }
            this.lastMovieStop = System.currentTimeMillis() + 1000;
            this.MovieTextDisplayed = false;
            this.movieLooping = 0;
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("onError what " + what + " exra " + extra);
        return true;
    }

    public boolean IsPhone() {
        return (getResources().getConfiguration().screenLayout & 15) < 3;
    }

    public void ClearSystemNotification() {
        runOnUiThread(new Runnable() {
            public void run() {
                ((NotificationManager) WarMedia.this.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void SetVidView() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (WarMedia.this.customMovieSurface != null) {
                    WarMedia.this.customMovieSurface.setVisibility(View.VISIBLE);
                } else if (!WarMedia.this.noVidSurface) {
                    WarMedia.this.vidView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void ClearVidView() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (WarMedia.this.customMovieSurface != null) {
                    WarMedia.this.customMovieSurface.setVisibility(View.GONE);
                    WarMedia.this.movieView.setVisibility(View.GONE);
                    WarMedia.this.customMovieSurface = null;
                } else if (!WarMedia.this.noVidSurface) {
                    WarMedia.this.vidView.setVisibility(android.view.View.GONE);
                }
            }
        });
    }

    public boolean IsMovieViewActive() {
        if (this.customMovieSurface == null) {
            return this.vidViewIsActive;
        }
        return this.movieViewIsActive;
    }

    public void MovieSetSkippable(boolean skippable) {
        this.MovieIsSkippable = skippable;
    }

    public void StopMovie() {
        this.movieLooping = 0;
        this.MovieIsSkippable = false;
        this.MovieTextDisplayed = false;
        if (this.bIsPlayingMovie != 2) {
            if (this.DoLog) {
                System.out.println("MOVIE IS NOT PLAYING bIsPlayingMovie " + this.bIsPlayingMovie);
            }
            this.bIsPlayingMovie = 0;
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
                this.moviePlayer = null;
            }
        } catch (Exception e) {
            this.moviePlayer = null;
        }
        this.lastMovieStop = System.currentTimeMillis() + 1000;
        ClearVidView();
        this.bIsPlayingMovie = 0;
        this.bMoviePlayerPaused = false;
    }

    /* access modifiers changed from: package-private */
    public SurfaceHolder CreatTextSurface(SurfaceView surface) {
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                boolean unused = WarMedia.this.movieTextViewIsActive = true;
                Canvas canvas = WarMedia.this.movieTextHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                WarMedia.this.movieTextHolder.unlockCanvasAndPost(canvas);
                if (!WarMedia.this.movieViewCreated) {
                    System.out.println("movieTextSurface surfaceCreated firsttime");
                    boolean unused2 = WarMedia.this.movieTextViewCreated = true;
                    return;
                }
                System.out.println("movieTextSurface surfaceCreated");
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("movieTextSurface surfaceChanged width " + width + " height " + height);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("movieTextSurface surfaceDestroyed");
                boolean unused = WarMedia.this.movieTextViewIsActive = false;
            }
        });
        System.out.println("movieWindowHolder setType");
        holder.setType(0);
        surface.setZOrderOnTop(true);
        return holder;
    }

    /* access modifiers changed from: package-private */
    public TextView CreateTextView() {
        TextView tv = new TextView(this.activity);
        tv.setLayoutParams(new WindowManager.LayoutParams(-2, -2));
        tv.setTextSize(48.0f);
        tv.setText("Tap to Skip");
        return tv;
    }

    /* access modifiers changed from: package-private */
    public LinearLayout CreateMovieView(int x, int y, int width, int height) {
        LinearLayout ll = new LinearLayout(this.activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.row1 = new LinearLayout(this.activity);
        this.row1.setOrientation(LinearLayout.VERTICAL);
        this.row1.setLayoutParams(new LinearLayout.LayoutParams(-2, y));
        ll.addView(this.row1);
        this.row2 = new LinearLayout(this.activity);
        this.row2.setOrientation(LinearLayout.HORIZONTAL);
        this.row2.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
        this.col1 = new LinearLayout(this.activity);
        this.col1.setOrientation(LinearLayout.HORIZONTAL);
        this.col1.setLayoutParams(new LinearLayout.LayoutParams(x, -2));
        this.col2 = new LinearLayout(this.activity);
        this.col2.setOrientation(LinearLayout.HORIZONTAL);
        this.col2.setLayoutParams(new LinearLayout.LayoutParams(width, -2));
        this.movieWindowSurface = new SurfaceView(this.activity);
        this.col2.addView(this.movieWindowSurface);
        this.col3 = new LinearLayout(this.activity);
        this.col3.setOrientation(LinearLayout.HORIZONTAL);
        this.col3.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.row2.addView(this.col1);
        this.row2.addView(this.col2);
        this.row2.addView(this.col3);
        ll.addView(this.row2);
        this.row3 = new LinearLayout(this.activity);
        this.row3.setOrientation(LinearLayout.VERTICAL);
        this.row3.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        ll.addView(this.row3);
        return ll;
    }

    /* access modifiers changed from: package-private */
    public void ChangeMovieView(int x, int y, int width, int height) {
        this.row1.setLayoutParams(new LinearLayout.LayoutParams(-2, y));
        this.row2.setLayoutParams(new LinearLayout.LayoutParams(-2, height));
        this.col1.setLayoutParams(new LinearLayout.LayoutParams(x, -2));
        this.col2.setLayoutParams(new LinearLayout.LayoutParams(width, -2));
    }

    public void PlayMovieInWindow(String inFilename, int x, int y, int width, int height, float inVolume, int inOffset, int inLength, int looping, boolean forceSize) {
        System.out.println("PlayMovieInWindow filename " + inFilename + " movieWindowSurface " + this.movieWindowSurface + " inOffset " + inOffset + " inLength " + inLength);
        this.MovieIsSkippable = false;
        this.ForceSize = forceSize;
        System.out.println("PlayMovieInWindow ForceSize " + this.ForceSize + " width " + width + " height " + height);
        if (!this.checkForMaxDisplaySize || this.baseDisplayHeight >= (height * 2) / 3) {
            this.movieViewWidth = width;
            this.movieViewHeight = height;
            this.movieViewX = x;
            this.movieViewY = y;
        } else {
            this.movieViewWidth = this.baseDisplayWidth;
            this.movieViewHeight = this.baseDisplayHeight;
            this.movieViewX = x;
            this.movieViewY = y;
        }
        this.movieLooping = looping;
        final String filename = inFilename;
        final float volume = inVolume;
        final int offset = inOffset;
        final int length = inLength;
        this.bIsPlayingMovie = 1;
        runOnUiThread(new Runnable() {
            public void run() {
                WarMedia.this.movieView = WarMedia.this.CreateMovieView(WarMedia.this.movieViewX, WarMedia.this.movieViewY, WarMedia.this.movieViewWidth, WarMedia.this.movieViewHeight);
                WarMedia.this.movieWindowHolder = WarMedia.this.movieWindowSurface.getHolder();
                WarMedia.this.movieWindowHolder.addCallback(new SurfaceHolder.Callback() {
                    public void surfaceCreated(SurfaceHolder holder) {
                        WarMedia.this.movieViewIsActive = true;
                        if (!WarMedia.this.movieViewCreated) {
                            System.out.println("movieViewCreated surfaceCreated firsttime");
                            WarMedia.this.movieViewCreated = true;
                            return;
                        }
                        System.out.println("movieViewCreated surfaceCreated");
                    }

                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        System.out.println("movieView surfaceChanged width " + width + " height " + height);
                        WarMedia.this.movieViewWidth = width;
                        WarMedia.this.movieViewHeight = height;
                    }

                    public void surfaceDestroyed(SurfaceHolder holder) {
                        System.out.println("movieViewCreated surfaceDestroyed");
                        if (WarMedia.this.movieTextSurface != null) {
                            WarMedia.this.movieTextSurface.setVisibility(View.GONE);
                        }
                        WarMedia.this.movieViewIsActive = false;
                        boolean unused = WarMedia.this.movieIsStopping = false;
                        WarMedia.this.movieSubtitleText = "";
                        String unused2 = WarMedia.this.movieText = "";
                    }
                });
                System.out.println("movieWindowHolder setType");
                WarMedia.this.movieWindowHolder.setType(3);
                WarMedia.this.movieWindowSurface.setZOrderOnTop(true);
                WarMedia.this.movieWindowHolder.setFormat(-3);
                System.out.println("movieView (" + WarMedia.this.movieViewX + "," + WarMedia.this.movieViewY + ") (" + WarMedia.this.movieViewWidth + "," + WarMedia.this.movieViewHeight + ")");
                LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(-2, -2);
                myParams.gravity = 17;
                WarMedia.this.addContentView(WarMedia.this.movieView, myParams);
                WarMedia.this.movieTextSurface = new SurfaceView(WarMedia.this.activity);
                WarMedia.this.movieTextHolder = WarMedia.this.CreatTextSurface(WarMedia.this.movieTextSurface);
                WarMedia.this.movieTextHolder.setFormat(-3);
                WarMedia.this.addContentView(WarMedia.this.movieTextSurface, new WindowManager.LayoutParams(-1, -1));
                System.out.println("PlayMovieInFile for inwindow");
                WarMedia.this.customMovieSurface = WarMedia.this.movieWindowSurface;
                new Thread(new Runnable() {
                    public void run() {
                        int count = 0;
                        while (true) {
                            if (WarMedia.this.movieViewIsActive) {
                                break;
                            }
                            int count2 = count + 1;
                            if (count >= 10) {
                                break;
                            }
                            System.out.println("wait for create");
                            WarMedia.this.mSleep(1000);
                            count = count2;
                        }
                        if (WarMedia.this.movieViewIsActive) {
                            if (length > 0) {
                                WarMedia.this.PlayMovieInFile(filename, volume, offset, length, WarMedia.this.movieWindowHolder);
                            } else {
                                WarMedia.this.PlayMovie(filename, volume, WarMedia.this.movieWindowHolder);
                            }
                        }
                    }
                }).start();
            }
        });
    }

    public void PlayMovieInFile(String filename, float volume, int offset, int length) {
        PlayMovieInFile(filename, volume, offset, length, this.vidHolder);
    }

    public void PlayMovieInFile(String filename, float volume, int offset, int length, SurfaceHolder myVidHolder) {
        String tempFilename;
        if (!(this.customMovieSurface == null || this.customMovieSurface == this.movieWindowSurface)) {
            this.customMovieSurface = null;
        }
        this.customMovieHolder = myVidHolder;
        this.bIsPlayingMovie = 1;
        this.bMoviePlayerPaused = false;
        this.currentMovieFilename = filename;
        this.currentMovieOffset = offset;
        this.currentMovieLength = length;
        this.currentMovieVolume = volume;
        if (this.DoLog) {
            System.out.println("PlayMovieInFile " + filename + " offset " + offset + " length " + length);
        }
        if (filename.startsWith("/")) {
            tempFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + filename;
        } else {
            tempFilename = this.baseDirectory + "/" + filename.replace("\\", "/");
        }
        if (!new File(tempFilename).exists()) {
            tempFilename = filename;
        }
        final String newFile = tempFilename;
        if (this.DoLog) {
            System.out.println("PlayMovieInFile " + newFile + " offset " + offset + " length " + length);
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
            }
        } catch (Exception e) {
        }
        while (this.lastMovieStop > System.currentTimeMillis()) {
            mSleep(100);
        }
        SetVidView();
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    WarMedia.this.moviePlayer = new MediaPlayer();
                    WarMedia.this.moviePlayer.setDataSource(new RandomAccessFile(new File(newFile), "r").getFD(), (long) WarMedia.this.currentMovieOffset, (long) WarMedia.this.currentMovieLength);
                    int count = 0;
                    while (!WarMedia.this.IsMovieViewActive()) {
                        System.out.println("Waiting for video surface PlayMovieInFile");
                        WarMedia.this.mSleep(100);
                        count++;
                        if (count > 5) {
                            break;
                        }
                    }
                    if (count <= 5) {
                        return;
                    }
                    System.out.println("creation failed count " + count);
                    WarMedia.this.ClearVidView();
                    WarMedia.this.moviePlayer.release();
                    WarMedia.this.moviePlayer = null;
                    WarMedia.this.bIsPlayingMovie = 0;
                } catch (Exception ex) {
                    System.out.println("creation failed error  " + ex.getMessage());
                    WarMedia.this.moviePlayer = null;
                    WarMedia.this.bIsPlayingMovie = 0;
                }
            }
        });
    }

    public void PlayMovie(String filename, float Volume) {
        PlayMovie(filename, Volume, this.vidHolder);
    }

    public void PlayMovie(String filename, float Volume, SurfaceHolder myVidHolder) {
        String apkFilename;
        this.customMovieHolder = myVidHolder;
        this.bIsPlayingMovie = 1;
        this.currentMovieFilename = filename;
        this.currentMovieOffset = 0;
        this.currentMovieLength = 0;
        this.currentMovieVolume = Volume;
        if (this.DoLog) {
            System.out.println("PlayMovie " + filename);
        }
        final String newFile = this.baseDirectory + "/" + filename.replace("\\", "/") + (this.AddMovieExtension ? ".m4v" : "");
        if (this.DoLog) {
            System.out.println("PlayMovie newFile " + newFile);
        }
        AssetFileDescriptor afd = null;
        if (!new File(newFile).exists()) {
            try {
                if (this.AddMovieExtension) {
                    apkFilename = filename.replace("\\", "/") + ".m4v.mp3";
                } else {
                    apkFilename = filename.replace("\\", "/") + ".mp3";
                }
                System.out.println("Looking for  " + apkFilename);
                afd = getApplicationContext().getAssets().openFd(apkFilename);
            } catch (Exception e) {
                afd = null;
            }
            if (afd == null) {
                System.out.println("File not found " + filename);
                this.bIsPlayingMovie = 0;
                return;
            }
        }
        try {
            if (this.moviePlayer != null) {
                this.moviePlayer.release();
            }
        } catch (Exception e2) {
        }
        while (this.lastMovieStop > System.currentTimeMillis()) {
            mSleep(100);
        }
        SetVidView();
        final AssetFileDescriptor afdFinal = afd;
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    WarMedia.this.moviePlayer = new MediaPlayer();
                    if (afdFinal != null) {
                        WarMedia.this.moviePlayer.setDataSource(afdFinal.getFileDescriptor(), afdFinal.getStartOffset(), afdFinal.getLength());
                    } else {
                        WarMedia.this.moviePlayer.setDataSource(newFile);
                    }
                    int count = 0;
                    while (!WarMedia.this.IsMovieViewActive()) {
                        System.out.println("Waiting for video surface PlayMovie");
                        int count2 = count + 1;
                        if (count > 5) {
                            try {
                                WarMedia.this.moviePlayer.release();
                            } catch (IllegalStateException e) {
                            }
                            WarMedia.this.moviePlayer = null;
                            WarMedia.this.ClearVidView();
                        }
                        WarMedia.this.mSleep(100);
                        count = count2;
                    }
                    WarMedia.this.moviePlayer.prepareAsync();
                } catch (Exception e2) {
                }
            }
        });
    }

    public int IsMoviePlaying() {
        if (this.bMoviePlayerPaused) {
            return 2;
        }
        if (this.bIsPlayingMovie == 1) {
            return this.bIsPlayingMovie;
        }
        if (this.bIsPlayingMovie != 2 || this.moviePlayer == null) {
            return 0;
        }
        try {
            if (this.moviePlayer.isPlaying()) {
                return this.bIsPlayingMovie;
            }
            return 0;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public String GetConfigSetting(String key) {
        String value = getPreferences(0).getString(key, "");
        if (this.DoLog) {
            System.out.println("GetConfigSetting " + key + " value " + value);
        }
        return value;
    }

    public void SetConfigSetting(String key, String value) {
        SharedPreferences.Editor e = getPreferences(0).edit();
        e.putString(key, value);
        e.commit();
        if (this.DoLog) {
            System.out.println("SetConfigSetting " + key + " value " + value);
        }
    }

    public String OBFU_GetDeviceID() {
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        return new UUID((long) ("" + Settings.Secure.getString(getContentResolver(), "android_id")).hashCode(), (((long) ("" + tm.getDeviceId()).hashCode()) << 32) | ((long) ("" + tm.getSimSerialNumber()).hashCode())).toString();
    }

    public void GetRealLocale() {
        Locale langLocal = Locale.getDefault();
        String lang = Locale.getDefault().getLanguage();
        String locale2 = Locale.getDefault().getDisplayLanguage();
        this.DeviceCountry = Locale.getDefault().getCountry();
        if (this.DoLog) {
            System.out.println("SetLocale getDefault " + lang + " langLocal " + langLocal + " locale " + locale2 + " DeviceCountry " + this.DeviceCountry);
        }
        this.DeviceLocale = lang;
    }

    public int GetDeviceLocale() {
        String lang = this.DeviceLocale;
        if (lang.equals("en")) {
            return 0;
        }
        if (lang.equals("fr")) {
            return 1;
        }
        if (lang.equals("de")) {
            return 2;
        }
        if (lang.equals("it")) {
            return 3;
        }
        if (lang.equals("es")) {
            return 4;
        }
        if (lang.equals("ja")) {
            return 5;
        }
        if (lang.equals("ko")) {
            return 6;
        }
        if (lang.equals("sv")) {
            return 7;
        }
        if (lang.equals("no") || lang.equals("nb") || lang.equals("nn")) {
            return 8;
        }
        if (lang.equals("ru")) {
            return 9;
        }
        return 0;
    }

    public int GetLocale() {
        String lang = GetConfigSetting("currentLanguage");
        if (lang == "en") {
            return 0;
        }
        if (lang == "fr") {
            return 1;
        }
        if (lang == "de") {
            return 2;
        }
        if (lang == "it") {
            return 3;
        }
        if (lang == "es") {
            return 4;
        }
        if (lang == "ja") {
            return 5;
        }
        if (lang.equals("ko")) {
            return 6;
        }
        if (lang.equals("sv")) {
            return 7;
        }
        if (lang.equals("no") || lang.equals("nb") || lang.equals("nn")) {
            return 8;
        }
        return 0;
    }

    public void SetLocale(String lStr) {
        GetRealLocale();
        if (this.DoLog) {
            System.out.println("SetLocale " + lStr);
        }
        String lang = GetConfigSetting("currentLanguage");
        if (this.DoLog) {
            System.out.println("SetLocale oldlang " + lang);
        }
        String countyStr = "";
        if ("en".equals(lStr)) {
            if (this.DeviceCountry.equals("GB")) {
                countyStr = "GB";
            } else {
                countyStr = "US";
            }
        }
        this.locale = new Locale(lStr, countyStr);
        Locale.setDefault(this.locale);
        SetConfigSetting("currentLanguage", lStr);
    }

    public void RestoreCurrentLanguage() {
        String lang = GetConfigSetting("currentLanguage");
        String countyStr = "";
        if (!"".equals(lang)) {
            if ("en".equals(lang)) {
                if (this.DeviceCountry.equals("GB")) {
                    countyStr = "GB";
                } else {
                    countyStr = "US";
                }
            }
            this.locale = new Locale(lang, countyStr);
            Locale.setDefault(this.locale);
        }
    }

    public void SetLocale(int newLang) {
        String lStr;
        switch (newLang) {
            case 0:
                lStr = "en";
                break;
            case 1:
                lStr = "fr";
                break;
            case 2:
                lStr = "de";
                break;
            case 3:
                lStr = "it";
                break;
            case 4:
                lStr = "es";
                break;
            case 5:
                lStr = "ja";
                break;
            case 6:
                lStr = "ko";
                break;
            case 7:
                lStr = "sv";
                break;
            case 8:
                lStr = "no";
                break;
            default:
                lStr = "en";
                break;
        }
        if (this.DoLog) {
            System.out.println("SetLocale " + newLang + " lStr " + lStr);
        }
        String lang = GetConfigSetting("currentLanguage");
        if (this.DoLog) {
            System.out.println("SetLocale oldlang " + lang);
        }
        this.locale = new Locale(lStr);
        Locale.setDefault(this.locale);
        SetConfigSetting("currentLanguage", lStr);
    }

    public boolean DeleteFile(String filename) {
        String fullFilename = this.baseDirectory + "/" + filename.replace('\\', '/');
        File delfile = new File(fullFilename);
        if (this.DoLog) {
            System.out.println("trying to delete file " + fullFilename);
        }
        if (!delfile.exists()) {
            return false;
        }
        if (this.DoLog) {
            System.out.println("Deleting file " + fullFilename);
        }
        return delfile.delete();
    }

    public boolean FileRename(String oldfile, String newfile, int overWrite) {
        new File(this.baseDirectory + "/" + oldfile.replace('\\', '/')).renameTo(new File(this.baseDirectory + "/" + newfile.replace('\\', '/')));
        return true;
    }

    public String FileGetArchiveName(int type) {
        switch (type) {
            case 0:
                return this.apkFileName;
            case 1:
                return this.expansionFileName;
            case 2:
                return this.patchFileName;
            default:
                return "";
        }
    }

    public boolean IsTV() {
        return this.IsAndroidTV;
    }

    public String GetAndroidBuildinfo(int index) {
        switch (index) {
            case 0:
                return Build.MANUFACTURER;
            case 1:
                return Build.PRODUCT;
            case 2:
                return Build.MODEL;
            case 3:
                return Build.HARDWARE;
            default:
                return "UNKNOWN";
        }
    }

    public int GetDeviceInfo(int index) {
        switch (index) {
            case 0:
                return getNumberOfProcessors();
            case 1:
                return this.hasTouchScreen ? 1 : 0;
            default:
                return -1;
        }
    }

    public int GetDeviceType() {
        int isTegra;
        int i = 0;
        System.out.println("Build info version device  " + Build.DEVICE);
        System.out.println("Build MANUFACTURER  " + Build.MANUFACTURER);
        System.out.println("Build BOARD  " + Build.BOARD);
        System.out.println("Build DISPLAY  " + Build.DISPLAY);
        System.out.println("Build CPU_ABI  " + Build.CPU_ABI);
        System.out.println("Build CPU_ABI2  " + Build.CPU_ABI2);
        System.out.println("Build HARDWARE  " + Build.HARDWARE);
        System.out.println("Build MODEL  " + Build.MODEL);
        System.out.println("Build PRODUCT  " + Build.PRODUCT);
        if (this.glVendor.contains("NVIDIA")) {
            isTegra = 2;
        } else {
            isTegra = 0;
        }
        int numProcs = getNumberOfProcessors() * 4;
        int mem = this.availableMemory * 64;
        if (this.isPhone) {
            i = 1;
        }
        int ret = i + isTegra + numProcs + mem;
        if (!this.FinalRelease) {
            System.out.println("renderer '" + this.glVendor + "' ret=" + ret);
        }
        return ret;
    }

    private int getNumberOfProcessors() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            }).length;
        } catch (Exception e) {
            return 1;
        }
    }

    public void ShowKeyboard(int show) {
        if (getResources().getConfiguration().hardKeyboardHidden != 1) {
            InputMethodManager myImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (show != 0) {
                myImm.toggleSoftInput(2, 0);
                this.IsShowingKeyboard = true;
                return;
            }
            View tbview = getCurrentFocus();
            if (tbview != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(tbview.getWindowToken(), 0);
            }
            myImm.toggleSoftInput(0, 0);
            this.IsShowingKeyboard = false;
            System.out.println("hideSystemUI");
            hideSystemUI();
        }
    }

    public boolean IsKeyboardShown() {
        return this.IsShowingKeyboard;
    }

    private void setState(int newState) {
        if (this.mState != newState) {
            this.mState = newState;

        }
    }

    /* access modifiers changed from: private */
    public void setButtonPausedState(boolean paused) {
        int stringResourceID;
        this.mStatePaused = paused;
    }

    public void onServiceConnected(Messenger m) {

    }

    @SuppressLint("WrongConstant")
    public void onDownloadStateChanged(int newState) {
        boolean paused;
        boolean indeterminate;
        int newDashboardVisibility;
        int cellMessageVisibility;
        setState(newState);
        boolean showDashboard = true;
        boolean showCellMessage = false;
        switch (newState) {
            case 1:
                paused = false;
                indeterminate = true;
                break;
            case 2:
            case 3:
                showDashboard = true;
                paused = false;
                indeterminate = true;
                break;
            case 4:
                paused = false;
                showDashboard = true;
                indeterminate = false;
                break;
            case 5:
                if (expansionFilesDelivered()) {

                    return;
                }
                return;
            case 7:
                paused = true;
                indeterminate = false;
                break;
            case 8:
            case 9:
                showDashboard = false;
                paused = true;
                indeterminate = false;
                showCellMessage = true;
                break;
            case 12:
            case 14:
                paused = true;
                indeterminate = false;
                break;
            case 15:
            case 16:
            case 18:
            case 19:
                paused = true;
                showDashboard = true;
                indeterminate = false;
                break;
            default:
                paused = true;
                indeterminate = true;
                showDashboard = true;
                break;
        }
        if (showDashboard) {
            newDashboardVisibility = 0;
        } else {
            newDashboardVisibility = 8;
        }
        if (this.mDashboard.getVisibility() != newDashboardVisibility) {
            this.mDashboard.setVisibility(newDashboardVisibility);
        }
        if (showCellMessage) {
            cellMessageVisibility = 0;
        } else {
            cellMessageVisibility = 8;
        }
        if (this.mCellMessage.getVisibility() != cellMessageVisibility) {
            this.mCellMessage.setVisibility(cellMessageVisibility);
        }
        this.mPB.setIndeterminate(indeterminate);
        setButtonPausedState(paused);
    }


    public boolean IsCloudAvailable() {
        return false;
    }

    public void LoadAllGamesFromCloud() {
    }

    public String LoadGameFromCloud(int slot, byte[] array) {
        return "";
    }

    public void SaveGameToCloud(int slot, byte[] array, int numbytes) {
    }

    public boolean NewCloudSaveAvailable(int slot) {
        return false;
    }

    public void MovieKeepAspectRatio(boolean keep) {
        this.KeepAspectRatio = keep;
    }

    /* access modifiers changed from: package-private */
    public void ClearMovieText() {
        runOnUiThread(new Runnable() {
            public void run() {
                Canvas canvas = WarMedia.this.movieTextHolder.lockCanvas();
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                WarMedia.this.movieTextHolder.unlockCanvasAndPost(canvas);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void MovieSetTextScale(int scale) {
        this.movieTextScale = scale;
        SetPaint(-16711936, this.movieTextScale);
    }

    /* access modifiers changed from: package-private */
    @SuppressLint("RestrictedApi")
    public void SetPaint(int color, int size) {
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize((float) size);
        this.mTextPaint.setColor(color);
        this.mAscent = (int) this.mTextPaint.ascent();
        this.tPaint = new Paint();
        this.tPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.textPaint = new TextPaint();
        this.textPaint.setColor(-1);
        this.textPaint.setTextSize((float) this.movieTextScale);
        this.sTextPaint = new TextPaint();
        this.sTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.sTextPaint.setTextSize((float) this.movieTextScale);
    }

    /* access modifiers changed from: package-private */
    public void DrawMovieText() {
        runOnUiThread(new Runnable() {
            public void run() {
                boolean clearText = true;
                if (WarMedia.this.movieText.length() > 0 || WarMedia.this.movieSubtitleText.length() > 0) {
                    clearText = false;
                    WarMedia.this.movieTextSurface.setVisibility(View.VISIBLE);
                    String drawText = WarMedia.this.movieSubtitleText + "\n\n" + (WarMedia.this.movieTextDisplayNow ? WarMedia.this.movieText : "");
                    Canvas canvas = WarMedia.this.movieTextHolder.lockCanvas();
                    if (canvas != null) {
                        StaticLayout layoutText = new StaticLayout(drawText, WarMedia.this.textPaint, WarMedia.this.surfaceWidth - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
                        StaticLayout sLayoutText = new StaticLayout(drawText, WarMedia.this.sTextPaint, WarMedia.this.surfaceWidth - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        canvas.translate(52.0f, (float) ((WarMedia.this.surfaceHeight - sLayoutText.getHeight()) - 5));
                        sLayoutText.draw(canvas);
                        canvas.translate(-2.0f, -2.0f);
                        layoutText.draw(canvas);
                        WarMedia.this.movieTextHolder.unlockCanvasAndPost(canvas);
                    }
                }
                if (clearText) {
                    Canvas canvas2 = WarMedia.this.movieTextHolder.lockCanvas();
                    if (canvas2 != null) {
                        canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
                        WarMedia.this.movieTextHolder.unlockCanvasAndPost(canvas2);
                    }
                    WarMedia.this.movieTextSurface.setVisibility(View.GONE);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void DisplayMovieText() {
        System.out.println("DisplayMovieText vidViewIsActive " + this.vidViewIsActive);
        DrawMovieText();
    }

    public void MovieClearText(boolean isSubtitle) {
        if (isSubtitle) {
            this.movieSubtitleText = "";
        } else {
            this.movieText = "";
        }
        DrawMovieText();
    }

    public void MovieSetText(String text, boolean DisplayNow, boolean isSubtitle) {
        if (isSubtitle) {
            this.movieSubtitleText = text;
        } else {
            this.DisplayMovieTextOnTap = !DisplayNow;
            this.movieTextDisplayNow = DisplayNow;
            this.movieText = text;
        }
        if (DisplayNow) {
            DisplayMovieText();
        } else if (!DisplayNow) {
            runOnUiThread(new Runnable() {
                public void run() {
                    System.out.println("MovieSetText create surface");
                    WarMedia.this.movieTextSurface.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void MovieDisplayText(boolean display) {
        System.out.println("MovieDisplayText " + display);
        if (display && !this.MovieTextDisplayed) {
            DisplayMovieText();
        }
    }

    public void DisplaySplashScreen(final String splashPng, final int splashTimer) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    WarMedia.this.handler.postDelayed(new Runnable() {
                        public void run() {
                            WarMedia.this.ClearSplashScreen();
                        }
                    }, (long) splashTimer);
                    ImageView splashView = new ImageView(WarMedia.this.getApplicationContext());
                    WarMedia.this.llSplashView = new LinearLayout(WarMedia.this.getApplicationContext());
                    Drawable dr = Drawable.createFromStream(WarMedia.this.getAssets().open(splashPng), (String) null);
                    int width = dr.getIntrinsicWidth() - 40;
                    int height = dr.getIntrinsicHeight() - 40;
                    splashView.setImageDrawable(dr);
                    int surfaceView_Width = WarMedia.this.metrics.widthPixels;
                    int surfaceView_Height = WarMedia.this.metrics.heightPixels;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    float aspectratio = ((float) width) / ((float) height);
                    if (((float) surfaceView_Width) / ((float) width) > ((float) surfaceView_Height) / ((float) height)) {
                        layoutParams.width = (int) (((float) surfaceView_Height) * aspectratio);
                        layoutParams.height = surfaceView_Height;
                    } else {
                        layoutParams.width = surfaceView_Width;
                        layoutParams.height = (int) (((float) surfaceView_Width) / aspectratio);
                    }
                    int i = (surfaceView_Width - layoutParams.width) / 2;
                    int i2 = (surfaceView_Height - layoutParams.height) / 2;
                    layoutParams.gravity = 17;
                    WarMedia.this.llSplashView.addView(splashView, layoutParams);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -1);
                    layoutParams2.gravity = 17;
                    WarMedia.this.addContentView(WarMedia.this.llSplashView, layoutParams2);
                } catch (Exception e) {
                    WarMedia.this.llSplashView = null;
                    System.out.println("DisplaySplashScreeen Error");
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void ClearSplashScreen() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (WarMedia.this.DoLog) {
                    System.out.println("Clearing SplashScreen ");
                }
                if (WarMedia.this.llSplashView != null) {
                    WarMedia.this.llSplashView.setVisibility(View.GONE);
                }
                WarMedia.this.llSplashView = null;
            }
        });
    }

    public int GetSpecialBuildType() {
        return this.SpecialBuildType;
    }

    public void DeleteDirectory(String dirString, boolean recurse) {
        File dir = new File(dirString);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File child = new File(dir, children[i]);
                if (child.isDirectory()) {
                    DeleteDirectory(dir + "/" + children[i], recurse);
                }
                System.out.println("delete " + dir + "/" + children[i]);
                child.delete();
            }
            dir.delete();
        }
    }

    public boolean CustomLoadFunction() {
        return false;
    }

    public void AfterDownloadFunction() {
        DoResumeEvent();
    }

    public void SendStatEvent(String eventId, boolean timedEvent) {
    }

    public void SendTimedStatEventEnd(String eventId) {
    }

    public void SendStatEvent(String eventId, String paramName, String paramValue, boolean timedEvent) {
    }

    public int GetTotalMemory() {
        return this.totalMemory;
    }

    public float GetScreenWidthInches() {
        return this.screenWidthInches;
    }

    public int GetLowThreshhold() {
        return this.memoryThreshold;
    }

    public int GetAvailableMemory() {
        try {
            Runtime info = Runtime.getRuntime();
            long usedSize = info.totalMemory() - ((long) ((int) ((info.freeMemory() ))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetMemoryInfo(false);
        return this.availableMemory;
    }

    public String GetAppId() {
        return getPackageName();
    }

    public void ScreenSetWakeLock(boolean enable) {
        if (enable) {
            this.myWakeLock.acquire();
        } else {
            this.myWakeLock.release();
        }
    }

    public void CreateTextBox(int id, int x, int y, int x2, int y2) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("onRequestPermissionsResult");
        switch (requestCode) {
            case 8001:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    System.out.println("Exiting App");
                    finish();
                    return;
                }
                this.waitForPermissions = false;
                localHasGameData();
                return;
            default:
                return;
        }
    }

    public String GetPackageName(String appname) {
        List<ApplicationInfo> appinfo = getPackageManager().getInstalledApplications(8192);
        for (int i = 0; i < appinfo.size(); i++) {
            if (appname.compareToIgnoreCase(appinfo.get(i).packageName.toString()) == 0) {
                return appinfo.get(i).sourceDir;
            }
        }
        return "";
    }

    public boolean IsAppInstalled(String appname) {
        List<ApplicationInfo> appinfo = getPackageManager().getInstalledApplications(8192);
        for (int i = 0; i < appinfo.size(); i++) {
            System.out.println("app[" + i + "]=" + appinfo.get(i).packageName + " dir " + appinfo.get(i).sourceDir);
            if (appname.compareToIgnoreCase(appinfo.get(i).packageName.toString()) == 0) {
                return true;
            }
        }
        return false;
    }
    ///
    public void OpenLink(String str)
    {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        System.out.println("**** OpenLink");
    }



    public void SendStatEvent(String str) {
        System.out.println("**** SendStatEvent");
    }

    public void SendStatEvent(String str, String str2, String str3) {
        System.out.println("**** SendStatEvent1");
    }


    ///

    public boolean CheckIfNeedsReadPermission(Activity me) {
        boolean askForPermission = true;

        this.waitForPermissions = true;
        this.delaySetContentView = true;
        if (ActivityCompat.shouldShowRequestPermissionRationale(me, "android.permission.READ_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(me, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 8001);
            return true;
        }
        ActivityCompat.requestPermissions(me, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 8001);
        return true;
    }

    public boolean CopyFileFromAssets(String filename, String destPathName, boolean overwrite) {
        File ringtone = new File(destPathName);
        if (ringtone.exists()) {
            return false;
        }
        ringtone.getParentFile().mkdirs();
        try {
            InputStream in = getAssets().open(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(destPathName);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        fileOutputStream.write(buffer, 0, read);
                    } else {
                        in.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        return true;
                    }
                }
            } catch (Exception e) {
                FileOutputStream fileOutputStream2 = fileOutputStream;
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public boolean ConvertToBitmap(byte[] data, int length) {
        return false;
    }

    public boolean ServiceAppCommand(String cmd, String args) {
        return false;
    }

    public int ServiceAppCommandValue(String cmd, String args) {
        return 0;
    }

    public boolean ServiceAppCommandInt(String cmd, int args) {
        return false;
    }
}
