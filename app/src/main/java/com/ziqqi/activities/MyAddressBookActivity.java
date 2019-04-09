package com.ziqqi.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.MyAddressAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.myaddressmodel.Payload;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.MyAddressViewModel;
import com.ziqqi.viewmodel.MyOrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAddressBookActivity extends AppCompatActivity {

    MyAddressViewModel myAddressViewModel;
    ActivityMyAddressBookBinding binding;
    String strCountry, strCity, strLocation, strAddress, strFirstName, strLastName, strMobile;

    EditText et_country, et_city, et_location, et_address_details, et_first_name, et_last_name, et_mobile_number;

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
        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            getAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        }
        binding.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditAddressDialog();
            }
        });
    }

    private void getAddress(String authToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.mainLayout.setVisibility(View.GONE);
        binding.llNoData.setVisibility(View.GONE);
        myAddressViewModel.fetchShippingAddress(authToken);
        myAddressViewModel.getShippingAddressResponse().observe(this, new Observer<ShippingAddressModel>() {
            @Override
            public void onChanged(@Nullable ShippingAddressModel shippingAddressModel) {
                if (!shippingAddressModel.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.mainLayout.setVisibility(View.VISIBLE);
                    binding.llNoData.setVisibility(View.GONE);

                    strCountry = shippingAddressModel.getPayload().getCountry();
                    strCity = shippingAddressModel.getPayload().getCity();
                    strLocation = shippingAddressModel.getPayload().getLocation();
                    strAddress = shippingAddressModel.getPayload().getAddressDetails();
                    strFirstName = shippingAddressModel.getPayload().getFirstName();
                    strLastName = shippingAddressModel.getPayload().getLastName();
                    strMobile = shippingAddressModel.getPayload().getMobile();

                    binding.tvUserName.setText(shippingAddressModel.getPayload().getFirstName() + shippingAddressModel.getPayload().getLastName());
                    binding.tvLineOne.setText("Address : \n" +shippingAddressModel.getPayload().getAddress1());
                    binding.tvLineTwo.setText(shippingAddressModel.getPayload().getAddress2());
                    binding.tvAddress.setText("Address Details : \n"+ shippingAddressModel.getPayload().getAddressDetails());
                    binding.tvMobile.setText("Mobile : "+shippingAddressModel.getPayload().getMobile());

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.mainLayout.setVisibility(View.GONE);
                    binding.llNoData.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    public void showEditAddressDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_address, null);

        et_country = dialogView.findViewById(R.id.et_country);
        et_city = dialogView.findViewById(R.id.et_city);
        et_location = dialogView.findViewById(R.id.et_location);
        et_address_details = dialogView.findViewById(R.id.et_address_details);
        et_first_name = dialogView.findViewById(R.id.et_first_name);
        et_last_name = dialogView.findViewById(R.id.et_last_name);
        et_mobile_number = dialogView.findViewById(R.id.et_mobile_number);

        Button cancel =  dialogView.findViewById(R.id.cancel);
        Button save = dialogView.findViewById(R.id.save);
        et_country.setText(strCountry);
        et_city.setText(strCity);
        et_location.setText(strLocation);
        et_address_details.setText(strAddress);
        et_first_name.setText(strFirstName);
        et_last_name.setText(strLastName);
        et_mobile_number.setText(strMobile);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void addAddress(String authToken, String name, String mobile, String country, String city, String location, String address) {
        binding.progressBar.setVisibility(View.VISIBLE);
        myAddressViewModel.fetchData(authToken, name, mobile, country, city, location, address);
        myAddressViewModel.addShippingAddressResponse().observe(this, new Observer<AddShippingAddressModel>() {
            @Override
            public void onChanged(@Nullable AddShippingAddressModel addShippingAddress) {
                if (!addShippingAddress.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceManager.setStringValue(Constants.SHIP_COUNTRY, et_country.getText().toString());
                    PreferenceManager.setStringValue(Constants.SHIP_ADDRESS, et_address_details.getText().toString());
                    PreferenceManager.setStringValue(Constants.SHIP_NAME, et_first_name.getText().toString());
                    PreferenceManager.setStringValue(Constants.SHIP_LOCATION, et_location.getText().toString());
                    PreferenceManager.setStringValue(Constants.SHIP_MOBILE, et_mobile_number.getText().toString());
                    PreferenceManager.setStringValue(Constants.SHIP_CITY, et_city.getText().toString());

                    startActivity(new Intent(MyAddressBookActivity.this, PaymentGatewayActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
