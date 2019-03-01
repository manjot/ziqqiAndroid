package com.ziqqi.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziqqi.R;
import com.ziqqi.databinding.FragmentCartBinding;
import com.ziqqi.viewmodel.CartViewModel;
import com.ziqqi.viewmodel.ProfileViewModel;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    CartViewModel viewModel;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_cart, container, false);
        binding.executePendingBindings();
        binding.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
