package com.ziqqi.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziqqi.R;
import com.ziqqi.activities.CommunicationPrefActivity;
import com.ziqqi.activities.MyAddressBookActivity;
import com.ziqqi.activities.MyOrdersActivity;
import com.ziqqi.activities.SelectYourLanguageActivity;
import com.ziqqi.databinding.FragmentProfileBinding;
import com.ziqqi.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment{

    FragmentProfileBinding binding;
    ProfileViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
