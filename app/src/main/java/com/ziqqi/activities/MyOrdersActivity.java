package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityMyOrdersBinding;
import com.ziqqi.viewmodel.MyOrderViewModel;
import com.ziqqi.viewmodel.SignUpViewModel;

public class MyOrdersActivity extends AppCompatActivity {

    MyOrderViewModel myOrderViewModel;
    ActivityMyOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        myOrderViewModel = ViewModelProviders.of(this).get(MyOrderViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
    }
}
