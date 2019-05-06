package com.ziqqi.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
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
import com.ziqqi.model.applycouponmodel.ApplyCouponModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.model.placeordermodel.Payload;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PayPalConfig;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.Utils;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.PlaceOrderViewModel;

import org.json.JSONException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    String paymentMethod = "0";
    String walletNumber = "0";
    String strCurrency = "";
    List<Payload> cartDataList = new ArrayList<>();
    List<Payload> payloadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_gateway);
        placeOrderViewModel = ViewModelProviders.of(this).get(PlaceOrderViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        binding.tvCartTotal.setText("$ " +PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT));

        binding.btCouponApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etCoupon.getText().toString().equalsIgnoreCase("")){
                    applyCouponCode(PreferenceManager.getStringValue(Constants.AUTH_TOKEN), binding.etCoupon.getText().toString(), PreferenceManager.getStringValue(Constants.GUEST_ID));
                }else{
                    Toast.makeText(PaymentGatewayActivity.this, "Please enter code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.llPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
                binding.rbPaypal.setChecked(true);
                binding.next.setVisibility(View.VISIBLE);
                strPaymentType = "PAYPAL";
                paymentMethod = "paypal";
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
                paymentMethod = "mobileWallet";
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
                paymentMethod = "mobileWallet";
                binding.llZaadNumber.setVisibility(View.GONE);
                binding.llDahabNumber.setVisibility(View.VISIBLE);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(PaymentGatewayActivity.this);
                if (strPaymentType.equalsIgnoreCase("PAYPAL")) {
                    getPayment();
                }else if (strPaymentType.equalsIgnoreCase("ZAAD")) {
                    String strZaad = binding.etZaad.getText().toString();
                    if (!strZaad.isEmpty()){
                        walletNumber = "63"+binding.etZaad.getText().toString();
                        PreferenceManager.setStringValue(Constants.WALLET_NUMBER, walletNumber);
                        if(Integer.parseInt(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) < 100){
                            showZAADDialog("SLS " + Integer.parseInt(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) * 9500, "63"+ binding.etZaad.getText().toString());
                        }else{
                            showZAADDialog("$ " +PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT), "63"+ binding.etZaad.getText().toString());
                        }
                    }else{
                        Toast.makeText(PaymentGatewayActivity.this, "Please enter your provider number.", Toast.LENGTH_SHORT).show();
                    }

                }else if (strPaymentType.equalsIgnoreCase("DAHAB")) {
                    String strDahab = binding.etDahab.getText().toString();
                    if (!strDahab.isEmpty()){
                        walletNumber = "65"+binding.etDahab.getText().toString();
                        PreferenceManager.setStringValue(Constants.WALLET_NUMBER, walletNumber);
                        if(Integer.parseInt(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) < 100){
                            showZAADDialog("SLS " + Integer.parseInt(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) * 9500 , "65"+ binding.etDahab.getText().toString());
                        }else{
                            showZAADDialog("$ " +PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT), "65"+ binding.etDahab.getText().toString());
                        }
                    }else{
                        Toast.makeText(PaymentGatewayActivity.this, "Please enter your provider number.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PaymentGatewayActivity.this, "Please choose a payment method", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Integer.parseInt(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) < 100){
            strCurrency = "SLS";
        }else{
            strCurrency = "USD";
        }

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
                        paymentMethod,
                        "",
                        "UNPAID" ,
                        "",
                        walletNumber,
                        PreferenceManager.getStringValue(Constants.BILLING_FIRST_NAME),
                        PreferenceManager.getStringValue(Constants.BILLING_LAST_NAME),
                        PreferenceManager.getStringValue(Constants.BILLING_MOBILE),
                        PreferenceManager.getStringValue(Constants.SHIP_NAME),
                        PreferenceManager.getStringValue(Constants.SHIP_MOBILE),
                        PreferenceManager.getStringValue(Constants.SHIP_COUNTRY),
                        PreferenceManager.getStringValue(Constants.SHIP_CITY),
                        PreferenceManager.getStringValue(Constants.SHIP_LOCATION),
                        PreferenceManager.getStringValue(Constants.SHIP_ADDRESS),
                        strCurrency);
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void getPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT))), "USD", "Ziqqi.com",
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
                                paymentMethod,
                                "",
                                "UNPAID" ,
                                "",
                                walletNumber,
                                PreferenceManager.getStringValue(Constants.BILLING_FIRST_NAME),
                                PreferenceManager.getStringValue(Constants.BILLING_LAST_NAME),
                                PreferenceManager.getStringValue(Constants.BILLING_MOBILE),
                                PreferenceManager.getStringValue(Constants.SHIP_NAME),
                                PreferenceManager.getStringValue(Constants.SHIP_MOBILE),
                                PreferenceManager.getStringValue(Constants.SHIP_COUNTRY),
                                PreferenceManager.getStringValue(Constants.SHIP_CITY),
                                PreferenceManager.getStringValue(Constants.SHIP_LOCATION),
                                PreferenceManager.getStringValue(Constants.SHIP_ADDRESS),
                                strCurrency);

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

    private void placeOrder(String authToken, String paymentMethod, String orderStatus, String paymentStatus, String transacttionId, String walletNumber, String billingFname, String billingLname, String billingMobile, String pickupName, String pickupMobile, String pickupCountry, String pickup_city, String pickup_location, String pickup_address, String payment_currency) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            placeOrderViewModel.placeOder(authToken,paymentMethod, orderStatus, paymentStatus, transacttionId, walletNumber, billingFname, billingLname, billingMobile, pickupName, pickupMobile, pickupCountry, pickup_city, pickup_location, pickup_address, payment_currency);
            placeOrderViewModel.getPlaceOrderResponse().observe(this, new Observer<PlaceOrderResponse>() {
                @Override
                public void onChanged(@Nullable PlaceOrderResponse placeOrderResponse) {
                    if (!placeOrderResponse.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        payloadList = placeOrderResponse.getPayload();
                        cartDataList.addAll(payloadList);

                        startActivity(new Intent(PaymentGatewayActivity.this, PaymentConfirmationActivity.class).putExtra("type", strPaymentType));
                        finishAffinity();
                    } else {
                        Toast.makeText(getApplicationContext(), placeOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(PaymentGatewayActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

    public void applyCouponCode(String authToken, String couponCode, String guest_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            placeOrderViewModel.applyCoupon(authToken, couponCode, guest_id);
            placeOrderViewModel.applyCouponResponse().observe(this, new Observer<ApplyCouponModel>() {
                @Override
                public void onChanged(@Nullable ApplyCouponModel applyCouponModel) {
                    if (!applyCouponModel.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), applyCouponModel.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.tvCartTotal.setText(String.valueOf(applyCouponModel.getPayload().getTotal()));
                    } else {
                        Toast.makeText(getApplicationContext(), applyCouponModel.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(PaymentGatewayActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }

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
