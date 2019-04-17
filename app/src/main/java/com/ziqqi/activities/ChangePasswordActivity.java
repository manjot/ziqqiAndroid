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
import com.ziqqi.databinding.ActivityChangePasswordBinding;
import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.forgotpasswordmodel.ForgotPasswordResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ChangePasswordViewModel;
import com.ziqqi.viewmodel.HelpCenterViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    ChangePasswordViewModel viewModel;
    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

    }

    private void VerifyOtp(String email, String otp, String newPassword) {
        if (ConnectivityHelper.isConnectedToNetwork(ChangePasswordActivity.this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rlMain.setVisibility(View.GONE);

            viewModel.changePassword(email, otp, newPassword);
            viewModel.changePasswordResponse().observe(this, new Observer<ForgotPasswordResponse>() {
                @Override
                public void onChanged(@Nullable ForgotPasswordResponse verifyOtpResponse) {
                    if (!verifyOtpResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.rlMain.setVisibility(View.VISIBLE);
                        Utils.ShowToast(ChangePasswordActivity.this, verifyOtpResponse.getMessage());
                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        finishAffinity();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.rlMain.setVisibility(View.VISIBLE);
                        Utils.ShowToast(ChangePasswordActivity.this, verifyOtpResponse.getMessage());
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
