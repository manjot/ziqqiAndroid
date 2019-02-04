package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.repository.Repository;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<BannerImageModel> data;
    private MutableLiveData<HomeCategoriesResponse> homeCategoryResponse;
    private Repository repository;
    /*public final ObservableField<Integer> progressVisibility = new ObservableField<>(View.GONE);
    public final ObservableField<Integer> mainLayoutVisibility = new ObservableField<>(View.VISIBLE);*/

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void init() {
        if (this.data != null) {

        } else {
            data = repository.getHomeBanners();
        }
    }

    public void initHomeCategories() {
        if (homeCategoryResponse != null) {
          /*  progressVisibility.set(View.GONE);
            mainLayoutVisibility.set(View.VISIBLE);*/
        } else {
         /*   progressVisibility.set(View.VISIBLE);
            mainLayoutVisibility.set(View.GONE);*/
            homeCategoryResponse = repository.getHomeCategories();
        }
    }

    public MutableLiveData<BannerImageModel> getHomeBanners() {
        return this.data;
    }

    public MutableLiveData<HomeCategoriesResponse> getHomeCategoryResponse() {
        return homeCategoryResponse;
    }
}
