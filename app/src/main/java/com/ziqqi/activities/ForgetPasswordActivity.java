package com.ziqqi.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;
import com.ziqqi.utils.FontCache;
import com.ziqqi.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText etEmail;
    Button btnSubmit;
    TextView tv;
    RadioButton rbSms, rbEmail;
    ProgressBar progressBar;
    RelativeLayout rlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        init();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isValidEmail(etEmail.getText().toString())) {
                    progressBar.setVisibility(View.VISIBLE);
                    rlMain.setVisibility(View.GONE);
                    getPassword();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPassword() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getPassword(etEmail.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.e("JSON", response.body());
                    JSONObject object = new JSONObject(response.body());
                    if (object.getInt("Status") == 1) {
                        progressBar.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgetPasswordActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgetPasswordActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void init() {
        etEmail = findViewById(R.id.et_enter);
        rbSms = findViewById(R.id.rb_sms);
        rbEmail = findViewById(R.id.rb_email);
        btnSubmit = findViewById(R.id.btn_submit);
        tv = findViewById(R.id.tv);
        progressBar = findViewById(R.id.progress_bar);
        rlMain = findViewById(R.id.rl_main);

        Typeface regular = FontCache.get(getResources().getString(R.string.regular), this);
        Typeface medium = FontCache.get(getResources().getString(R.string.medium), this);
        Typeface light = FontCache.get(getResources().getString(R.string.light), this);

        etEmail.setTypeface(regular);
        rbSms.setTypeface(regular);
        rbEmail.setTypeface(regular);
        tv.setTypeface(light);
        btnSubmit.setTypeface(medium);
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
