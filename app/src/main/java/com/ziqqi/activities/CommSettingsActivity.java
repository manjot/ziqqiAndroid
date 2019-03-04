package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityCommSettingsBinding;
import com.ziqqi.viewmodel.CommSettingsViewModel;
import com.ziqqi.viewmodel.CommunicationPrefViewModel;

public class CommSettingsActivity extends AppCompatActivity {

    CommSettingsViewModel commSettingsViewModel;
    ActivityCommSettingsBinding binding;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comm_settings);
        commSettingsViewModel = ViewModelProviders.of(this).get(CommSettingsViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null) {
            value = getIntent().getExtras().getString("value");

            if (value != null){
                if (value.equalsIgnoreCase("1")){
                    binding.tvToolbarTitle.setText("Recommendation");
                }else if (value.equalsIgnoreCase("2")){
                    binding.tvToolbarTitle.setText("Promotional");
                }else if (value.equalsIgnoreCase("3")){
                    binding.tvToolbarTitle.setText("Newsletters");
                }else if (value.equalsIgnoreCase("4")){
                    binding.tvToolbarTitle.setText("Alerts");
                }
            }
        }


    }
}
