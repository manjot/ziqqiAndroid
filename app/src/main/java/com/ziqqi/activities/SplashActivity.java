package com.ziqqi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        PreferenceManager.setStringValue(Constants.GUEST_ID, android_id);

        Intent intent = new Intent(this, SecondSplashActivity.class);
        startActivity(intent);
        finish();
    }
}
