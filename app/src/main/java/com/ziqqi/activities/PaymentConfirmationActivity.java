package com.ziqqi.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

public class PaymentConfirmationActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button bt_click, bt_method;
    String strPaymentType;
    TextView tv_send;
    String strUSSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        toolbar = findViewById(R.id.toolbar);
        bt_click = findViewById(R.id.bt_click);
        bt_method = findViewById(R.id.bt_method);
        tv_send = findViewById(R.id.tv_send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            strPaymentType = getIntent().getStringExtra("type");
            if (strPaymentType.equalsIgnoreCase("ZAAD")){
                strUSSD = "*883*504880*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                bt_method.setText("ZAAD - 504880");
                tv_send.setText("To complete the order, please send us the payment of USD "+PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+ " from your USD account with phone number " +PreferenceManager.getStringValue(Constants.WALLET_NUMBER)+ " to our "+strPaymentType+" Merchant account mentioned below.");
            }else if (strPaymentType.equalsIgnoreCase("DAHAB")){
                bt_method.setText("E-Dahab - 74110");
                strUSSD = "*113*74110*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                tv_send.setText("To complete the order, please send us the payment of USD "+PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+ " from your USD account with phone number " +PreferenceManager.getStringValue(Constants.WALLET_NUMBER)+ " to our "+strPaymentType+" Merchant account mentioned below.");
            }
        }


        bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + strUSSD));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(PaymentConfirmationActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentConfirmationActivity.this, MainActivity.class));
        finish();
    }
}
