package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.ziqqi.R;
import com.ziqqi.adapters.CountryCodeAdapter;
import com.ziqqi.databinding.ActivitySignUpBinding;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.SignUpViewModel;

import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    SignUpViewModel signUpViewModel;
    ActivitySignUpBinding binding;
    int[] flags = {R.drawable.ic_flag_somaliland, R.drawable.flag_india};
    String[] code = {"+252", "+91"};
    CountryCodeAdapter adapter;
    int countryPosition = 0;
    String strCountryCode = "252";
    String strGender = "M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

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

        final InputFilter[] FilterArray = new InputFilter[1];

        binding.ccp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (binding.ccp.getSelectedItemPosition() == 0) {
                    FilterArray[0] = new InputFilter.LengthFilter(9);
                    countryPosition = 0;
                    strCountryCode = "252";
                    binding.etMobileNumber.setFilters(FilterArray);
                } else {
                    FilterArray[0] = new InputFilter.LengthFilter(10);
                    countryPosition = 1;
                    strCountryCode = "91";
                    binding.etMobileNumber.setFilters(FilterArray);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "M";
            }
        });

        binding.rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strGender = "F";
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryPosition == 0) {
                    if (binding.etMobileNumber.getText().toString().length() == 9) {
                        onClickRegister();
                    }
                } else if (countryPosition == 1) {
                    if (binding.etMobileNumber.getText().toString().length() == 10) {
                        onClickRegister();
                    }
                }
            }
        });
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

    public void onClickRegister() {
        Utils.hideKeyboard(this);
        if (binding.etEmail.getText().toString().equalsIgnoreCase("") || binding.etPassword.getText().toString().equalsIgnoreCase("") || binding.etMobileNumber.getText().toString().equalsIgnoreCase("") || binding.etLastName.getText().toString().equalsIgnoreCase("") || binding.etFirstName.getText().toString().equalsIgnoreCase("")) {
            Utils.ShowToast(getApplication().getApplicationContext(), "Fields can not be emplty");
        } else if (!Utils.isValidEmail(binding.etEmail.getText().toString())) {
            Utils.ShowToast(getApplication().getApplicationContext(), "Enter a valid email");
        } else {
            if (ConnectivityHelper.isConnectedToNetwork(this)) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.scroll.setVisibility(View.GONE);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("email", binding.etEmail.getText().toString());
                hashMap.put("password", binding.etPassword.getText().toString());
                hashMap.put("fname", binding.etFirstName.getText().toString());
                hashMap.put("lname", binding.etLastName.getText().toString());
                hashMap.put("phone", binding.etMobileNumber.getText().toString());
                hashMap.put("phonecode", strCountryCode);
                hashMap.put("gender", strGender);

                signUpViewModel.init(hashMap);
                signUpViewModel.getSignUpResponse().observe(this, new Observer<SignUpResponse>() {
                    @Override
                    public void onChanged(@Nullable SignUpResponse signUpResponse) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.scroll.setVisibility(View.VISIBLE);
                        if (!signUpResponse.getError()) {
                            Utils.ShowToast(SignUpActivity.this, signUpResponse.getMessage());
                            startActivity(new Intent(SignUpActivity.this, OtpVerifyActivity.class).putExtra("cId", String.valueOf(signUpResponse.getOtpdetails().getCustomerId())));
                            finish();

                        } else {
                            binding.scroll.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                            Utils.ShowToast(SignUpActivity.this, signUpResponse.getMessage());
                        }
                    }
                });

            } else {
                Utils.ShowToast(this, "No Internet Connection");
            }
        }

    }

//    private void VerifyOtp(int customerId, int otp) {
//        signUpViewModel.verifyOtp(customerId, otp);
//        signUpViewModel.getVerifyOtp().observe(this, new Observer<VerifyOtpResponse>() {
//            @Override
//            public void onChanged(@Nullable VerifyOtpResponse verifyOtpResponse) {
//                if (!verifyOtpResponse.getError()) {
//                    signUpViewModel.mainLayoutVisibility.set(View.VISIBLE);
//                    signUpViewModel.progressVisibility.set(View.GONE);
//                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                    PreferenceManager.setBoolValue(Constants.LOGGED_IN, true);
//                    PreferenceManager.setStringValue(Constants.AUTH_TOKEN, verifyOtpResponse.getPayload().get(0).getAuthToken());
//                    PreferenceManager.setStringValue(Constants.FIRST_NAME, verifyOtpResponse.getPayload().get(0).getFirstName());
//                    PreferenceManager.setStringValue(Constants.EMAIL, verifyOtpResponse.getPayload().get(0).getEmail());
//                    finishAffinity();
//                } else {
//                    signUpViewModel.mainLayoutVisibility.set(View.VISIBLE);
//                    signUpViewModel.progressVisibility.set(View.GONE);
//                    Utils.ShowToast(SignUpActivity.this, verifyOtpResponse.getMessage());
//                }
//            }
//        });
//    }

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
