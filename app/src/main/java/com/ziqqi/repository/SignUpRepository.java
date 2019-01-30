package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository {

    private ApiInterface apiInterface;

    public SignUpRepository() {

    }

    public MutableLiveData<SignUpResponse> getSignUpData(HashMap<String, String> hashMap) {
        final MutableLiveData<SignUpResponse> languageModel = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SignUpResponse> call = apiInterface.getSignUpData(hashMap);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body() != null) {
                    languageModel.setValue(response.body());
                    Log.e("LanguageData ", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

            }
        });

        return languageModel;
    }
}
