package com.wardrumstudios.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.drive.DriveFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import org.apache.commons.net.ftp.FTPClient;

public class WarDownloader {
    public String ACCEPT_EULA_BUTTON;
    public String ADDITIONAL_DATA_NEEDED;
    public String APPLICATION_EXITING_TEXT;
    boolean BreakOut = false;
    public String CANCEL_BUTTON;
    public String CANNOT_DOWNLOAD_LL_DATA;
    public String CANNOT_WRITE_DATA;
    int CompressionType = -1;
    public String DATA_ACCESS_ERROR;
    public String DATA_ACCESS_ERROR_MESSAGE;
    public String DATA_INACCESSIBLE;
    public String DECLARE_EULA_BUTTON;
    public String DOWNLOADING_FILES_TEXT;
    public String DOWNLOAD_COMPLETE;
    boolean DoDownloadTest = false;
    final boolean DoLog = false;
    boolean DownloadButUseLocalLocation = false;
    final String DownloadCheckVersion = "Downloadv9";
    int DownloadFailedMessage = 1;
    ProgressDialog DownloadProgress;
    public String ESTIMATED_COMPLETION_TEXT;
    public String EULA1;
    public String EULA2;
    public String EULA3;
    public String EULA4;
    public String EXIT_BUTTON;
    public String EXPANDING_AUDIO_FILES;
    public String FATAL_AUDIO_EXTRACTION;
    public String FILES_TEXT;
    boolean IOWriteError = false;
    boolean IsDisplayingSpash = false;
    public String LICENSE_ERROR;
    public String LICENSE_ERROR_MESSAGE;
    public String LOADING_MESSAGE;
    public String MINUTES_TEXT;
    public String NEXT_BUTTON;
    public String NO_NETWORK;
    public String NO_WIFI;
    public int NumThreadsUnzipping = 0;
    final String OldDownloadCheckVersion = "Downloadv8";
    public String PLEASE_WAIT_TEXT;
    boolean ProgressInited = false;
    public String RETURN_TEXT;
    public String SECONDS_TEXT;
    public int SpashIcon = 0;
    long StartTime = 0;
    final String TAG = "DownloadManager";
    protected boolean UseAPKData = false;
    public boolean UseFTP = false;
    boolean UseLocalData = false;
    public boolean UseMediaVault = true;
    public boolean UseWardrumData = false;
    final String VerifyCheckVersion = "VerifyV1";
    private final String WardrumFtpdownloadLocation = "MobileDownloads/gta3";
    private final String WardrumdownloadLocation = "http://wardrumstudios.com/MobileDownloads/gta3/";
    long amountDownloaded = 0;
    long amountReallyDownloaded = 0;
    String baseRootDirectory = "/mnt/sdcard/GTA3/";
    byte[] buf;
    boolean checkData = true;
    String[] checkModels = {"models/gta3_unc.img", "models/gta3_dxt.img", "models/gta3_atc.img", "models/gta3_pvr.img"};
    public int compressionScheme = 0;
    String currentDownloadingFile = null;
    long[] currentRollingAmount;
    long[] currentRollingStart;
    long[] currentRollingTime;
    byte[] downloadBuffer = null;
    CDownloadFileList[] downloadFileList;
    private String downloadLocation = "";
    long downloadTime = 0;
    public String downloadTitle = "Vice City";
    int filesDownloaded = 0;
    int filesToDownload = 0;
    public int filesUnzipped = 0;
    boolean firstTry = true;
    FTPClient ftpClient = null;
    boolean justCheckImg = false;
    int lastUpdateBytes = 0;
    public long legalStartTime = 0;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    MediaVault mv = null;
    WarDownloader myWarDownloader = null;
    WarMedia myWarMedia = null;
    String notifyMessage = "";
    int numOfDownloadFiles = 0;
    long realTimeToDownload = 0;
    int rollIndex = -1;
    SurfaceHolder splashHolder;
    boolean splashViewCreated = false;
    long timeToDownload = 0;
    long totalToDownload = 0;
    private String try1 = "";

    public void SetKey(String key) {
        this.try1 = key;
    }

    public void SetLocation(String loc) {
        this.downloadLocation = loc;
    }

    public void SetWarMedia(WarMedia warMedia) {
        this.myWarMedia = warMedia;
    }

    /* access modifiers changed from: package-private */
    public void ShowEULA() {
        while (!this.myWarMedia.HasGLExtensions) {
            this.myWarMedia.mSleep(100);
        }
        String version = this.myWarMedia.GetConfigSetting("DownloadCheckVersion");
        String GetConfigSetting = this.myWarMedia.GetConfigSetting("VerifyCheckVersion");
        boolean dataExists = version.equalsIgnoreCase("Downloadv9");
        if (dataExists) {
            if (!VerifyData()) {
                dataExists = false;
            } else {
                this.myWarMedia.SetConfigSetting("DownloadCheckVersion", "Downloadv9");
                this.myWarMedia.SetConfigSetting("VerifyCheckVersion", "VerifyV1");
            }
        }
        if (dataExists) {
            this.myWarMedia.DoResumeEvent();
        } else if (this.myWarMedia.GetConfigSetting("VerifyCheckVersion").equalsIgnoreCase("Yes")) {
            this.myWarMedia.runOnUiThread(new Runnable() {
                public void run() {
                    WarDownloader.this.CheckAndDownload();
                }
            });
        } else {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this.myWarMedia).setMessage(this.EULA2 + "\n\n" + this.EULA4 + "\n\n").setPositiveButton(this.ACCEPT_EULA_BUTTON, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface i, int a) {
                    i.dismiss();
                    WarDownloader.this.myWarMedia.SetConfigSetting("VerifyCheckVersion", "Yes");
                    WarDownloader.this.myWarMedia.runOnUiThread(new Runnable() {
                        public void run() {
                            WarDownloader.this.CheckAndDownload();
                        }
                    });
                }
            }).setNegativeButton(this.DECLARE_EULA_BUTTON, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface i, int a) {
                    WarDownloader.this.myWarMedia.finish();
                }
            }).setCancelable(false);
            alert.setTitle("EULA");
            this.myWarMedia.runOnUiThread(new Runnable() {
                public void run() {
                    alert.create().show();
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void ClearSplash() {
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                if (WarDownloader.this.myWarMedia.splashView != null) {
                    WarDownloader.this.myWarMedia.splashView.setVisibility(View.GONE);
                }
                WarDownloader.this.myWarMedia.splashView = null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void DisplaySplash() {
        try {
            LinearLayout.LayoutParams myParams = new LinearLayout.LayoutParams(-2, -2);
            this.myWarMedia.splashView = new View(this.myWarMedia);
            this.myWarMedia.addContentView(this.myWarMedia.splashView, myParams);
            this.myWarMedia.splashView.setBackgroundDrawable(Drawable.createFromStream(this.myWarMedia.getAssets().open("SPLASH1.PNG"), (String) null));
        } catch (Exception e) {
            this.myWarMedia.splashView.setBackgroundResource(this.SpashIcon);
        }
        this.IsDisplayingSpash = true;
    }

    public boolean CheckCompressionFile() {
        CheckToSkipTexture("");
        System.out.println("Check for compression " + this.CompressionType);
        if (this.CompressionType >= 0) {
            if (new File(this.baseRootDirectory + this.checkModels[this.CompressionType]).exists()) {
                return true;
            }
            System.out.println("File " + this.checkModels[this.CompressionType] + " does not exist for compression type " + this.CompressionType);
            return false;
        } else if (this.myWarMedia.glExtensions != "") {
            return true;
        } else {
            for (int i = 0; i < 4; i++) {
                if (!new File(this.baseRootDirectory + this.checkModels[i]).exists()) {
                    System.out.println("File " + this.checkModels[i] + " does not exist for compression type " + this.CompressionType);
                    return false;
                }
            }
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void CheckAndDownload() {
        System.out.println("UseAPKData " + this.UseAPKData);
        if (this.UseAPKData) {
            this.myWarMedia.SetConfigSetting("DownloadCheckVersion", "Downloadv9");
            this.myWarMedia.DoResumeEvent();
            return;
        }
        if (!this.justCheckImg) {
            File gtaset = new File(this.baseRootDirectory + "gta3.set");
            if (gtaset.exists()) {
                gtaset.delete();
            }
        }
        CheckForDrive();
        while (!this.myWarMedia.HasGLExtensions) {
            System.out.println("wait for HasGLExtensions");
            this.myWarMedia.mSleep(10);
        }
        DownloadDataCheck();
    }

    /* access modifiers changed from: package-private */
    public void DownloadDataCheck() {
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                WarDownloader.this.DisplaySplash();
                if (!WarDownloader.this.checkData) {
                    WarDownloader.this.myWarMedia.DoResumeEvent();
                } else if (!WarDownloader.this.myWarMedia.isNetworkAvailable()) {
                    WarDownloader.this.ShowDownloadNetworkError();
                } else if (!WarDownloader.this.myWarMedia.isWiFiAvailable()) {
                    WarDownloader.this.WiFiMessage();
                } else {
                    WarDownloader.this.DownloadDataMessage();
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void ShowDownloadNetworkError() {
        WarMedia warMedia = this.myWarMedia;
        final String fTitle = this.NO_NETWORK;
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(WarDownloader.this.myWarMedia).setTitle(fTitle).setPositiveButton(WarDownloader.this.EXIT_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarDownloader.this.myWarMedia.finish();
                    }
                }).setNegativeButton(WarDownloader.this.NEXT_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarDownloader.this.myWarMedia.runOnUiThread(new Runnable() {
                            public void run() {
                                WarDownloader.this.DownloadDataCheck();
                            }
                        });
                    }
                }).setCancelable(false).show().setCanceledOnTouchOutside(false);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void WiFiMessage() {
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(WarDownloader.this.myWarMedia).setMessage(WarDownloader.this.NO_WIFI).setPositiveButton(WarDownloader.this.NEXT_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        i.dismiss();
                        new Thread(new Runnable() {
                            public void run() {
                                WarDownloader.this.DownloadData();
                            }
                        }).start();
                    }
                }).setNegativeButton(WarDownloader.this.CANCEL_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarDownloader.this.myWarMedia.finish();
                    }
                }).setCancelable(false).create().show();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void DownloadDataMessage() {
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(WarDownloader.this.myWarMedia).setMessage(WarDownloader.this.ADDITIONAL_DATA_NEEDED).setPositiveButton(WarDownloader.this.NEXT_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        i.dismiss();
                        new Thread(new Runnable() {
                            public void run() {
                                WarDownloader.this.DownloadData();
                            }
                        }).start();
                    }
                }).setNegativeButton(WarDownloader.this.CANCEL_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarDownloader.this.myWarMedia.finish();
                    }
                }).setCancelable(false).create().show();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean VerifyData() {
        if (!ReadFilelist()) {
            return true;
        }
        int filenum = 0;
        while (filenum < this.numOfDownloadFiles) {
            String to = this.myWarMedia.baseDirectory + "/" + this.downloadFileList[filenum].filename;
            try {
                if (!CheckToSkipTexture(to)) {
                    File file = new File(to);
                    if (!file.exists() || ((long) this.downloadFileList[filenum].size) != file.length()) {
                        return false;
                    }
                }
                filenum++;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void CheckForDrive() {
        new File(this.baseRootDirectory);
    }

    /* access modifiers changed from: package-private */
    public boolean DownloadData() {
        if (DownloadAllFiles()) {
            this.buf = null;
        } else {
            this.buf = null;
            this.myWarMedia.runOnUiThread(new Runnable() {
                public void run() {
                    String errMess;
                    System.out.println("Data download failed");
                    if (WarDownloader.this.IOWriteError) {
                        errMess = WarDownloader.this.CANNOT_WRITE_DATA;
                    } else if (WarDownloader.this.DownloadFailedMessage == 1) {
                        errMess = WarDownloader.this.DATA_INACCESSIBLE;
                    } else if (WarDownloader.this.DownloadFailedMessage == 2) {
                        errMess = WarDownloader.this.CANNOT_DOWNLOAD_LL_DATA;
                    } else {
                        errMess = WarDownloader.this.FATAL_AUDIO_EXTRACTION;
                    }
                    new AlertDialog.Builder(WarDownloader.this.myWarMedia).setMessage(errMess).setPositiveButton(WarDownloader.this.EXIT_BUTTON, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface i, int a) {
                            WarDownloader.this.myWarMedia.finish();
                        }
                    }).setCancelable(false).show();
                }
            });
        }
        this.myWarMedia.ClearSystemNotification();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void ShowErrorMessage(int err) {
        this.myWarMedia.runOnUiThread(new Runnable() {
            public void run() {
                System.out.println("Data download failed");
                new AlertDialog.Builder(WarDownloader.this.myWarMedia).setMessage(WarDownloader.this.DATA_ACCESS_ERROR_MESSAGE).setPositiveButton(WarDownloader.this.EXIT_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        WarDownloader.this.myWarMedia.finish();
                    }
                }).setCancelable(false).show();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public int CheckAndCreate(String to, int filesize) {
        File file = new File(to);
        if (file.exists() && (filesize == 0 || file.length() == ((long) filesize))) {
            return 1;
        }
        boolean mkdirs = new File(to.substring(0, to.lastIndexOf(47))).mkdirs();
        return 0;
    }

    public boolean downloadFile(String filename) {
        return downloadFile(filename, 0, true) != 0;
    }

    private class progressFileOutputStream extends FileOutputStream {
        public boolean doCheck = true;
        int outputCount = 0;

        public progressFileOutputStream(File file) throws FileNotFoundException {
            super(file);
        }

        public synchronized void write(byte[] b) throws IOException {
            super.write(b);
        }

        public synchronized void write(byte b) throws IOException {
            super.write(b);
        }

        public synchronized void write(byte[] b, int off, int len) throws IOException {
            if (WarDownloader.this.BreakOut) {
                throw new IOException();
            }
            super.write(b, off, len);
            if (this.doCheck) {
                this.outputCount += len;
                WarDownloader.this.updateProgress(len, 0, true);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void downloadHttpFile(String from, String to) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (CheckAndCreate(to, 0) <= 0) {
                File file = new File(to);
                this.currentDownloadingFile = to;
                HttpURLConnection ucon = (HttpURLConnection) new URL(from).openConnection();
                if (ucon.getResponseCode() != 200) {
                    System.out.println("ERROR Downloading " + ucon.getResponseCode());
                    this.DownloadFailedMessage = 2;
                }
                InputStream is = ucon.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                if (this.downloadBuffer == null) {
                    this.downloadBuffer = new byte[65536];
                }
                int totalBytes = 0;
                while (true) {
                    int numBytesRead = is.read(this.downloadBuffer, 0, 65536);
                    if (numBytesRead == -1) {
                        fos.close();
                        this.currentDownloadingFile = null;
                        return;
                    }
                    fos.write(this.downloadBuffer, 0, numBytesRead);
                    totalBytes += numBytesRead;
                }
            }
        } catch (IOException e) {
            Log.d("DownloadManager", "Error: " + e);
        }
    }

    static MediaVaultRequest getTimeParameters(String URL) {
        long unixNow = (Calendar.getInstance().getTimeInMillis() / 1000) - 3600;
        MediaVaultRequest options = new MediaVaultRequest(URL);
        options.setStartTime(unixNow);
        options.setEndTime(7200 + unixNow);
        return options;
    }

    private String GetVal(String tval) {
        String val = "";
        for (int i = 0; i < tval.length(); i += 2) {
            val = val + ((char) (tval.charAt(i + 1) - Integer.parseInt(tval.substring(i, i + 1))));
        }
        return val;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0194, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0195, code lost:
        java.lang.System.out.println("FileNotFoundException " + r7.getMessage());
        r30.IOWriteError = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        java.lang.System.out.println("Canceled Download");
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x01c8, code lost:
        return -1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0194 A[ExcHandler: FileNotFoundException (r7v1 'ex' java.io.FileNotFoundException A[CUSTOM_DECLARE]), Splitter:B:3:0x000a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int downloadFile(String r31, int r32, boolean r33) {
        /*
            r30 = this;
            r0 = r30
            boolean r0 = r0.UseFTP
            r27 = r0
            if (r27 == 0) goto L_0x01f0
            r0 = r30
            boolean r0 = r0.UseWardrumData     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            if (r27 != 0) goto L_0x0067
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            java.lang.String r0 = r0.downloadLocation     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28 = r0
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r11 = r27.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
        L_0x002b:
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            com.wardrumstudios.utils.WarMedia r0 = r0.myWarMedia     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28 = r0
            r0 = r28
            java.lang.String r0 = r0.baseDirectory     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28 = r0
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = "/"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r19 = r27.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            long r20 = java.lang.System.currentTimeMillis()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            r1 = r19
            r2 = r32
            int r8 = r0.CheckAndCreate(r1, r2)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            if (r33 == 0) goto L_0x007f
            if (r8 <= 0) goto L_0x007f
            r18 = -2
        L_0x0066:
            return r18
        L_0x0067:
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = "/"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r11 = r27.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            goto L_0x002b
        L_0x007f:
            r0 = r19
            r1 = r30
            r1.currentDownloadingFile = r0     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            if (r27 != 0) goto L_0x0108
            org.apache.commons.net.ftp.FTPClient r27 = new org.apache.commons.net.ftp.FTPClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r27
            r1 = r30
            r1.ftpClient = r0     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r28 = 5
            r27.setConnectTimeout(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            java.lang.String r28 = "wardrumstudios.com"
            r27.connect((java.lang.String) r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r26 = "wargtavc"
            java.lang.String r16 = "Gt56kj9Dl4f6B!"
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r0 = r27
            r1 = r26
            r2 = r16
            boolean r17 = r0.login(r1, r2)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = "ftp login "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r28
            r1 = r17
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = r28.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.println(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r28 = 2
            r27.setFileType(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r27.enterLocalPassiveMode()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r28 = 1
            r27.setKeepAlive(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27 = r0
            r28 = 1048576(0x100000, float:1.469368E-39)
            r27.setBufferSize(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
        L_0x0108:
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = "ftp download "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r28
            java.lang.StringBuilder r28 = r0.append(r11)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = " to "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r28
            r1 = r19
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = r28.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.println(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.io.File r9 = new java.io.File     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r19
            r9.<init>(r0)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            com.wardrumstudios.utils.WarDownloader$progressFileOutputStream r10 = new com.wardrumstudios.utils.WarDownloader$progressFileOutputStream     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            r10.<init>(r9)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = "fos "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = r10.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = r28.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.println(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r30
            org.apache.commons.net.ftp.FTPClient r0 = r0.ftpClient     // Catch:{ Exception -> 0x01bd, FileNotFoundException -> 0x0194 }
            r27 = r0
            r0 = r27
            boolean r13 = r0.retrieveFile(r11, r10)     // Catch:{ Exception -> 0x01bd, FileNotFoundException -> 0x0194 }
            if (r13 != 0) goto L_0x016f
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ Exception -> 0x01bd, FileNotFoundException -> 0x0194 }
            java.lang.String r28 = "ftpRet failed "
            r27.println(r28)     // Catch:{ Exception -> 0x01bd, FileNotFoundException -> 0x0194 }
        L_0x016f:
            r10.close()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            if (r32 <= 0) goto L_0x01cc
            r18 = r32
        L_0x0176:
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r28.<init>()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r29 = "ret "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r0 = r28
            r1 = r18
            java.lang.StringBuilder r28 = r0.append(r1)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = r28.toString()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r27.println(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            goto L_0x0066
        L_0x0194:
            r7 = move-exception
            java.io.PrintStream r27 = java.lang.System.out
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "FileNotFoundException "
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = r7.getMessage()
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            r27.println(r28)
            r27 = 1
            r0 = r27
            r1 = r30
            r1.IOWriteError = r0
        L_0x01b9:
            r18 = -1
            goto L_0x0066
        L_0x01bd:
            r7 = move-exception
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            java.lang.String r28 = "Canceled Download"
            r27.println(r28)     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r10.close()     // Catch:{ FileNotFoundException -> 0x0194, Exception -> 0x01cf }
            r18 = -1
            goto L_0x0066
        L_0x01cc:
            r18 = 100
            goto L_0x0176
        L_0x01cf:
            r7 = move-exception
            java.io.PrintStream r27 = java.lang.System.out
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "ftp error "
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = r7.getMessage()
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            r27.println(r28)
            r7.printStackTrace()
            goto L_0x01b9
        L_0x01f0:
            r4 = 1048576(0x100000, float:1.469368E-39)
            r0 = r30
            boolean r0 = r0.UseWardrumData     // Catch:{ IOException -> 0x03bb }
            r27 = r0
            if (r27 != 0) goto L_0x0281
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x03bb }
            r27.<init>()     // Catch:{ IOException -> 0x03bb }
            r0 = r30
            java.lang.String r0 = r0.downloadLocation     // Catch:{ IOException -> 0x03bb }
            r28 = r0
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ IOException -> 0x03bb }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r12 = r27.toString()     // Catch:{ IOException -> 0x03bb }
        L_0x0215:
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x03bb }
            r27.<init>()     // Catch:{ IOException -> 0x03bb }
            r0 = r30
            com.wardrumstudios.utils.WarMedia r0 = r0.myWarMedia     // Catch:{ IOException -> 0x03bb }
            r28 = r0
            r0 = r28
            java.lang.String r0 = r0.baseDirectory     // Catch:{ IOException -> 0x03bb }
            r28 = r0
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r28 = "/"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ IOException -> 0x03bb }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r19 = r27.toString()     // Catch:{ IOException -> 0x03bb }
            r0 = r30
            com.wardrumstudios.utils.MediaVault r0 = r0.mv     // Catch:{ IOException -> 0x03bb }
            r27 = r0
            if (r27 != 0) goto L_0x025d
            com.wardrumstudios.utils.MediaVault r27 = new com.wardrumstudios.utils.MediaVault     // Catch:{ IOException -> 0x03bb }
            r0 = r30
            java.lang.String r0 = r0.try1     // Catch:{ IOException -> 0x03bb }
            r28 = r0
            r0 = r30
            r1 = r28
            java.lang.String r28 = r0.GetVal(r1)     // Catch:{ IOException -> 0x03bb }
            r27.<init>(r28)     // Catch:{ IOException -> 0x03bb }
            r0 = r27
            r1 = r30
            r1.mv = r0     // Catch:{ IOException -> 0x03bb }
        L_0x025d:
            r0 = r30
            com.wardrumstudios.utils.MediaVault r0 = r0.mv     // Catch:{ IOException -> 0x03bb }
            r27 = r0
            com.wardrumstudios.utils.MediaVaultRequest r28 = getTimeParameters(r12)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r11 = r27.compute(r28)     // Catch:{ IOException -> 0x03bb }
            long r20 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x03bb }
            r0 = r30
            r1 = r19
            r2 = r32
            int r8 = r0.CheckAndCreate(r1, r2)     // Catch:{ IOException -> 0x03bb }
            if (r33 == 0) goto L_0x02a0
            if (r8 <= 0) goto L_0x02a0
            r18 = -2
            goto L_0x0066
        L_0x0281:
            java.lang.StringBuilder r27 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x03bb }
            r27.<init>()     // Catch:{ IOException -> 0x03bb }
            java.lang.String r28 = "http://wardrumstudios.com/MobileDownloads/gta3/"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ IOException -> 0x03bb }
            r0 = r27
            r1 = r31
            java.lang.StringBuilder r27 = r0.append(r1)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r28 = ";type=i"
            java.lang.StringBuilder r27 = r27.append(r28)     // Catch:{ IOException -> 0x03bb }
            java.lang.String r12 = r27.toString()     // Catch:{ IOException -> 0x03bb }
            goto L_0x0215
        L_0x02a0:
            java.io.File r9 = new java.io.File     // Catch:{ IOException -> 0x03bb }
            r0 = r19
            r9.<init>(r0)     // Catch:{ IOException -> 0x03bb }
            r0 = r19
            r1 = r30
            r1.currentDownloadingFile = r0     // Catch:{ IOException -> 0x03bb }
            java.net.URL r25 = new java.net.URL     // Catch:{ IOException -> 0x03bb }
            r0 = r25
            r0.<init>(r11)     // Catch:{ IOException -> 0x03bb }
            r22 = 0
            java.net.Proxy r27 = java.net.Proxy.NO_PROXY     // Catch:{ Exception -> 0x03e2 }
            r0 = r25
            r1 = r27
            java.net.URLConnection r24 = r0.openConnection(r1)     // Catch:{ Exception -> 0x03e2 }
            java.net.HttpURLConnection r24 = (java.net.HttpURLConnection) r24     // Catch:{ Exception -> 0x03e2 }
            int r27 = r24.getResponseCode()     // Catch:{ Exception -> 0x03e2 }
            r28 = 200(0xc8, float:2.8E-43)
            r0 = r27
            r1 = r28
            if (r0 == r1) goto L_0x0314
            java.io.PrintStream r27 = java.lang.System.out     // Catch:{ Exception -> 0x03e2 }
            java.lang.StringBuilder r28 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x03e2 }
            r28.<init>()     // Catch:{ Exception -> 0x03e2 }
            java.lang.String r29 = "ERROR Downloading "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ Exception -> 0x03e2 }
            int r29 = r24.getResponseCode()     // Catch:{ Exception -> 0x03e2 }
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ Exception -> 0x03e2 }
            java.lang.String r29 = " message "
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ Exception -> 0x03e2 }
            java.lang.String r29 = r24.getResponseMessage()     // Catch:{ Exception -> 0x03e2 }
            java.lang.StringBuilder r28 = r28.append(r29)     // Catch:{ Exception -> 0x03e2 }
            java.lang.String r28 = r28.toString()     // Catch:{ Exception -> 0x03e2 }
            r27.println(r28)     // Catch:{ Exception -> 0x03e2 }
            r0 = r30
            boolean r0 = r0.firstTry     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            if (r27 == 0) goto L_0x0308
            r27 = 0
            r0 = r27
            r1 = r30
            r1.firstTry = r0     // Catch:{ Exception -> 0x03e2 }
        L_0x0308:
            r27 = 2
            r0 = r27
            r1 = r30
            r1.DownloadFailedMessage = r0     // Catch:{ Exception -> 0x03e2 }
            r18 = -1
            goto L_0x0066
        L_0x0314:
            java.io.InputStream r14 = r24.getInputStream()     // Catch:{ Exception -> 0x03e2 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x03e2 }
            r10.<init>(r9)     // Catch:{ Exception -> 0x03e2 }
            r0 = r30
            byte[] r0 = r0.downloadBuffer     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            if (r27 != 0) goto L_0x0333
            r27 = 1048576(0x100000, float:1.469368E-39)
            r0 = r27
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            r0 = r27
            r1 = r30
            r1.downloadBuffer = r0     // Catch:{ Exception -> 0x03e2 }
        L_0x0333:
            r15 = 0
            r5 = 0
            r23 = 0
        L_0x0337:
            r0 = r30
            byte[] r0 = r0.downloadBuffer     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            r28 = 1048576(0x100000, float:1.469368E-39)
            int r28 = r28 - r5
            r0 = r27
            r1 = r28
            int r15 = r14.read(r0, r5, r1)     // Catch:{ Exception -> 0x03e2 }
            r27 = -1
            r0 = r27
            if (r15 != r0) goto L_0x0387
            if (r23 <= 0) goto L_0x0378
            r0 = r30
            byte[] r0 = r0.downloadBuffer     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            r28 = 0
            r0 = r27
            r1 = r28
            r2 = r23
            r10.write(r0, r1, r2)     // Catch:{ Exception -> 0x03e2 }
            int r22 = r22 + r23
            if (r33 == 0) goto L_0x0375
            r27 = 0
            r28 = 1
            r0 = r30
            r1 = r23
            r2 = r27
            r3 = r28
            r0.updateProgress(r1, r2, r3)     // Catch:{ Exception -> 0x03e2 }
        L_0x0375:
            r5 = 0
            r23 = r5
        L_0x0378:
            r10.close()     // Catch:{ Exception -> 0x03e2 }
        L_0x037b:
            r27 = 0
            r0 = r27
            r1 = r30
            r1.currentDownloadingFile = r0     // Catch:{ IOException -> 0x03bb }
            r18 = r22
            goto L_0x0066
        L_0x0387:
            int r23 = r23 + r15
            int r5 = r5 + r15
            r27 = 524288(0x80000, float:7.34684E-40)
            r0 = r23
            r1 = r27
            if (r0 <= r1) goto L_0x0337
            r0 = r30
            byte[] r0 = r0.downloadBuffer     // Catch:{ Exception -> 0x03e2 }
            r27 = r0
            r28 = 0
            r0 = r27
            r1 = r28
            r2 = r23
            r10.write(r0, r1, r2)     // Catch:{ Exception -> 0x03e2 }
            int r22 = r22 + r23
            if (r33 == 0) goto L_0x03b6
            r27 = 0
            r28 = 1
            r0 = r30
            r1 = r23
            r2 = r27
            r3 = r28
            r0.updateProgress(r1, r2, r3)     // Catch:{ Exception -> 0x03e2 }
        L_0x03b6:
            r5 = 0
            r23 = r5
            goto L_0x0337
        L_0x03bb:
            r6 = move-exception
            java.lang.String r27 = "DownloadManager"
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "Error: "
            java.lang.StringBuilder r28 = r28.append(r29)
            r0 = r28
            java.lang.StringBuilder r28 = r0.append(r6)
            java.lang.String r28 = r28.toString()
            android.util.Log.d(r27, r28)
            r27 = 0
            r0 = r27
            r1 = r30
            r1.currentDownloadingFile = r0
            r18 = -1
            goto L_0x0066
        L_0x03e2:
            r27 = move-exception
            goto L_0x037b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.wardrumstudios.utils.WarDownloader.downloadFile(java.lang.String, int, boolean):int");
    }

    /* access modifiers changed from: package-private */
    public ProgressDialog MakeProgressDialog() {
        return ProgressDialog.show(this.myWarMedia, "", this.LOADING_MESSAGE, true);
    }

    /* access modifiers changed from: package-private */
    public void ClearDownloadVals() {
        this.amountReallyDownloaded = 0;
        this.amountDownloaded = 0;
        this.timeToDownload = 0;
        this.realTimeToDownload = 0;
        this.filesToDownload = 0;
        this.filesDownloaded = 0;
        this.totalToDownload = 0;
        this.lastUpdateBytes = 0;
        this.rollIndex = -1;
    }

    /* access modifiers changed from: package-private */
    public void updateProgress(int bytes, int files, boolean isRealData) {
        int rate;
        boolean doUpdate = false;
        if (isRealData) {
            this.lastUpdateBytes += bytes;
            if (this.lastUpdateBytes > 1000000) {
                doUpdate = true;
            }
        } else {
            this.amountDownloaded += (long) bytes;
            doUpdate = true;
        }
        this.filesDownloaded += files;
        if (doUpdate) {
            if (this.rollIndex == -1) {
                this.currentRollingAmount = new long[2];
                this.currentRollingStart = new long[2];
                this.currentRollingTime = new long[2];
                this.currentRollingAmount[0] = 0;
                this.currentRollingStart[0] = this.StartTime;
                this.currentRollingAmount[1] = 0;
                this.currentRollingStart[1] = this.StartTime;
                this.currentRollingTime[0] = 0;
                this.currentRollingTime[1] = 0;
                this.rollIndex = 0;
            }
            this.amountReallyDownloaded += (long) this.lastUpdateBytes;
            this.amountDownloaded += (long) this.lastUpdateBytes;
            long[] jArr = this.currentRollingAmount;
            int i = this.rollIndex;
            jArr[i] = jArr[i] + ((long) this.lastUpdateBytes);
            long[] jArr2 = this.currentRollingTime;
            int i2 = this.rollIndex;
            jArr2[i2] = jArr2[i2] + this.timeToDownload;
            this.timeToDownload = 0;
            this.lastUpdateBytes = 0;
            String eta = "";
            this.timeToDownload = System.currentTimeMillis() - this.currentRollingStart[1 - this.rollIndex];
            long downsum = this.currentRollingAmount[0] + this.currentRollingAmount[1];
            if (this.timeToDownload > 0 && this.currentRollingAmount[0] > 0 && this.currentRollingAmount[1] > 0 && (rate = (int) (downsum / this.timeToDownload)) > 0) {
                int time = ((int) (this.totalToDownload - (this.amountDownloaded ))) / rate;
                int minutes = time / 60;
                eta = this.ESTIMATED_COMPLETION_TEXT + " " + minutes + " " + this.MINUTES_TEXT + " " + (minutes > 5 ? "" : (time - (minutes * 60)) + " " + this.SECONDS_TEXT);
            }
            if (this.currentRollingAmount[this.rollIndex] > 50000000) {
                this.rollIndex = 1 - this.rollIndex;
                this.currentRollingStart[this.rollIndex] = System.currentTimeMillis();
                this.currentRollingAmount[this.rollIndex] = 0;
                this.currentRollingTime[this.rollIndex] = 0;
            }
            String mess = this.DOWNLOADING_FILES_TEXT + " (" + (this.filesToDownload - this.filesDownloaded) + " " + this.FILES_TEXT + " / " + ((this.totalToDownload - (this.amountDownloaded))) + " MB) \n" + this.PLEASE_WAIT_TEXT;
            if (this.timeToDownload > 0) {
                mess = mess + "" + (downsum / this.timeToDownload) + " KB/s " + eta;
            }
            this.notifyMessage = "" + (this.filesToDownload - this.filesDownloaded) + " " + this.FILES_TEXT + " / " + ((this.totalToDownload - (this.amountDownloaded)) ) + " MB ";
            if (this.timeToDownload > 0) {
                this.notifyMessage += "" + (downsum / this.timeToDownload) + " KB/s " + eta;
            }
            ChangeMessage(mess);
            if (this.DownloadProgress != null) {
                this.DownloadProgress.setProgress(((int) this.amountDownloaded) / 1024);
            }
            if (this.mBuilder != null) {
                this.mBuilder.setProgress((int) this.totalToDownload, ((int) this.amountDownloaded) / 1024, false);
                this.mNotifyManager.notify(0, this.mBuilder.build());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean GetCompressionType() {
        if (this.CompressionType == -1) {
            if (this.myWarMedia.glExtensions == "") {
                System.out.println("No glextensions - download all");
                return false;
            } else if (this.compressionScheme == 0) {
                if (this.myWarMedia.glExtensions.contains("GL_EXT_texture_compression_dxt1")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_AMD_compressed_ATC_texture")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_IMG_texture_compression_pvrtc")) {
                    this.CompressionType = 3;
                } else {
                    this.CompressionType = 0;
                }
            } else if (this.compressionScheme == 1) {
                if (this.myWarMedia.glExtensions.contains("GL_EXT_texture_compression_dxt1")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_AMD_compressed_ATC_texture")) {
                    this.CompressionType = 2;
                } else if (this.myWarMedia.glExtensions.contains("GL_IMG_texture_compression_pvrtc")) {
                    this.CompressionType = 3;
                } else {
                    this.CompressionType = 0;
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean CheckToSkipTexture(String filename) {
        if (this.CompressionType == -1) {
            if (this.myWarMedia.glExtensions == null || this.myWarMedia.glExtensions == "") {
                System.out.println("No glextensions - download all");
                return false;
            } else if (this.compressionScheme == 0) {
                if (this.myWarMedia.glExtensions.contains("GL_EXT_texture_compression_dxt1")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_AMD_compressed_ATC_texture")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_IMG_texture_compression_pvrtc")) {
                    this.CompressionType = 3;
                } else {
                    this.CompressionType = 0;
                }
            } else if (this.compressionScheme == 1) {
                if (this.myWarMedia.glExtensions.contains("GL_EXT_texture_compression_dxt1")) {
                    this.CompressionType = 1;
                } else if (this.myWarMedia.glExtensions.contains("GL_AMD_compressed_ATC_texture")) {
                    this.CompressionType = 2;
                } else if (this.myWarMedia.glExtensions.contains("GL_IMG_texture_compression_pvrtc")) {
                    this.CompressionType = 3;
                } else {
                    this.CompressionType = 0;
                }
            }
        }
        if (filename.contains("DXT")) {
            if (this.CompressionType == 1) {
                return false;
            }
            return true;
        } else if (this.compressionScheme != 1 || !filename.contains("ATC")) {
            if (filename.contains("PVR")) {
                if (this.CompressionType == 3) {
                    return false;
                }
                return true;
            } else if (this.compressionScheme != 0 || !filename.contains("ETC")) {
                if (this.compressionScheme != 1 || !filename.contains("UNC")) {
                    return false;
                }
                if (this.CompressionType == 0) {
                    return false;
                }
                return true;
            } else if (this.CompressionType == 0) {
                return false;
            } else {
                return true;
            }
        } else if (this.CompressionType == 2) {
            return false;
        } else {
            return true;
        }
    }

    class CDownloadFileList {
        boolean downloaded;
        int downloadsize;
        String filename;
        int size;
        boolean unzipped;

        CDownloadFileList() {
        }
    }

    /* access modifiers changed from: package-private */
    public boolean ReadFilelist() {
        boolean z;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.myWarMedia.getAssets().open("filelist.txt")));
            int lineCount = 0;
            int numFilesToDownload = 0;
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (lineCount == 0) {
                    this.filesToDownload = Integer.decode(line).intValue();
                    this.numOfDownloadFiles = Integer.decode(line).intValue();
                    this.downloadFileList = new CDownloadFileList[this.numOfDownloadFiles];
                } else if (lineCount == 1) {
                    this.totalToDownload = (long) Integer.decode(line).intValue();
                } else if (lineCount - 2 > this.filesToDownload) {
                    System.out.println("Error lineCount " + lineCount + " filesToDownload " + this.filesToDownload);
                    break;
                } else {
                    this.downloadFileList[numFilesToDownload] = new CDownloadFileList();
                    this.downloadFileList[numFilesToDownload].filename = line.substring(11);
                    this.downloadFileList[numFilesToDownload].size = Integer.decode(line.substring(0, 10).trim()).intValue();
                    if (!CheckToSkipTexture(this.downloadFileList[numFilesToDownload].filename)) {
                        this.downloadFileList[numFilesToDownload].downloaded = false;
                        this.downloadFileList[numFilesToDownload].downloadsize = 0;
                        CDownloadFileList cDownloadFileList = this.downloadFileList[numFilesToDownload];
                        if (!this.downloadFileList[numFilesToDownload].filename.contains(".zip")) {
                            z = true;
                        } else {
                            z = false;
                        }
                        cDownloadFileList.unzipped = z;
                        numFilesToDownload++;
                    } else {
                        this.numOfDownloadFiles--;
                        this.totalToDownload -= (long) (this.downloadFileList[numFilesToDownload].size / 1024);
                    }
                }
                lineCount++;
            }
            input.close();
            return true;
        } catch (Exception ex) {
            System.out.println("ERROR in ReadFilelist " + ex.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void UpdateDownloadList() {
        int totalSize = 0;
        int numFiles = 0;
        for (int filenum = 0; filenum < this.numOfDownloadFiles; filenum++) {
            String to = this.myWarMedia.baseDirectory + "/" + this.downloadFileList[filenum].filename;
            try {
                if (!CheckToSkipTexture(to)) {
                    File file = new File(to);
                    if (!file.exists() || ((long) this.downloadFileList[filenum].size) != file.length()) {
                        totalSize += this.downloadFileList[filenum].size;
                        numFiles++;
                    } else {
                        this.downloadFileList[filenum].downloaded = true;
                    }
                }
            } catch (Exception e) {
                totalSize += this.downloadFileList[filenum].size;
                numFiles++;
            }
        }
        this.filesToDownload = numFiles;
        this.totalToDownload = (long) (totalSize / 1024);
        this.filesDownloaded = 0;
    }

    /* access modifiers changed from: package-private */
    @SuppressLint("InvalidWakeLockTag")
    public boolean DownloadAllFiles() {
        final Context mContext = this.myWarMedia;
        final WarMedia fWarMedia = this.myWarMedia;
        PowerManager.WakeLock wakeLock = null;
        this.myWarMedia.handler.post(new Runnable() {
            public void run() {
                WarDownloader.this.mNotifyManager = (NotificationManager) WarDownloader.this.myWarMedia.getSystemService(Context.NOTIFICATION_SERVICE);
                WarDownloader.this.mBuilder = new NotificationCompat.Builder(WarDownloader.this.myWarMedia);
                WarDownloader.this.mBuilder.setContentTitle(WarDownloader.this.downloadTitle).setContentText(WarDownloader.this.DOWNLOADING_FILES_TEXT + ", " + WarDownloader.this.PLEASE_WAIT_TEXT).setSmallIcon(WarDownloader.this.myWarMedia.appStatusIcon);
                Intent notificationIntent = fWarMedia.appIntent;
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                WarDownloader.this.mBuilder.setContentIntent(PendingIntent.getActivity(WarDownloader.this.myWarMedia, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                WarDownloader.this.DownloadProgress = new ProgressDialog(mContext);
                WarDownloader.this.DownloadProgress.setProgressStyle(1);
                WarDownloader.this.DownloadProgress.setMessage(WarDownloader.this.DOWNLOADING_FILES_TEXT + ", " + WarDownloader.this.PLEASE_WAIT_TEXT);
                WarDownloader.this.DownloadProgress.setButton(-1, WarDownloader.this.CANCEL_BUTTON, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface i, int a) {
                        if (WarDownloader.this.currentDownloadingFile != null) {
                            new File(WarDownloader.this.currentDownloadingFile).delete();
                        }
                        WarDownloader.this.myWarMedia.finish();
                    }
                });
                WarDownloader.this.DownloadProgress.setCancelable(false);
                WarDownloader.this.DownloadProgress.setCanceledOnTouchOutside(false);
                WarDownloader.this.DownloadProgress.show();
                WarDownloader.this.ProgressInited = true;
            }
        });
        int ret = -1;
        boolean Result = true;
        if (!this.DoDownloadTest) {
            try {
                this.StartTime = System.currentTimeMillis();
                wakeLock = ((PowerManager) mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, "OSWrapper");
                wakeLock.acquire();
                if (ReadFilelist()) {
                    UpdateDownloadList();
                    do {
                        try {
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ret = -1;
                        }
                    } while (!this.ProgressInited);
                    ChangeMessage(this.DOWNLOADING_FILES_TEXT + ". " + this.PLEASE_WAIT_TEXT);
                    if (this.totalToDownload > 5) {
                        ChangeMessage(this.DOWNLOADING_FILES_TEXT + " (" + this.filesToDownload + " " + this.FILES_TEXT + " / " + (this.totalToDownload ) + " MB). " + this.PLEASE_WAIT_TEXT);
                        if (this.DownloadProgress != null) {
                            this.DownloadProgress.setMax((int) this.totalToDownload);
                        }
                        if (this.mBuilder != null) {
                            this.mBuilder.setContentText(this.DOWNLOADING_FILES_TEXT + " (" + this.filesToDownload + " " + this.FILES_TEXT + " / " + (this.totalToDownload ) + " MB). " + this.PLEASE_WAIT_TEXT);
                            this.mNotifyManager.notify(0, this.mBuilder.build());
                        }
                    }
                    for (int filenum = 0; filenum < this.numOfDownloadFiles; filenum++) {
                        if (!this.downloadFileList[filenum].downloaded) {
                            long curTime = System.currentTimeMillis();
                            ret = 0;
                            int count = 0;
                            boolean doUnzipFile = true;
                            while (true) {
                                int count2 = count;
                                if (ret == this.downloadFileList[filenum].size) {
                                    int i = count2;
                                    break;
                                }
                                count = count2 + 1;
                                if (count2 > 3) {
                                    System.out.println("Failed Downloading 3 times read " + ret + " expected " + this.downloadFileList[filenum].size);
                                    if (wakeLock == null) {
                                        return false;
                                    }
                                    wakeLock.release();
                                    return false;
                                }
                                if (count > 0) {
                                    System.out.println("Download " + this.downloadFileList[filenum].filename + " failed size " + ret + " expected " + this.downloadFileList[filenum].size);
                                }
                                if (CheckToSkipTexture(this.downloadFileList[filenum].filename)) {
                                    ret = -3;
                                    break;
                                }
                                ret = downloadFile(this.downloadFileList[filenum].filename, this.downloadFileList[filenum].size, true);
                                if (ret == -2) {
                                    System.out.println("Download " + this.downloadFileList[filenum].filename + " already exists");
                                    doUnzipFile = false;
                                    ret = 0;
                                    break;
                                }
                            }
                            long timeDiff = System.currentTimeMillis() - curTime;
                            if (timeDiff == 0) {
                                timeDiff = 1;
                            }
                            if (ret == 0 || ret == -3) {
                                ret = this.downloadFileList[filenum].size;
                                updateProgress(ret, 1, false);
                            } else {
                                this.realTimeToDownload += timeDiff;
                                this.timeToDownload += this.downloadTime;
                                this.amountReallyDownloaded += (long) ret;
                                updateProgress(0, 1, false);
                            }
                            if (ret == -3) {
                                this.downloadFileList[filenum].unzipped = true;
                                this.downloadFileList[filenum].downloaded = true;
                            }
                            if (ret != -3) {
                                if (!doUnzipFile) {
                                    this.downloadFileList[filenum].unzipped = true;
                                }
                                this.downloadFileList[filenum].downloaded = true;
                            }
                            if (ret == -1) {
                                break;
                            }
                        }
                    }
                    if (wakeLock != null) {
                        wakeLock.release();
                    }
                    Result = ret != -1;
                } else if (wakeLock == null) {
                    return false;
                } else {
                    wakeLock.release();
                    return false;
                }
            } catch (Exception ex2) {
                try {
                    ex2.printStackTrace();
                    ret = -1;
                    if (wakeLock != null) {
                        wakeLock.release();
                    }
                } catch (Throwable th) {
                    if (wakeLock != null) {
                        wakeLock.release();
                    }
                    throw th;
                }
            }
        }
        if (this.UseFTP) {
            try {
                this.ftpClient.disconnect();
                this.ftpClient = null;
            } catch (Exception e) {
            }
        }
        if (Result) {
            if (!VerifyData()) {
                this.DownloadFailedMessage = 3;
                return false;
            }
            this.myWarMedia.SetConfigSetting("DownloadCheckVersion", "Downloadv9");
            ChangeMessage("Done Downloading");
        }
        this.downloadBuffer = null;
        return Result;
    }

    /* access modifiers changed from: package-private */
    public void ChangeMessage(String message) {
        final String mess = message;
        if (this.myWarMedia.paused && mess == "Done Downloading") {
            this.myWarMedia.runOnUiThread(new Runnable() {
                public void run() {
                    WarDownloader.this.mBuilder.setContentText(WarDownloader.this.DOWNLOAD_COMPLETE);
                    WarDownloader.this.mBuilder.setProgress(0, 0, false);
                    WarDownloader.this.mNotifyManager.cancel(0);
                    WarDownloader.this.mNotifyManager.notify(0, WarDownloader.this.mBuilder.build());
                }
            });
            while (this.myWarMedia.paused) {
                this.myWarMedia.mSleep(1000);
            }
        }
        this.myWarMedia.handler.post(new Runnable() {
            public void run() {
                String newMess;
                if (mess == "Done Downloading") {
                    if (WarDownloader.this.DownloadProgress != null) {
                        WarDownloader.this.DownloadProgress.dismiss();
                    }
                    WarDownloader.this.DownloadProgress = null;
                    long elapsed = System.currentTimeMillis() - WarDownloader.this.StartTime;
                    if (elapsed > 300000) {
                        newMess = "" + "" + (elapsed / 60000) + " " + WarDownloader.this.MINUTES_TEXT;
                    } else {
                        newMess = "" + "" + (elapsed / 1000) + " " + WarDownloader.this.SECONDS_TEXT;
                    }
                    WarDownloader.this.mBuilder.setContentText(WarDownloader.this.DOWNLOAD_COMPLETE + " (" + newMess + ")");
                    new AlertDialog.Builder(WarDownloader.this.myWarMedia).setMessage(WarDownloader.this.DOWNLOAD_COMPLETE + " (" + newMess + ")").setPositiveButton(WarDownloader.this.NEXT_BUTTON, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface i, int a) {
                            WarDownloader.this.myWarMedia.runOnUiThread(new Runnable() {
                                public void run() {
                                    WarDownloader.this.ClearSplash();
                                    WarDownloader.this.myWarMedia.DoResumeEvent();
                                }
                            });
                        }
                    }).setNegativeButton(WarDownloader.this.EXIT_BUTTON, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface i, int a) {
                            WarDownloader.this.myWarMedia.finish();
                        }
                    }).setCancelable(false).show();
                } else if (mess == "Done Downloading Language") {
                    if (WarDownloader.this.DownloadProgress != null) {
                        WarDownloader.this.DownloadProgress.dismiss();
                    }
                    WarDownloader.this.DownloadProgress = null;
                } else {
                    if (WarDownloader.this.DownloadProgress != null) {
                        WarDownloader.this.DownloadProgress.setMessage(mess);
                    }
                    if (WarDownloader.this.mBuilder != null) {
                        WarDownloader.this.mBuilder.setContentText(WarDownloader.this.notifyMessage);
                        WarDownloader.this.mNotifyManager.notify(0, WarDownloader.this.mBuilder.build());
                    }
                }
            }
        });
    }
}
