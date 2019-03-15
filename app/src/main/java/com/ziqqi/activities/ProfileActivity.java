package com.ziqqi.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziqqi.R;
import com.ziqqi.databinding.ActivityProfileBinding;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    ProfileViewModel viewModel;
    String strSharingUrl;
    LoginDialog loginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        loginDialog = new LoginDialog();

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        strSharingUrl = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getPackageName();

        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            binding.tvName.setText(PreferenceManager.getStringValue(Constants.FIRST_NAME));
            binding.tvEmail.setText(PreferenceManager.getStringValue(Constants.EMAIL));
            if (PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equalsIgnoreCase("f") ||PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equalsIgnoreCase("g")){
                Glide.with(this)
                        .load(PreferenceManager.getStringValue(Constants.PROFILE_PIC))
                        .into(binding.ivProfilePic);
            }

        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MyOrdersActivity.class));
            }
        });
        binding.llMyAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MyAddressBookActivity.class));
            }
        });
        binding.llCountryLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SelectYourLanguageActivity.class));
            }
        });
        binding.llCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, CommunicationPrefActivity.class));
            }
        });
        binding.llHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HelpCenterActivity.class));
            }
        });
        binding.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ziqqi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, strSharingUrl);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
        binding.llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, FeedbackActivity.class));
            }
        });
        binding.llRatePlaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
    }
}
