package com.nvidia.devtech;

import android.app.Activity;
import java.util.HashMap;

public class NvUtil {
    private static NvUtil instance = new NvUtil();
    private Activity activity = null;
    private HashMap<String, String> appLocalValues = new HashMap<>();

    private NvUtil() {
    }

    public void setActivity(Activity activity2) {
        this.activity = activity2;
    }

    public static NvUtil getInstance() {
        return instance;
    }

    public boolean hasAppLocalValue(String key) {
        return this.appLocalValues.containsKey(key);
    }

    public String getAppLocalValue(String key) {
        return this.appLocalValues.get(key);
    }

    public void setAppLocalValue(String key, String value) {
        this.appLocalValues.put(key, value);
    }

    public String getParameter(String paramName) {
        return this.activity.getIntent().getStringExtra(paramName);
    }
}
