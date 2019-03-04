package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.viewmodel.MyAddressViewModel;
import com.ziqqi.viewmodel.MyOrderViewModel;

public class MyAddressBookActivity extends AppCompatActivity {

    MyAddressViewModel myAddressViewModel;
    ActivityMyAddressBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_address_book);
        myAddressViewModel = ViewModelProviders.of(this).get(MyAddressViewModel.class);
        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
    }
}
