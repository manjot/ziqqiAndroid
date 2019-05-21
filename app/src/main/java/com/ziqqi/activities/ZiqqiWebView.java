package com.ziqqi.activities;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ziqqi.R;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Locale;

public class ZiqqiWebView extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    TextView tvTitle;
    String strType;
    ProgressDialog prDialog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziqqi_web_view);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);
        tvTitle = findViewById(R.id.tv_toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            strType = getIntent().getStringExtra("type");

            if (strType.equalsIgnoreCase("1")){
                tvTitle.setText(R.string.privacy_policy);
                if (PreferenceManager.getStringValue(Constants.LANG).equalsIgnoreCase("en")){
                    url = "http://idukaan.ae/ziqqi/privacy-policy.html";
                }else{
                    url = "http://idukaan.ae/ziqqi/Privacy-Policy-somali.html";
                }

            }else if (strType.equalsIgnoreCase("2")){
                tvTitle.setText(R.string.terms_and_condition);
                if (PreferenceManager.getStringValue(Constants.LANG).equalsIgnoreCase("en")){
                    url = "http://idukaan.ae/ziqqi/Terms-Condition.html";
                }else{
                    url = "http://idukaan.ae/ziqqi/Terms-Conditions.html";
                }
            }

            webView.setWebViewClient(new MyWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.loadUrl(url);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(ZiqqiWebView.this);
            prDialog.setMessage("Please wait ...");
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
