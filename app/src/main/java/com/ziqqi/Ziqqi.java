package com.ziqqi;

import android.app.Application;
import com.ziqqi.utils.PreferenceManager;

public class Ziqqi extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.init(getApplicationContext());
    }
}
