package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityPaymentGatewayBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.PlaceOrderViewModel;

public class PaymentGatewayActivity extends AppCompatActivity {

    PlaceOrderViewModel placeOrderViewModel;
    ActivityPaymentGatewayBinding binding;
    boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_gateway);
        placeOrderViewModel = ViewModelProviders.of(this).get(PlaceOrderViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        binding.llPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked){
                    binding.cbPaypal.setChecked(true);
                    binding.rbDahab.setChecked(false);
                    binding.rbZaad.setChecked(false);
                    isChecked = true;
                    binding.next.setVisibility(View.VISIBLE);
                }else{
                    binding.cbPaypal.setChecked(false);
                    isChecked = false;
                    binding.next.setVisibility(View.GONE);
                }
            }
        });

        binding.llZaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked){
                    binding.rbZaad.setChecked(true);
                    binding.rbDahab.setChecked(false);
                    binding.cbPaypal.setChecked(false);
                    isChecked = true;
                    binding.next.setVisibility(View.VISIBLE);
                }else{
                    binding.rbZaad.setChecked(false);
                    isChecked = false;
                    binding.next.setVisibility(View.GONE);
                }
            }
        });

        binding.llDahab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked){
                    binding.rbDahab.setChecked(true);
                    binding.rbZaad.setChecked(false);
                    binding.cbPaypal.setChecked(false);
                    isChecked = true;
                    binding.next.setVisibility(View.VISIBLE);
                }else{
                    binding.rbDahab.setChecked(false);
                    isChecked = false;
                    binding.next.setVisibility(View.GONE);
                }
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                        PreferenceManager.getStringValue(Constants.BILLING_FIRST_NAME),
                        PreferenceManager.getStringValue(Constants.BILLING_LAST_NAME),
                        PreferenceManager.getStringValue(Constants.BILLING_MOBILE),
                        PreferenceManager.getStringValue(Constants.SHIP_NAME),
                        PreferenceManager.getStringValue(Constants.SHIP_MOBILE),
                        PreferenceManager.getStringValue(Constants.SHIP_COUNTRY),
                        PreferenceManager.getStringValue(Constants.SHIP_CITY),
                        PreferenceManager.getStringValue(Constants.SHIP_LOCATION),
                        PreferenceManager.getStringValue(Constants.SHIP_ADDRESS));
            }
        });
    }


    private void placeOrder(String authToken, String billingFname, String billingLname, String billingMobile, String pickupName, String pickupMobile, String pickupCountry, String pickup_city, String pickup_location, String pickup_address) {
        binding.progressBar.setVisibility(View.VISIBLE);
        placeOrderViewModel.placeOder(authToken, billingFname, billingLname, billingMobile, pickupName, pickupMobile, pickupCountry, pickup_city, pickup_location,  pickup_address);
        placeOrderViewModel.getPlaceOrderResponse().observe(this, new Observer<PlaceOrderResponse>() {
            @Override
            public void onChanged(@Nullable PlaceOrderResponse placeOrderResponse) {
                if (!placeOrderResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentGatewayActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
