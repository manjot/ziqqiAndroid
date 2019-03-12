package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.SelectLanguageAdapter;
import com.ziqqi.model.homecategorymodel.BestsellerProduct;
import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.model.languagemodel.Payload;
import com.ziqqi.utils.BlurBuilder;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SelectLanguageViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectYourLanguageActivity extends AppCompatActivity {
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    RecyclerView rvSelectLanguage;
    SelectLanguageViewModel viewModel;
    List<Payload> payloadList = new ArrayList<>();
    SelectLanguageAdapter adapter;
    ProgressBar progressBar;
    CardView mainLayout;
    TextView tvApply;
    OnItemClickListener listener;
    ImageView ivBgImage;
    TextView tvSelectLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_your_language);

        viewModel = ViewModelProviders.of(this).get(SelectLanguageViewModel.class);
        viewModel.init();

        Bitmap resultBmp = BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), R.drawable.splash));

        tvApply = findViewById(R.id.tv_apply);


        rvSelectLanguage = findViewById(R.id.rv_select_language);
        mainLayout = findViewById(R.id.main_layout);
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                setLocale(id);
            }

            @Override
            public void onItemClick(BestsellerProduct bestsellerProduct, String type) {

            }
        };

        adapter = new SelectLanguageAdapter(payloadList, this, listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSelectLanguage.setLayoutManager(mLayoutManager);
        rvSelectLanguage.setItemAnimator(new DefaultItemAnimator());
        rvSelectLanguage.setAdapter(adapter);

        progressBar = findViewById(R.id.progress_bar);
        tvSelectLanguage = findViewById(R.id.tv_select_language);
        ivBgImage = findViewById(R.id.iv_bg_image);
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        setUpFonts();

        Glide.with(this).load(resultBmp).into(ivBgImage);

        viewModel.getLanguageData().observe(this, new Observer<LanguageModel>() {
            @Override
            public void onChanged(@Nullable LanguageModel languageModel) {
                if (!languageModel.getError()) {
                    payloadList.addAll(languageModel.getPayload());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                } else {
                    Utils.ShowToast(SelectYourLanguageActivity.this, languageModel.getMessage());
                }
            }
        });
    }

    private void setUpFonts() {
        Typeface medium = FontCache.get(getResources().getString(R.string.medium), this);
        tvSelectLanguage.setTypeface(medium);
        tvApply.setTypeface(medium);
    }


    public void setLocale(String localeName) {
        tvApply.setVisibility(View.VISIBLE);

      /*  if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            tvApply.setVisibility(View.VISIBLE);
            PreferenceManager.setStringValue(Constants.LANG, localeName);
        *//*    Intent refresh = new Intent(this, SelectYourLanguageActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
            finish();*//*
        } else {
            Toast.makeText(this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void onClickApply(View view) {
        PreferenceManager.setBoolValue(Constants.LANG_SELECTED, true);
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
