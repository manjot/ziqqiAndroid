package com.ziqqi.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziqqi.R;
import com.ziqqi.activities.CommunicationPrefActivity;
import com.ziqqi.activities.FeedbackActivity;
import com.ziqqi.activities.HelpCenterActivity;
import com.ziqqi.activities.MyAddressBookActivity;
import com.ziqqi.activities.MyOrdersActivity;
import com.ziqqi.activities.ProductDetailActivity;
import com.ziqqi.activities.SelectYourLanguageActivity;
import com.ziqqi.databinding.FragmentProfileBinding;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.LoginDialog;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment{

    FragmentProfileBinding binding;
    ProfileViewModel viewModel;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivTitle;
    String strSharingUrl;
    LoginDialog loginDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);

        toolbar=  getActivity().findViewById(R.id.toolbar);
        tvTitle=  toolbar.findViewById(R.id.tv_toolbar_title_text);
        ivTitle=  toolbar.findViewById(R.id.tv_toolbar_title);
        ivTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);

        loginDialog = new LoginDialog();

        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        strSharingUrl = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName();

        if (PreferenceManager.getBoolValue(Constants.LOGGED_IN)){
            binding.tvName.setText(PreferenceManager.getStringValue(Constants.FIRST_NAME));
            binding.tvEmail.setText(PreferenceManager.getStringValue(Constants.EMAIL));
            if (PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equalsIgnoreCase("f") ||PreferenceManager.getStringValue(Constants.FACEBOOK_OR_GMAIL).equalsIgnoreCase("g")){
                Glide.with(getActivity())
                        .load(PreferenceManager.getStringValue(Constants.PROFILE_PIC))
                        .into(binding.ivProfilePic);
            }

        }


        binding.llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyOrdersActivity.class));
            }
        });
        binding.llMyAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyAddressBookActivity.class));
            }
        });
        binding.llCountryLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectYourLanguageActivity.class));
            }
        });
        binding.llCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CommunicationPrefActivity.class));
            }
        });
        binding.llHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
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
                startActivity(new Intent(getContext(), FeedbackActivity.class));
            }
        });
        binding.llRatePlaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
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
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
