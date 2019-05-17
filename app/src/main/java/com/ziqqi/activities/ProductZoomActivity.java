package com.ziqqi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jsibbold.zoomage.ZoomageView;
import com.ziqqi.R;

public class ProductZoomActivity extends AppCompatActivity {

    ImageView  iv_close;
    String strUrl;
    ZoomageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_zoom);

        image = findViewById(R.id.image);
        iv_close = findViewById(R.id.iv_close);

        if (getIntent().getExtras() != null){
            strUrl = getIntent().getStringExtra("pic");
        }

        Glide.with(ProductZoomActivity.this).load(strUrl).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(image);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
