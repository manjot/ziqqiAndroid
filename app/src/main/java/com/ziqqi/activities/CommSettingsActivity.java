package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityCommSettingsBinding;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.CommSettingsViewModel;
import com.ziqqi.viewmodel.CommunicationPrefViewModel;

import java.util.Locale;

public class CommSettingsActivity extends AppCompatActivity {

    CommSettingsViewModel commSettingsViewModel;
    ActivityCommSettingsBinding binding;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comm_settings);
        commSettingsViewModel = ViewModelProviders.of(this).get(CommSettingsViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null) {
            value = getIntent().getExtras().getString("value");

            if (value != null){
                if (value.equalsIgnoreCase("1")){
                    binding.tvToolbarTitle.setText(getString(R.string.recommendations));
                }else if (value.equalsIgnoreCase("2")){
                    binding.tvToolbarTitle.setText(getString(R.string.promotional));
                }else if (value.equalsIgnoreCase("3")){
                    binding.tvToolbarTitle.setText(getString(R.string.newsletters));
                }else if (value.equalsIgnoreCase("4")){
                    binding.tvToolbarTitle.setText(getString(R.string.alerts));
                }
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
