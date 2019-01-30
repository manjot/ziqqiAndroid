package com.ziqqi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziqqi.R;
import com.ziqqi.adapters.BestSellerAdapter;
import com.ziqqi.utils.SpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment {
    RecyclerView rvDeals;
    LinearLayoutManager manager;
    BestSellerAdapter adapter;

    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deals, container, false);

        rvDeals = view.findViewById(R.id.rv_deals);
/*        manager = new GridLayoutManager(getActivity(), 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeals.setLayoutManager(manager);
        adapter = new BestSellerAdapter(getActivity());
        rvDeals.setAdapter(adapter);

        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(getActivity(), R.dimen.dp_4, DealsFragment.this);
        rvDeals.addItemDecoration(itemDecoration);*/

        return view;
    }

}
