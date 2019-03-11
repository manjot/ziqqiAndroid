package com.ziqqi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.sdk.permissions.PermissionActivity;
import com.ziqqi.R;

import java.util.ArrayList;

public class PaymentGatewayActivity extends AppCompatActivity {

    Button btn_pay, btn_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        Hover.initialize(this);

        btn_pay = findViewById(R.id.btn_pay);
        btn_permission = findViewById(R.id.btn_permission);

        btn_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivityForResult(i, 0);
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new HoverParameters.Builder(PaymentGatewayActivity.this)
                        .request("action_id")
                        .extra("Test", "1")
                        .buildIntent();
                startActivityForResult(i, 0);
            }
        });



    }

//    @Override
//    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
//        if (requestCode == HOVER_REQUEST && resultCode == Activity.RESULT_OK) {
//            String[] sessionTextArr = data.getStringArrayExtra("ussd_messages");
//            String uuid = data.getStringExtra("uuid");
//        } else if (requestCode == HOVER_REQUEST && resultCode == Activity.RESULT_CANCELED) {
//            Toast.makeText(this, "Error: " + data.getStringExtra("error"), Toast.LENGTH_LONG).show();
//        }
//    }
}
