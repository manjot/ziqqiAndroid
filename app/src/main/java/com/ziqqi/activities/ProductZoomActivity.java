package com.ziqqi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ziqqi.R;
import com.ziqqi.utils.ZoomableRelativeLayout;

public class ProductZoomActivity extends AppCompatActivity {

    ImageView image, iv_close;
    ZoomableRelativeLayout zoomableRelativeLayout;
    String strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_zoom);

        image = findViewById(R.id.image);
        iv_close = findViewById(R.id.iv_close);
        final ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, new OnPinchListener());
        zoomableRelativeLayout = findViewById(R.id.zoom);

        if (getIntent().getExtras() != null){
            strUrl = getIntent().getStringExtra("pic");
        }

        Glide.with(ProductZoomActivity.this).load(strUrl).apply(RequestOptions.placeholderOf(R.drawable.place_holder)).into(image);

        zoomableRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float startingSpan;
        float endSpan;
        float startFocusX;
        float startFocusY;


        public boolean onScaleBegin(ScaleGestureDetector detector) {
            startingSpan = detector.getCurrentSpan();
            startFocusX = detector.getFocusX();
            startFocusY = detector.getFocusY();
            return true;
        }


        public boolean onScale(ScaleGestureDetector detector) {
            zoomableRelativeLayout.scale(detector.getCurrentSpan()/startingSpan, startFocusX, startFocusY);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
            zoomableRelativeLayout.restore();
        }
    }
}
