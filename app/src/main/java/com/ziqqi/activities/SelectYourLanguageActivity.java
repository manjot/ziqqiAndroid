package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    String currentLanguage = "";
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
    RelativeLayout rl_country;
    TextView tv_country_code, tv_country;
    boolean isCountrySelected = false;
    String strLangName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_your_language);

        viewModel = ViewModelProviders.of(this).get(SelectLanguageViewModel.class);
        viewModel.init();

        Bitmap resultBmp = BlurBuilder.blur(this, BitmapFactory.decodeResource(getResources(), R.drawable.splash));

        tvApply = findViewById(R.id.tv_apply);
        rl_country = findViewById(R.id.rl_country);
        tv_country_code = findViewById(R.id.tv_country_code);
        tv_country = findViewById(R.id.tv_county);



/*        rl_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCountrySelected){
                    isCountrySelected = true;
                    rl_country.setBackground(getResources().getDrawable(R.drawable.selected_bg) );
                    tv_country_code.setBackground(getResources().getDrawable(R.drawable.black_circle));
                    tv_country_code.setTextColor(getResources().getColor(R.color.colorWhite));
                }else{
                    isCountrySelected = false;
                    rl_country.setBackground(getResources().getDrawable(R.drawable.white_bg_ripple) );
                    tv_country_code.setBackground(getResources().getDrawable(R.drawable.grey_circle));
                    tv_country_code.setTextColor(getResources().getColor(R.color.colorBlack));
                }

            }
        });*/


        rvSelectLanguage = findViewById(R.id.rv_select_language);
        mainLayout = findViewById(R.id.main_layout);
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(String id, String type) {
                if (id.equalsIgnoreCase("English")) {
                    currentLanguage = "en";
                    strLangName = id;
                } else if (id.equalsIgnoreCase("Somali")) {
                    currentLanguage = "so";
                    strLangName = id;
                }
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
        /*myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);*/
    }

    public void onClickApply(View view) {

        PreferenceManager.setBoolValue(Constants.LANG_SELECTED, true);
        PreferenceManager.setStringValue(Constants.LANG, currentLanguage);
        PreferenceManager.setStringValue(Constants.LANG_NAME, strLangName);
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();

    }
}
