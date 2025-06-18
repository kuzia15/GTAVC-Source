package com.wardrumstudios.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.nvidia.devtech.NvEventQueueActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class WarBase extends NvEventQueueActivity {
    private static final String TAG = "WarBase";
    public boolean FinalRelease = false;
    public boolean GameServiceLog = false;
    public int MultiplayerVersion = -1;
    private ArrayList<WarActivityLifecycleListener> mLifecycleListeners;
    protected UsbManager mUsbManager;
    protected BroadcastReceiver mUsbReceiver;

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state) || "mounted_ro".equals(state)) {
            return true;
        }
        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        this.mLifecycleListeners = new ArrayList<>();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 12) {
            this.mUsbManager = (UsbManager) getSystemService("usb");
            CreateUSBReceiver();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onStart();
        }
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onStop();
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.e(TAG, "OnResume -> Intent: " + intent.toString());
        if (Build.VERSION.SDK_INT >= 12) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
            if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                Log.e(TAG, "OnResume -> ACTION_USB_DEVICE_ATTACHED " + device.toString());
                USBDeviceAttached(device, device.getDeviceName());
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                Log.e(TAG, "OnResume -> ACTION_USB_DEVICE_DETACHED " + device.toString());
                USBDeviceDetached(device, device.getDeviceName());
            }
        }
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onResume();
        }
        super.onResume();
    }

    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= 12) {
            DestroyUSBReceiver();
        }
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onDestroy();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int request, int response, Intent data) {
        Iterator<WarActivityLifecycleListener> it = this.mLifecycleListeners.iterator();
        while (it.hasNext()) {
            it.next().onActivityResult(request, response, data);
        }
        super.onActivityResult(request, response, data);
    }

    /* access modifiers changed from: package-private */
    public void CreateUSBReceiver() {
        if (Build.VERSION.SDK_INT >= 12) {
            Log.e(TAG, "Creating USB intent receiver");
            this.mUsbReceiver = new BroadcastReceiver() {
                @SuppressLint("MissingPermission")
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    System.out.println("BroadcastReceiver WarMedia Base " + action.toString());
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    try {
                        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                            Log.e(WarBase.TAG, "BroadcastReceiver -> ACTION_USB_DEVICE_ATTACHED " + device.toString());
                            WarBase.this.USBDeviceAttached(device, device.getDeviceName());
                        } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                            Log.e(WarBase.TAG, "BroadcastReceiver -> ACTION_USB_DEVICE_DETACHED " + device.toString());
                            WarBase.this.USBDeviceDetached(device, device.getDeviceName());
                        } else if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                            BluetoothDevice btDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                            Log.e(WarBase.TAG, "BroadcastReceiver ACTION_ACL_CONNECTED name " + btDevice.getName());
                            if (btDevice != null && btDevice.getName() != null && btDevice.getName().equals("GS controller")) {
                                WarBase.this.SetGamepad(btDevice.getName());///TODO TYT
                            }
                        } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                            BluetoothDevice btDevice2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                            Log.e(WarBase.TAG, "BroadcastReceiver ACTION_ACL_DISCONNECTED name " + btDevice2.getName());
                            if (btDevice2 != null && btDevice2.getName().equals("GS controller")) {
                                WarBase.this.SetGamepad("");
                            }
                        } else {
                            Log.e(WarBase.TAG, "BroadcastReceiver -> UNKNOWN ACTION : " + action.toString());
                        }
                    } catch (Exception e) {
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            filter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
            filter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
            registerReceiver(this.mUsbReceiver, filter);
            Log.e(TAG, "Receiver set up");
        }
    }

    public void DestroyUSBReceiver() {
        if (Build.VERSION.SDK_INT >= 12) {
            unregisterReceiver(this.mUsbReceiver);
        }
    }

    public void USBDeviceAttached(UsbDevice device, String name) {
    }

    public void USBDeviceDetached(UsbDevice device, String name) {
    }

    public void SetGamepad(String gamepadString) {
    }

    public void AddLifecycleListener(WarActivityLifecycleListener listener) {
        this.mLifecycleListeners.add(listener);
    }

    public String GetLeaderboardId(String id) {
        return id;
    }
}
