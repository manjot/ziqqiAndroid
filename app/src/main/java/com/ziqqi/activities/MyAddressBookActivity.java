package com.ziqqi.activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.adapters.MyAddressAdapter;
import com.ziqqi.adapters.SearchCategoryAdapter;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.countrymodel.Payload;
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.MyAddressViewModel;
import com.ziqqi.viewmodel.MyOrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAddressBookActivity extends AppCompatActivity {

    MyAddressViewModel myAddressViewModel;
    ActivityMyAddressBookBinding binding;
    String strCountry, strCity, strLocation, strAddress, strFirstName, strLastName, strMobile;
    List<Payload> countryPayloadList;
    EditText  et_address_details, et_first_name, et_last_name, et_mobile_number;
    Spinner et_country, et_city, et_location;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> locationAdapter;
    List<String> countries = new ArrayList<>();
    boolean isCityLoaded = false;
    int  countrySpinnerPosition = 0;
    LinearLayout ll_city, ll_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_address_book);
        myAddressViewModel = ViewModelProviders.of(this).get(MyAddressViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        countryPayloadList = new ArrayList<>();
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
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.mainLayout.setVisibility(View.GONE);
            binding.llNoData.setVisibility(View.GONE);
            myAddressViewModel.getBillingAddress(authToken);
            myAddressViewModel.getBillingAddressResponse().observe(this, new Observer<BillingAddressModel>() {
                @Override
                public void onChanged(@Nullable BillingAddressModel shippingAddressModel) {
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

                        if (shippingAddressModel.getPayload().getCountry().equalsIgnoreCase("SOMALILAND")){
                            binding.tvUserName.setText(shippingAddressModel.getPayload().getFirstName() +" "+ shippingAddressModel.getPayload().getLastName());
                            binding.tvLineOne.setText("Address : \n" +shippingAddressModel.getPayload().getLocation());
                            binding.tvLineTwo.setText(shippingAddressModel.getPayload().getCity()+"\n"+shippingAddressModel.getPayload().getCountry());
                            binding.tvAddress.setText("Address Details : \n"+ shippingAddressModel.getPayload().getAddressDetails());
                            binding.tvMobile.setText("Mobile : "+shippingAddressModel.getPayload().getMobile());
                        }else{
                            binding.tvUserName.setText(shippingAddressModel.getPayload().getFirstName() +" "+ shippingAddressModel.getPayload().getLastName());
                            binding.tvLineTwo.setText(shippingAddressModel.getPayload().getCountry());
                            binding.tvAddress.setText("Address Details : \n"+ shippingAddressModel.getPayload().getAddressDetails());
                            binding.tvMobile.setText("Mobile : "+shippingAddressModel.getPayload().getMobile());
                        }



//                    }else if (shippingAddressModel.getCode() == 204){
//                        binding.progressBar.setVisibility(View.GONE);
//                        binding.mainLayout.setVisibility(View.GONE);
//                        binding.llNoData.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), shippingAddressModel.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.mainLayout.setVisibility(View.GONE);
                        binding.llNoData.setVisibility(View.VISIBLE);

                    }
                }
            });
        }else{
            Toast.makeText(MyAddressBookActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
        }


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
        ll_city = dialogView.findViewById(R.id.ll_city);
        ll_location = dialogView.findViewById(R.id.ll_location);

        Button cancel =  dialogView.findViewById(R.id.cancel);
        Button save = dialogView.findViewById(R.id.save);
        et_address_details.setText(strAddress);
        et_first_name.setText(strFirstName);
        et_last_name.setText(strLastName);
        et_mobile_number.setText(strMobile);

        String[] citiy =getResources().getStringArray(R.array.array_cities);
        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, citiy);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_city.setAdapter(cityAdapter);

        String[] locations =getResources().getStringArray(R.array.array_locations);
        locationAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        et_location.setAdapter(locationAdapter);

        getCountries();

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        et_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySpinnerPosition = et_country.getSelectedItemPosition();
                PreferenceManager.setIntValue(Constants.BILLING_COUNTRY_POSITION, countrySpinnerPosition);
                Log.i("Id", countryPayloadList.get(position).getId());
                /*getCities(countryPayloadList.get(position).getId());*/
                if (et_country.getSelectedItem().toString().equalsIgnoreCase("SOMALILAND")){
                    ll_city.setVisibility(View.VISIBLE);
                    ll_location.setVisibility(View.VISIBLE);
                }else {
                    ll_city.setVisibility(View.GONE);
                    ll_location.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!et_country.getSelectedItem().toString().equalsIgnoreCase("SOMALILAND")){
                    strCity = "";
                    strLocation = "";
                }else{
                    strCity = et_city.getSelectedItem().toString();
                    strLocation = et_location.getSelectedItem().toString();
                }
                // DO SOMETHINGS
                addAddress(PreferenceManager.getStringValue(Constants.AUTH_TOKEN),
                        et_first_name.getText().toString(),
                        et_last_name.getText().toString(),
                        et_mobile_number.getText().toString(),
                        et_country.getSelectedItem().toString(),
                        strCity,
                        strLocation,
                        et_address_details.getText().toString());
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void addAddress(String authToken, String Fname, String Lname, String mobile, String county, String city, String location, String address) {
        binding.progressBar.setVisibility(View.VISIBLE);
        myAddressViewModel.fetchData(authToken, Fname, Lname, mobile, county, city, location, address);
        myAddressViewModel.addBillingAddressResponse().observe(this, new Observer<AddBillingAddressModel>() {
            @Override
            public void onChanged(@Nullable AddBillingAddressModel addBillingAddress) {
                if (!addBillingAddress.getError()) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceManager.setStringValue(Constants.BILLING_COUNTRY, et_country.getSelectedItem().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_ADRESS, et_address_details.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_FIRST_NAME, et_first_name.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_LAST_NAME, et_last_name.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_MOBILE, et_mobile_number.getText().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_CITY, et_city.getSelectedItem().toString());
                    PreferenceManager.setStringValue(Constants.BILLING_LOCATION, et_location.getSelectedItem().toString());

                    strCountry = addBillingAddress.getPayload().getCountry();
                    strCity = addBillingAddress.getPayload().getCity();
                    strLocation = addBillingAddress.getPayload().getLocation();
                    strAddress = addBillingAddress.getPayload().getAddressDetails();
                    strFirstName = addBillingAddress.getPayload().getFirstName();
                    strLastName = addBillingAddress.getPayload().getLastName();
                    strMobile = addBillingAddress.getPayload().getMobile();


                    binding.tvUserName.setText(addBillingAddress.getPayload().getFirstName() +" "+ addBillingAddress.getPayload().getLastName());
                    binding.tvLineOne.setText("Address : \n" +addBillingAddress.getPayload().getLocation());
                    binding.tvLineTwo.setText(addBillingAddress.getPayload().getCity()+"\n"+addBillingAddress.getPayload().getCountry());
                    binding.tvAddress.setText("Address Details : \n"+ addBillingAddress.getPayload().getAddressDetails());
                    binding.tvMobile.setText("Mobile : "+addBillingAddress.getPayload().getMobile());
                } else {
                    Toast.makeText(getApplicationContext(), addBillingAddress.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getCountries() {
        if (ConnectivityHelper.isConnectedToNetwork(this)) {
            myAddressViewModel.fetchCountry();
            myAddressViewModel.getCountryResponse().observe(this, new Observer<CountryResponse>() {
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

                        et_country.setAdapter(adapter);

                    } else {

                    }
                }
            });
        } else {
            Toast.makeText(MyAddressBookActivity.this, "You're not connected!", Toast.LENGTH_SHORT).show();
        }

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
