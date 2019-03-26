package com.ziqqi.activities;

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
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityBillingInfoBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.countrymodel.Payload;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.HelpCenterViewModel;

import java.util.ArrayList;
import java.util.List;

public class BillingInfoActivity extends AppCompatActivity {

    BillingInfoViewModel billingInfoViewModel;
    ActivityBillingInfoBinding binding;
    List<String> countries = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    List<Payload> countryPayloadList;
    String CountryId = "1";
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_info);
        billingInfoViewModel = ViewModelProviders.of(this).get(BillingInfoViewModel.class);
        setSupportActionBar(binding.toolbar);
        countryPayloadList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getCountries();
        getCities(CountryId);

        adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        cityAdapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, cities);
        cityAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etName.getText().toString().equals("")  && !binding.etMobileNumber.getText().toString().equals("") && !binding.etAddressDetails.getText().toString().equals("")){
                    addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                            binding.etName.getText().toString(),
                            binding.etName.getText().toString(),
                            binding.etMobileNumber.getText().toString(),
                            binding.etAddressDetails.getText().toString(),
                            binding.etCountry.getSelectedItem().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.etCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                CountryId = countryPayloadList.get(position).getId();
                Log.i("Id", countryPayloadList.get(position).getId());
                getCities(countryPayloadList.get(position).getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private void getCountries() {
        billingInfoViewModel.fetchCountry();
        billingInfoViewModel.getCountryResponse().observe(this, new Observer<CountryResponse>() {
            @Override
            public void onChanged(@Nullable CountryResponse countryResponse) {
                if (!countryResponse.getError()) {
                    adapter.notifyDataSetChanged();
                    countryPayloadList.addAll(countryResponse.getPayload());
                    for (int i = 0; i <= countryResponse.getPayload().size()-1; i++){
                        countries.add(countryResponse.getPayload().get(i).getName());
                    }

                    binding.etCountry.setAdapter(adapter);

                } else {

                }
            }
        });
    }

    private void getCities(String country_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        billingInfoViewModel.fetchCity(country_id);
        billingInfoViewModel.getCityResponse().observe(this, new Observer<CityResponse>() {
            @Override
            public void onChanged(@Nullable CityResponse cityResponse) {
                binding.progressBar.setVisibility(View.GONE);
                if (!cityResponse.getError()) {
                    cityAdapter.notifyDataSetChanged();
                    for (int i = 0; i <= cityResponse.getPayload().size()-1; i++){
                        cities.add(cityResponse.getPayload().get(i).getName());
                    }

                    binding.etCity.setAdapter(cityAdapter);

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addAddress(String authToken, String firstName, String lastName, String mobile, String address, String county) {
        binding.progressBar.setVisibility(View.VISIBLE);
        billingInfoViewModel.fetchData(authToken, firstName, lastName, mobile, address, county);
        billingInfoViewModel.addBillingAddressResponse().observe(this, new Observer<AddBillingAddressModel>() {
            @Override
            public void onChanged(@Nullable AddBillingAddressModel addBillingAddress) {
                if (!addBillingAddress.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();

                    PreferenceManager.setStringValue(Constants.BILLING_COUNTRY, binding.etCountry.getSelectedItem().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_ADRESS, binding.etAddressDetails.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_FIRST_NAME, binding.etName.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_MOBILE, binding.etMobileNumber.getText().toString());

                    startActivity(new Intent(BillingInfoActivity.this, ShippingInfoActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
