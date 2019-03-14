package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.PaymentGatewayActivity;
import com.ziqqi.R;
import com.ziqqi.databinding.ActivityShippingInfoBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.ShippingInfoViewModel;

public class ShippingInfoActivity extends AppCompatActivity {

    ShippingInfoViewModel shippingInfoViewModel;
    ActivityShippingInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_info);
        shippingInfoViewModel = ViewModelProviders.of(this).get(ShippingInfoViewModel.class);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etName.getText().toString().equals("") && !binding.etMobileNumber.getText().toString().equals("") && !binding.etCountry.getText().toString().equals("") && !binding.etCity.getText().toString().equals("") && !binding.etAddressDetails.getText().toString().equals("") && !binding.etLocation.getText().toString().equals("")){
                    addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                            binding.etName.getText().toString(),
                            binding.etMobileNumber.getText().toString(),
                            binding.etCountry.getText().toString(),
                            binding.etCity.getText().toString(),
                            binding.etLocation.getText().toString(),
                            binding.etAddressDetails.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addAddress(String authToken, String name, String mobile, String country, String city, String location, String address) {
        binding.progressBar.setVisibility(View.VISIBLE);
        shippingInfoViewModel.fetchData(authToken, name, mobile, country, city, location, address);
        shippingInfoViewModel.addShippingAddressResponse().observe(this, new Observer<AddShippingAddressModel>() {
            @Override
            public void onChanged(@Nullable AddShippingAddressModel addShippingAddress) {
                if (!addShippingAddress.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ShippingInfoActivity.this, PaymentGatewayActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
