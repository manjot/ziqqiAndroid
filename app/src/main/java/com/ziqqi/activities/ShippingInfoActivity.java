package com.ziqqi.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityShippingInfoBinding;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.countrymodel.Payload;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.ShippingInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShippingInfoActivity extends AppCompatActivity {

    ShippingInfoViewModel shippingInfoViewModel;
    ActivityShippingInfoBinding binding;
    List<String> countries = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    List<Payload> countryPayloadList;
    String CountryId = "1";
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_info);
        shippingInfoViewModel = ViewModelProviders.of(this).get(ShippingInfoViewModel.class);
        countryPayloadList = new ArrayList<>();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
//        getCountries();
//        getCities(CountryId);

        if (!PreferenceManager.getStringValue(Constants.BILLING_COUNTRY).equalsIgnoreCase("SOMALILAND")){
            binding.checkbox.setVisibility(View.GONE);
        }

        String[] cities=getResources().getStringArray(R.array.array_cities);
        cityAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, cities);
        cityAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        binding.etCity.setAdapter(cityAdapter);

        String[] countries=getResources().getStringArray(R.array.array_countries);
        adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        binding.etCountry.setAdapter(adapter);

        String[] locations =getResources().getStringArray(R.array.array_locations);
        locationAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.etLocation.setAdapter(locationAdapter);

        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    binding.etName.setText(PreferenceManager.getStringValue(Constants.BILLING_FIRST_NAME) + PreferenceManager.getStringValue(Constants.BILLING_LAST_NAME));
                    binding.etMobileNumber.setText(PreferenceManager.getStringValue(Constants.BILLING_MOBILE ));
                    binding.etAddressDetails.setText(PreferenceManager.getStringValue(Constants.BILLING_ADRESS));
                    binding.etLocation.setSelection(locationAdapter.getPosition(PreferenceManager.getStringValue(Constants.BILLING_LOCATION)));
//                    binding.etCountry.setSelection(adapter.getPosition(PreferenceManager.getStringValue(Constants.BILLING_COUNTRY)));
                }else{
                    binding.etName.getText().clear();
                    binding.etMobileNumber.getText().clear();
                    binding.etAddressDetails.getText().clear();
                    binding.etLocation.setSelection(0);
                    binding.etCity.setSelection(0);
                    binding.etCountry.setSelection(0);
                }

            }
        }
        );

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etName.getText().toString().equals("") && !binding.etMobileNumber.getText().toString().equals("") &&  !binding.etAddressDetails.getText().toString().equals("") && !binding.etLocation.getSelectedItem().toString().equals("")){
                    addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                            binding.etName.getText().toString(),
                            binding.etMobileNumber.getText().toString(),
                            binding.etCountry.getSelectedItem().toString(),
                            binding.etCity.getSelectedItem().toString(),
                            binding.etLocation.getSelectedItem().toString(),
                            binding.etAddressDetails.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        binding.etCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
////                CountryId = countryPayloadList.get(position).getId();
//                Log.i("Id", countryPayloadList.get(position).getId());
//                getCities(countryPayloadList.get(position).getId());
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//
//            }
//        });
    }

//    private void getCountries() {
//        shippingInfoViewModel.fetchCountry();
//        shippingInfoViewModel.getCountryResponse().observe(this, new Observer<CountryResponse>() {
//            @Override
//            public void onChanged(@Nullable CountryResponse countryResponse) {
//                if (!countryResponse.getError()) {
//                    adapter.notifyDataSetChanged();
//                    countryPayloadList.addAll(countryResponse.getPayload());
//                    for (int i = 0; i <= countryResponse.getPayload().size()-1; i++){
//                        countries.add(countryResponse.getPayload().get(i).getName());
//                    }
//
//                    binding.etCountry.setAdapter(adapter);
//
//                } else {
//
//                }
//            }
//        });
//    }

//    private void getCities(String country_id) {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        shippingInfoViewModel.fetchCity(country_id);
//        shippingInfoViewModel.getCityResponse().observe(this, new Observer<CityResponse>() {
//            @Override
//            public void onChanged(@Nullable CityResponse cityResponse) {
//                binding.progressBar.setVisibility(View.GONE);
//                if (!cityResponse.getError()) {
//                    cities.clear();
//                    for (int i = 0; i <= cityResponse.getPayload().size()-1; i++){
//                        cities.add(cityResponse.getPayload().get(i).getName());
//                    }
//                    cityAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, cities);
//                    cityAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//                    binding.etCity.setAdapter(cityAdapter);
//
//                } else {
//                    binding.progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//    }

    private void addAddress(String authToken, String name, String mobile, String country, String city, String location, String address) {

        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            shippingInfoViewModel.fetchData(authToken, name, mobile, country, city, location, address);
            shippingInfoViewModel.addShippingAddressResponse().observe(this, new Observer<AddShippingAddressModel>() {
                @Override
                public void onChanged(@Nullable AddShippingAddressModel addShippingAddress) {
                    if (!addShippingAddress.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                        PreferenceManager.setStringValue(Constants.SHIP_COUNTRY, binding.etCity.getSelectedItem().toString());
                        PreferenceManager.setStringValue(Constants.SHIP_ADDRESS, binding.etAddressDetails.getText().toString());
                        PreferenceManager.setStringValue(Constants.SHIP_NAME, binding.etName.getText().toString());
                        PreferenceManager.setStringValue(Constants.SHIP_LOCATION, binding.etLocation.getSelectedItem().toString());
                        PreferenceManager.setStringValue(Constants.SHIP_MOBILE, binding.etMobileNumber.getText().toString());
                        PreferenceManager.setStringValue(Constants.SHIP_CITY, binding.etCountry.getSelectedItem().toString());

                        startActivity(new Intent(ShippingInfoActivity.this, PaymentGatewayActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), addShippingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(ShippingInfoActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
