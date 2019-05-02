package com.ziqqi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.ziqqi.R;

public class ProductZoomActivity extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_zoom);

        image = findViewById(R.id.image);
        image.setOnTouchListener(new ImageMatrixTouchHandler(ProductZoomActivity.this));
    }
}
