package com.ziqqi.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ziqqi.R;
import com.ziqqi.databinding.ActivityHelpCenterBinding;
import com.ziqqi.databinding.ActivityMyAddressBookBinding;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.HelpCenterViewModel;
import com.ziqqi.viewmodel.MyAddressViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener {

    HelpCenterViewModel helpCenterViewModel;
    ActivityHelpCenterBinding binding;
    RelativeLayout ll_fb, ll_insta, ll_google, ll_linkedin, ll_twitter,ll_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_center);
        helpCenterViewModel = ViewModelProviders.of(this).get(HelpCenterViewModel.class);

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        binding.executePendingBindings();
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);

        ll_fb = binding.llFb;
        ll_insta = binding.llInsta;
        ll_google = binding.llGoogle;
        ll_linkedin = binding.llLinkedin;
        ll_twitter = binding.llTwitter;
        ll_v = binding.llV;

        ll_fb.setOnClickListener(this);
        ll_insta.setOnClickListener(this);
        ll_google.setOnClickListener(this);
        ll_linkedin.setOnClickListener(this);
        ll_twitter.setOnClickListener(this);
        ll_v.setOnClickListener(this);

        fetchHelps();

    }

    private void fetchHelps() {
        if (ConnectivityHelper.isConnectedToNetwork(this)){
            binding.progressBar.setVisibility(View.VISIBLE);
            helpCenterViewModel.fetchHelpCenter();
            helpCenterViewModel.getHelpResponse().observe(this, new Observer<HelpCenterModel>() {
                @Override
                public void onChanged(@Nullable final HelpCenterModel helpCenterModel) {
                    if (!helpCenterModel.getError()) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tvHelpFirst.setText(helpCenterModel.getPayload().get(0).getTitle());
                        binding.tvHelpSecond.setText(helpCenterModel.getPayload().get(1).getTitle());
                        binding.tvHelpThird.setText(helpCenterModel.getPayload().get(2).getTitle());
                        binding.tvHelpFourth.setText(helpCenterModel.getPayload().get(3).getTitle());
                        binding.tvHelpFifth.setText(helpCenterModel.getPayload().get(4).getTitle());
                        binding.tvHelpSixth.setText(helpCenterModel.getPayload().get(5).getTitle());

                        binding.llOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(0).getId()).putExtra("title" ,helpCenterModel.getPayload().get(0).getTitle()));
                            }
                        });
                        binding.llTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(1).getId()).putExtra("title" ,helpCenterModel.getPayload().get(1).getTitle()));
                            }
                        });
                        binding.llThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(2).getId()).putExtra("title" ,helpCenterModel.getPayload().get(2).getTitle()));
                            }
                        });
                        binding.llFour.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(3).getId()).putExtra("title" ,helpCenterModel.getPayload().get(3).getTitle()));
                            }
                        });
                        binding.llFive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(4).getId()).putExtra("title" ,helpCenterModel.getPayload().get(4).getTitle()));
                            }
                        });
                        binding.llSix.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(HelpCenterActivity.this, MyAccountActivity.class).putExtra("id" ,helpCenterModel.getPayload().get(5).getId()).putExtra("title" ,helpCenterModel.getPayload().get(5).getTitle()));
                            }
                        });
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(HelpCenterActivity.this,"You're not connected!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_fb:
                OpenUrl("https://www.facebook.com/ziqqisl/");
                break;

            case R.id.ll_insta:
                OpenUrl("https://www.instagram.com/ziqqi_4you/");
                break;

            case R.id.ll_google:
                OpenUrl("https://plus.google.com/111518750782020554842");
                break;

            case R.id.ll_linkedin:
                OpenUrl("https://www.linkedin.com/company/ziqqi-com");
                break;

            case R.id.ll_twitter:
                OpenUrl("https://twitter.com/ziqqi_4you/");
                break;

            case R.id.ll_v:
                OpenUrl("https://vimeo.com/ziqqi");
                break;

        }
    }

    public void OpenUrl(String url){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
