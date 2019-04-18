package com.ziqqi.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziqqi.OnCartItemlistener;
import com.ziqqi.OnItemClickListener;
import com.ziqqi.R;
import com.ziqqi.activities.BillingInfoActivity;
import com.ziqqi.activities.MainActivity;
import com.ziqqi.adapters.CartAdapter;
import com.ziqqi.model.viewcartmodel.Payload;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.utils.ConnectivityHelper;
import com.ziqqi.utils.Constants;
import com.ziqqi.utils.PreferenceManager;
import com.ziqqi.utils.SpacesItemDecoration;
import com.ziqqi.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CheckoutDialogFragment extends DialogFragment {

    CartViewModel viewModel;
    RecyclerView rv_cart;
    List<Payload> payloadList = new ArrayList<>();
    List<Payload> cartDataList = new ArrayList<>();
    OnCartItemlistener listener;
    CartAdapter adapter;
    SpacesItemDecoration spacesItemDecoration;
    LinearLayoutManager manager;
    LinearLayout ll_total;
    TextView tv_total_price;
    Button bt_checkout, bt_next;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Locale locale = new Locale(PreferenceManager.getStringValue(Constants.LANG));
        Configuration config = getApplicationContext().getResources().getConfiguration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());


        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }

        getDialog().setCanceledOnTouchOutside(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        View view =  inflater.inflate(R.layout.fragment_checkout_dialog, container, false);

        rv_cart = view.findViewById(R.id.rv_cart);
        ll_total = view.findViewById(R.id.ll_total);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        bt_checkout = view.findViewById(R.id.bt_checkout);
        bt_next = view.findViewById(R.id.bt_next);

        setUpAdapter();
        fetchCart(PreferenceManager.getStringValue(Constants.AUTH_TOKEN));
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void fetchCart(String authToken) {
        if (ConnectivityHelper.isConnectedToNetwork(getContext())){
            viewModel.fetchData(authToken);
            viewModel.getCartResponse().observe(this, new Observer<ViewCartResponse>() {
                @Override
                public void onChanged(@Nullable ViewCartResponse viewCart) {
                    if (!viewCart.getError()) {
                        if (payloadList != null) {
                            cartDataList.clear();
                            payloadList = viewCart.getPayload();
                            cartDataList.addAll(payloadList);
                            ll_total.setVisibility(View.VISIBLE);
                            tv_total_price.setText("$ " +viewCart.getTotal());
                            bt_checkout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), BillingInfoActivity.class));
                                }
                            });

                            bt_next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    dismiss();
                                }
                            });
                            // adapter.notifyDataSetChanged();
                        }
                    } else {

                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"You're not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpAdapter() {
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_cart.setLayoutManager(manager);
        adapter = new CartAdapter(getContext(), cartDataList, listener);
        rv_cart.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String inputText);
    }


}