package com.ziqqi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ziqqi.R;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

public class SecondSplashActivity extends Activity {
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_splash);
        ivSplash = findViewById(R.id.iv_splash);

/*
        Bitmap resultBmp = BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), R.drawable.splash));
*/

        Glide.with(this).load(R.drawable.splash).into(ivSplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceManager.getBoolValue(Constants.LANG_SELECTED)) {
                    startActivity(new Intent(SecondSplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SecondSplashActivity.this, SelectYourLanguageActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
