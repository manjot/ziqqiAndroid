package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityChangePasswordBinding;
import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.forgotpasswordmodel.ForgotPasswordResponse;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.ChangePasswordViewModel;
import com.ziqqi.viewmodel.HelpCenterViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    ChangePasswordViewModel viewModel;
    ActivityChangePasswordBinding binding;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            strEmail = getIntent().getStringExtra("email");
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etCnfPassword.getText().toString().equalsIgnoreCase(binding.etPassword.getText().toString())){
                    binding.tvError.setVisibility(View.VISIBLE);
                }else{
                    changePassword();
                }
            }
        });


    }

    private void changePassword() {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> call = apiInterface.changedPassword(strEmail, binding.etCode.getText().toString(), binding.etCnfPassword.getText().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        Log.e("JSON", response.body());
                        JSONObject object = new JSONObject(response.body());
                        if (object.getInt("Status") == 1) {
                            binding.progressBar.setVisibility(View.GONE);
                            /*rlMain.setVisibility(View.VISIBLE);*/
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            Toast.makeText(ChangePasswordActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            /*rlMain.setVisibility(View.VISIBLE);*/
                            Toast.makeText(ChangePasswordActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(ChangePasswordActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
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
