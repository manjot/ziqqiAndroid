package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.BillingInfoViewModel;
import com.ziqqi.viewmodel.HelpCenterViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BillingInfoActivity extends AppCompatActivity {

    BillingInfoViewModel billingInfoViewModel;
    ActivityBillingInfoBinding binding;
    List<String> countries = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    List<Payload> countryPayloadList;
    String CountryId = "1";
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> locationAdapter;
    int countrySpinnerPosition = 0;
    int citySpinnerPosition = 0;
    boolean isCityLoaded = false;
    String strCity;
    String strLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_info);
        billingInfoViewModel = ViewModelProviders.of(this).get(BillingInfoViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setSupportActionBar(binding.toolbar);
        countryPayloadList = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getCountries();
        /*getCities(CountryId);*/

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String[] citiy =getResources().getStringArray(R.array.array_cities);
        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, citiy);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.etCity.setAdapter(cityAdapter);

        String[] locations =getResources().getStringArray(R.array.array_locations);
        locationAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.etLocation.setAdapter(locationAdapter);

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etFirstName.getText().toString().equals("") && !binding.etLastName.getText().toString().equals("") && !binding.etMobileNumber.getText().toString().equals("") && !binding.etAddressDetails.getText().toString().equals("")) {
                    if (!binding.etCountry.getSelectedItem().toString().equalsIgnoreCase("SOMALILAND")){
                        strCity = "";
                        strLocation = "";
                    }else{
                        strCity = binding.etCity.getSelectedItem().toString();
                        strLocation = binding.etLocation.getSelectedItem().toString();
                    }
                    addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                            binding.etFirstName.getText().toString(),
                            binding.etLastName.getText().toString(),
                            binding.etMobileNumber.getText().toString(),
                            binding.etCountry.getSelectedItem().toString(),
                            strCity,
                            strLocation,
                            binding.etAddressDetails.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.etCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySpinnerPosition = binding.etCountry.getSelectedItemPosition();
                PreferenceManager.setIntValue(Constants.BILLING_COUNTRY_POSITION, countrySpinnerPosition);
                Log.i("Id", countryPayloadList.get(position).getId());
                /*getCities(countryPayloadList.get(position).getId());*/
                if (binding.etCountry.getSelectedItem().toString().equalsIgnoreCase("SOMALILAND")){
                    binding.etCity.setVisibility(View.VISIBLE);
                    binding.etLocation.setVisibility(View.VISIBLE);
                }else {
                    binding.etCity.setVisibility(View.GONE);
                    binding.etLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        binding.etCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySpinnerPosition = binding.etCity.getSelectedItemPosition();
                PreferenceManager.setIntValue(Constants.BILLING_CITY_POSITION, citySpinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void getCountries() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            billingInfoViewModel.fetchCountry();
            billingInfoViewModel.getCountryResponse().observe(this, new Observer<CountryResponse>() {
                @Override
                public void onChanged(@Nullable CountryResponse countryResponse) {
                    if (!countryResponse.getError()) {
                        adapter.notifyDataSetChanged();
                        countryPayloadList.addAll(countryResponse.getPayload());
                        for (int i = 0; i <= countryResponse.getPayload().size() - 1; i++) {
                            countries.add(countryResponse.getPayload().get(i).getName());
                        }
                        if (!isCityLoaded) {
                            isCityLoaded = true;
                            getAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
                        }

                        binding.etCountry.setAdapter(adapter);

                    } else {

                    }
                }
            });
        } else {
            Toast.makeText(BillingInfoActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }

/*    private void getCities(String country_id) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            billingInfoViewModel.fetchCity(country_id);
            billingInfoViewModel.getCityResponse().observe(this, new Observer<CityResponse>() {
                @Override
                public void onChanged(@Nullable CityResponse cityResponse) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (!cityResponse.getError()) {
                        cities.clear();
                        for (int i = 0; i <= cityResponse.getPayload().size() - 1; i++) {
                            cities.add(cityResponse.getPayload().get(i).getName());
                        }
                        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cities);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.etCity.setAdapter(cityAdapter);
                        if (!isCityLoaded) {
                            isCityLoaded = true;
                            getAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
                        }

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Toast.makeText(BillingInfoActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

    }*/

    private void getAddress(String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            billingInfoViewModel.getBillingAddress(authToken);
            billingInfoViewModel.getBillingAddressResponse().observe(this, new Observer<BillingAddressModel>() {
                @Override
                public void onChanged(@Nullable BillingAddressModel billingAddressModel) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (!billingAddressModel.getError()) {
                        binding.etFirstName.setText(billingAddressModel.getPayload().getFirstName());
                        binding.etLastName.setText(billingAddressModel.getPayload().getLastName());
                        binding.etMobileNumber.setText(billingAddressModel.getPayload().getMobile());
                        binding.etAddressDetails.setText(billingAddressModel.getPayload().getAddressDetails());
                        String strCountry = billingAddressModel.getPayload().getCountry();
                        strLocation = billingAddressModel.getPayload().getLocation();
                        binding.etCity.setSelection(cityAdapter.getPosition(strCity));
                        binding.etCountry.setSelection(adapter.getPosition(strCountry));
                        binding.etLocation.setSelection(locationAdapter.getPosition(strLocation));
                    } else {

                    }
                }
            });
        } else {
            Toast.makeText(BillingInfoActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addAddress(String authToken, String Fname, String Lname, String mobile, String county, String city, String location, String address) {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            billingInfoViewModel.fetchData(authToken, Fname, Lname, mobile, county, city, location, address);
            billingInfoViewModel.addBillingAddressResponse().observe(this, new Observer<AddBillingAddressModel>() {
                @Override
                public void onChanged(@Nullable AddBillingAddressModel addBillingAddress) {
                    if (!addBillingAddress.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();

                        PreferenceManager.setStringValue(Constants.BILLING_COUNTRY, binding.etCountry.getSelectedItem().toString());
                        PreferenceManager.setStringValue(Constants.BILLING_ADRESS, binding.etAddressDetails.getText().toString());
                        PreferenceManager.setStringValue(Constants.BILLING_FIRST_NAME, binding.etFirstName.getText().toString());
                        PreferenceManager.setStringValue(Constants.BILLING_LAST_NAME, binding.etLastName.getText().toString());
                        PreferenceManager.setStringValue(Constants.BILLING_MOBILE, binding.etMobileNumber.getText().toString());
                        if (binding.etCountry.getSelectedItem().toString().equalsIgnoreCase("SOMALILAND")) {
                            PreferenceManager.setStringValue(Constants.BILLING_CITY, binding.etCity.getSelectedItem().toString());
                            PreferenceManager.setStringValue(Constants.BILLING_LOCATION, binding.etLocation.getSelectedItem().toString());
                        }else{
                            PreferenceManager.setStringValue(Constants.BILLING_CITY, "");
                            PreferenceManager.setStringValue(Constants.BILLING_LOCATION, "");
                        }

                        startActivity(new Intent(BillingInfoActivity.this, ShippingInfoActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            Toast.makeText(BillingInfoActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
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
