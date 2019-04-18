package com.ziqqi.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ziqqi.R;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

import java.util.Locale;

import static com.ziqqi.utils.Utils.setWindowFlag;

public class SecondSplashActivity extends Activity {
    ImageView ivSplash, ivCircle;
    Animation animSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_second_splash);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        ivSplash = findViewById(R.id.iv_splash);
        ivCircle = findViewById(R.id.iv_circle);

        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.left_to_rigth);

        ivCircle.setAnimation(animSlide);

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
