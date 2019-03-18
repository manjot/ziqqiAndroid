package com.ziqqi.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.facebook.login.Login;
import com.ziqqi.R;
import com.ziqqi.activities.LoginActivity;

public class LoginDialog {

    public void showDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_login_first);

        Button bt_cancel = dialog.findViewById(R.id.bt_cancel);
        Button bt_login = dialog.findViewById(R.id.bt_login);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
