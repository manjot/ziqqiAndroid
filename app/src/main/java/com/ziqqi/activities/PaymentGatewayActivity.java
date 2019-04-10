package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ziqqi.R;
import com.ziqqi.databinding.ActivityPaymentGatewayBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PayPalConfig;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.PlaceOrderViewModel;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentGatewayActivity extends AppCompatActivity {

    PlaceOrderViewModel placeOrderViewModel;
    ActivityPaymentGatewayBinding binding;
    boolean isChecked = false;
    private String strPaymentType = "null";
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    TextView amount,zaad_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_gateway);
        placeOrderViewModel = ViewModelProviders.of(this).get(PlaceOrderViewModel.class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        binding.llPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                binding.rbPaypal.setChecked(true);
                binding.next.setVisibility(View.VISIBLE);
                strPaymentType = "PAYPAL";
                binding.llZaadNumber.setVisibility(View.GONE);
                binding.llDahabNumber.setVisibility(View.GONE);
            }
        });

        binding.llZaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                binding.rbZaad.setChecked(true);
                binding.next.setVisibility(View.VISIBLE);
                strPaymentType = "ZAAD";
                binding.llZaadNumber.setVisibility(View.VISIBLE);
                binding.llDahabNumber.setVisibility(View.GONE);
            }
        });
        
        binding.llDahab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                binding.rbDahab.setChecked(true);
                binding.next.setVisibility(View.VISIBLE);
                strPaymentType = "DAHAB";
                binding.llZaadNumber.setVisibility(View.GONE);
                binding.llDahabNumber.setVisibility(View.VISIBLE);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strPaymentType.equalsIgnoreCase("PAYPAL")) {
                    getPayment();
                }else if (strPaymentType.equalsIgnoreCase("ZAAD")) {
                    String strZaad = binding.etZaad.getText().toString();
                    if (!strZaad.isEmpty()){
                        showZAADDialog(String.valueOf(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)), "63"+ binding.etZaad.getText().toString());
                    }else{
                        Toast.makeText(PaymentGatewayActivity.this, "Please enter your provider number.", Toast.LENGTH_SHORT).show();
                    }

                }else if (strPaymentType.equalsIgnoreCase("DAHAB")) {
                    String strDahab = binding.etDahab.getText().toString();
                    if (!strDahab.isEmpty()){
                        showZAADDialog(String.valueOf(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)), "65"+ binding.etDahab.getText().toString());
                    }else{
                        Toast.makeText(PaymentGatewayActivity.this, "Please enter your provider number.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PaymentGatewayActivity.this, "Please choose a payment method", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearAll(){
        binding.next.setVisibility(View.GONE);
        binding.rbDahab.setChecked(false);
        binding.rbZaad.setChecked(false);
        binding.rbPaypal.setChecked(false);
    }

    public void showZAADDialog(String strAmount, String strNumber){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_alert_zaad, null);

        amount = dialogView.findViewById(R.id.amount);
        zaad_number = dialogView.findViewById(R.id.zaad_number);
        Button pay = dialogView.findViewById(R.id.pay);

        amount.setText(strAmount);
        zaad_number.setText(strNumber);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void getPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT))), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
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

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void placeOrder(String authToken, String billingFname, String billingLname, String billingMobile, String pickupName, String pickupMobile, String pickupCountry, String pickup_city, String pickup_location, String pickup_address) {
        binding.progressBar.setVisibility(View.VISIBLE);
        placeOrderViewModel.placeOder(authToken, billingFname, billingLname, billingMobile, pickupName, pickupMobile, pickupCountry, pickup_city, pickup_location, pickup_address);
        placeOrderViewModel.getPlaceOrderResponse().observe(this, new Observer<PlaceOrderResponse>() {
            @Override
            public void onChanged(@Nullable PlaceOrderResponse placeOrderResponse) {
                if (!placeOrderResponse.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PaymentGatewayActivity.this, PaymentConfirmationActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
//        Toast.makeText(getApplicationContext(), "Payment Canceled", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
