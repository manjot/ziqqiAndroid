package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityCommunicationPrefBinding;
import com.ziqqi.viewmodel.CommunicationPrefViewModel;
import com.ziqqi.viewmodel.MyAddressViewModel;

public class CommunicationPrefActivity extends AppCompatActivity {

    CommunicationPrefViewModel communicationPrefViewModel;
    ActivityCommunicationPrefBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_communication_pref);
        communicationPrefViewModel = ViewModelProviders.of(this).get(CommunicationPrefViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        binding.llRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommunicationPrefActivity.this, CommSettingsActivity.class)
                        .putExtra("value", "1"));
            }
        });
        binding.llPromotional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommunicationPrefActivity.this, CommSettingsActivity.class)
                        .putExtra("value", "2"));
            }
        });
        binding.llNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommunicationPrefActivity.this, CommSettingsActivity.class)
                        .putExtra("value", "3"));
            }
        });
        binding.llAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommunicationPrefActivity.this, CommSettingsActivity.class)
                        .putExtra("value", "4"));
            }
        });

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
