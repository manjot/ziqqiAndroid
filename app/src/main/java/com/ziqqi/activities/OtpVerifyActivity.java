package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityOtpVerifyBinding;
import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.OtpViewModel;
import com.ziqqi.viewmodel.SignUpViewModel;

public class OtpVerifyActivity extends AppCompatActivity {

    ActivityOtpVerifyBinding binding;
    OtpViewModel viewModel;
    String strCId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verify);
        viewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            strCId = getIntent().getStringExtra("cId");
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOtp(Integer.parseInt(strCId), Integer.parseInt(binding.etEnter.getText().toString()));
            }
        });
    }

    public void recivedSms(String message) {
        try {
            binding.etEnter.setText(message);
        } catch (Exception e) {

        }
    }

    private void VerifyOtp(int customerId, int otp) {
        if (ConnectivityHelper.isConnectedToNetwork(OtpVerifyActivity.this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rlMain.setVisibility(View.GONE);

            viewModel.verifyOtp(customerId, otp);
            viewModel.getVerifyOtp().observe(this, new Observer<VerifyOtpResponse>() {
                @Override
                public void onChanged(@Nullable VerifyOtpResponse verifyOtpResponse) {
                    if (!verifyOtpResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.rlMain.setVisibility(View.VISIBLE);
                        startActivity(new Intent(OtpVerifyActivity.this, MainActivity.class));
                        PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                        PreferenceManager.setStringValue(Constants.FACEBOOK_OR_GMAIL, "f or g");
                        PreferenceManager.setStringValue(Constants.AUTH_TOKEN, verifyOtpResponse.getPayload().get(0).getAuthToken());
                        PreferenceManager.setStringValue(Constants.FIRST_NAME, verifyOtpResponse.getPayload().get(0).getFirstName());
                        PreferenceManager.setStringValue(Constants.EMAIL, verifyOtpResponse.getPayload().get(0).getEmail());
                        finishAffinity();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.rlMain.setVisibility(View.VISIBLE);
                        Utils.ShowToast(OtpVerifyActivity.this, verifyOtpResponse.getMessage());
                    }
                }
            });
        }else{
            Utils.ShowToast(this, "No Internet Connection");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
