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

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityBillingInfoBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.HelpCenterViewModel;

public class BillingInfoActivity extends AppCompatActivity {

    BillingInfoViewModel billingInfoViewModel;
    ActivityBillingInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_info);
        billingInfoViewModel = ViewModelProviders.of(this).get(BillingInfoViewModel.class);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etFirstName.getText().toString().equals("") && !binding.etLastName.getText().toString().equals("") && !binding.etMobileNumber.getText().toString().equals("") && !binding.etAddressDetails.getText().toString().equals("") && !binding.etCountry.getText().toString().equals("")){
                    addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                            binding.etFirstName.getText().toString(),
                            binding.etLastName.getText().toString(),
                            binding.etMobileNumber.getText().toString(),
                            binding.etAddressDetails.getText().toString(),
                            binding.etCountry.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addAddress(String authToken, String firstName, String lastName, String mobile, String address, String county) {
        binding.progressBar.setVisibility(View.VISIBLE);
        billingInfoViewModel.fetchData(authToken, firstName, lastName, mobile, address, county);
        billingInfoViewModel.addBillingAddressResponse().observe(this, new Observer<AddBillingAddressModel>() {
            @Override
            public void onChanged(@Nullable AddBillingAddressModel addBillingAddress) {
                if (!addBillingAddress.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();

                    PreferenceManager.setStringValue(Constants.BILLING_COUNTRY, binding.etCountry.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_ADRESS, binding.etAddressDetails.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_FIRST_NAME, binding.etFirstName.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_LAST_NAME, binding.etLastName.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_MOBILE, binding.etMobileNumber.getText().toString());

                    startActivity(new Intent(BillingInfoActivity.this, ShippingInfoActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();
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
