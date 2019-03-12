package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ziqqi.R;
import com.ziqqi.adapters.CountryCodeAdapter;
import com.ziqqi.databinding.ActivitySignUpBinding;
import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    SignUpViewModel signUpViewModel;
    ActivitySignUpBinding binding;
    int[] flags = {R.drawable.flag_soloman_islands, R.drawable.flag_india};
    String[] code = {"+252", "+91"};
    CountryCodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.executePendingBindings();
        binding.setSignUpViewModel(signUpViewModel);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        initViews();
        setUpFonts();

        adapter = new CountryCodeAdapter(this, flags, code);
        binding.ccp.setAdapter(adapter);
    }

    private void setUpFonts() {
        Typeface regular = FontCache.get(getResources().getString(R.string.regular), this);
        Typeface medium = FontCache.get(getResources().getString(R.string.medium), this);
        binding.tilPassword.setTypeface(regular);
        binding.tilEmail.setTypeface(regular);
        binding.tilFirstName.setTypeface(regular);
        binding.tilLastName.setTypeface(regular);
        binding.tilPhone.setTypeface(regular);
        binding.btnRegister.setTypeface(medium);
        binding.tvTerms.setTypeface(regular);
        binding.rbFemale.setTypeface(regular);
        binding.rbMale.setTypeface(regular);
    }

    private void initViews() {

        String text = getResources().getString(R.string.by_clicking);
        String terms = getResources().getString(R.string.terms);
        String pp = getResources().getString(R.string.pp);
        String or = getResources().getString(R.string.or);
        binding.tvTerms.setText(text);
        binding.tvTerms.append(Utils.getColoredString(terms, ContextCompat.getColor(this, R.color.colorPrimary)));
        binding.tvTerms.append(or);
        binding.tvTerms.append(Utils.getColoredString(pp, ContextCompat.getColor(this, R.color.colorPrimary)));

    }

    public void onClickShowPassword(View view) {
        if (binding.tvShowPassword.getText().toString().equals(getResources().getString(R.string.show_pass))) {
            binding.etPassword.setTransformationMethod(null);
            binding.tvShowPassword.setText(getResources().getString(R.string.hide_pass));
        } else {
            binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
            binding.tvShowPassword.setText(getResources().getString(R.string.show_pass));
        }
    }

    public void onClickRegister(View view) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            if (signUpViewModel.isValid()) {
                signUpViewModel.init();
                signUpViewModel.getSignUpResponse().observe(this, new Observer<SignUpResponse>() {
                    @Override
                    public void onChanged(@Nullable SignUpResponse signUpResponse) {
                        if (!signUpResponse.getError()) {
                            VerifyOtp(signUpResponse.getOtpdetails().getCustomerId(), signUpResponse.getOtpdetails().getOtp());
                            PreferenceManager.setStringValue(Constants.FIRST_NAME, signUpResponse.getPayload().getFirstName());
                            PreferenceManager.setStringValue(Constants.EMAIL, signUpResponse.getPayload().getEmail());
                        } else {
                            signUpViewModel.mainLayoutVisibility.set(View.VISIBLE);
                            signUpViewModel.progressVisibility.set(View.GONE);
                            Utils.ShowToast(SignUpActivity.this, signUpResponse.getMessage());
                        }
                    }
                });
            }

        } else Utils.ShowToast(this, "No Internet Connection");
    }

    private void VerifyOtp(int customerId, int otp) {
        signUpViewModel.verifyOtp(customerId, otp);
        signUpViewModel.getVerifyOtp().observe(this, new Observer<VerifyOtpResponse>() {
            @Override
            public void onChanged(@Nullable VerifyOtpResponse verifyOtpResponse) {
                if (!verifyOtpResponse.getError()) {
                    signUpViewModel.mainLayoutVisibility.set(View.VISIBLE);
                    signUpViewModel.progressVisibility.set(View.GONE);
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
                    PreferenceManager.setStringValue(Constants.FIRST_NAME, verifyOtpResponse.getPayload().get(0).getFirstName());
                    PreferenceManager.setStringValue(Constants.EMAIL, verifyOtpResponse.getPayload().get(0).getEmail());
                    finishAffinity();
                } else {
                    signUpViewModel.mainLayoutVisibility.set(View.VISIBLE);
                    signUpViewModel.progressVisibility.set(View.GONE);
                    Utils.ShowToast(SignUpActivity.this, verifyOtpResponse.getMessage());
                }
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDelayedKeyboard(final View et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }
}
