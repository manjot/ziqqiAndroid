package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnCartItemlistener;
import com.ziqqi.R;
import com.ziqqi.adapters.CartAdapter;
import com.ziqqi.adapters.PlaceOrderAdapter;
import com.ziqqi.model.placeordermodel.Payload;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PaymentConfirmationActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button bt_click, bt_method;
    String strPaymentType, strCartTotal;
    TextView tv_send;
    String strUSSD;
    List<Payload> payloadList = new ArrayList<>();
    RecyclerView rvCart;
    LinearLayoutManager manager;
    OnCartItemlistener listener;
    PlaceOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        toolbar = findViewById(R.id.toolbar);
        bt_click = findViewById(R.id.bt_click);
        bt_method = findViewById(R.id.bt_method);
        tv_send = findViewById(R.id.tv_send);
        rvCart = findViewById(R.id.rv_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        if (getIntent().getExtras() != null){
            strPaymentType = getIntent().getStringExtra("type");
            strCartTotal = getIntent().getStringExtra("cartTotal");
            strCartTotal = strCartTotal.indexOf(".") < 0 ? strCartTotal : strCartTotal.replaceAll("0*$", "").replaceAll("\\.$", "");
            strCartTotal = strCartTotal.replace(".", "*");

            if (strPaymentType.equalsIgnoreCase("ZAAD")){
                if (Float.parseFloat(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) >= 100){
                    strUSSD = "*883*504880*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                } else{
                    strUSSD = "*223*504880*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                }

                bt_method.setText("ZAAD - 504880");
                tv_send.setText("To complete the order, please send us the payment of USD "+PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+ " from your USD account with phone number " +PreferenceManager.getStringValue(Constants.WALLET_NUMBER)+ " to our "+strPaymentType+" Merchant account mentioned below.");

            }else if (strPaymentType.equalsIgnoreCase("DAHAB")){

                if (Float.parseFloat(PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)) >= 100){
                    strUSSD = "*113*74110*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                } else{
                    strUSSD = "*773*74110*" + PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+Uri.encode("#");
                }

                bt_method.setText("E-Dahab - 74110");
                tv_send.setText("To complete the order, please send us the payment of USD "+PreferenceManager.getStringValue(Constants.CART_TOTAL_AMOUNT)+ " from your USD account with phone number " +PreferenceManager.getStringValue(Constants.WALLET_NUMBER)+ " to our "+strPaymentType+" Merchant account mentioned below.");
            }
        }

//        setUpAdapter();

        bt_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + strUSSD));
                startActivity(intent);
            }
        });

    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(PaymentConfirmationActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCart.setLayoutManager(manager);
        adapter = new PlaceOrderAdapter(PaymentConfirmationActivity.this, payloadList, listener);
        rvCart.setAdapter(adapter);
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
