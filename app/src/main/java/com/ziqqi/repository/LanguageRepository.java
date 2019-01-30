package com.ziqqi.repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.retrofit.ApiClient;
import com.ziqqi.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageRepository {
    private ApiInterface apiInterface;

    public LanguageRepository() {

    }

    public MutableLiveData<LanguageModel> getLanguageData() {
        final MutableLiveData<LanguageModel> languageModel = new MutableLiveData<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LanguageModel> call = apiInterface.getLanguageData();
        call.enqueue(new Callback<LanguageModel>() {
            @Override
            public void onResponse(Call<LanguageModel> call, Response<LanguageModel> response) {
                if (response.body() != null) {
                    languageModel.setValue(response.body());
                    Log.e("LanguageData ", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<LanguageModel> call, Throwable t) {

            }
        });

        return languageModel;
    }

}
