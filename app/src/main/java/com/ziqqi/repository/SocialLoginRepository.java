package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialLoginRepository {
    ApiInterface apiInterface;

    public SocialLoginRepository() {

    }

    public MutableLiveData<LoginResponse> getLoginData(HashMap<String, Object> socialLoginRequest) {

        final MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call;
        if (socialLoginRequest.containsKey("social_id")) {
            call = apiInterface.getLogin(socialLoginRequest);
        } else {
            call = apiInterface.login(socialLoginRequest);
        }
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    loginResponse.setValue(response.body());
                    Log.e("LoginResponse ", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

        return loginResponse;
    }
}
