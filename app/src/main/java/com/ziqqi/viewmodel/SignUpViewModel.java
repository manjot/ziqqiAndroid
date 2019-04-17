package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.model.signup.SignUpResponse;
import com.ziqqi.repository.SignUpRepository;
import com.ziqqi.utils.Utils;

import java.util.HashMap;

public class SignUpViewModel extends AndroidViewModel {
//    public final ObservableField<String> email = new ObservableField<>();
//    public final ObservableField<String> password = new ObservableField<>();
//    public final ObservableField<String> firstName = new ObservableField<>();
//    public final ObservableField<String> lastName = new ObservableField<>();
//    public final ObservableField<String> mobileNumber = new ObservableField<>();
//    public final ObservableField<Boolean> male = new ObservableField<>(false);
//    public final ObservableField<Boolean> female = new ObservableField<>(false);
//    public final ObservableField<Integer> progressVisibility = new ObservableField<>(View.GONE);
//    public final ObservableField<Integer> mainLayoutVisibility = new ObservableField<>(View.VISIBLE);


    SignUpRepository repository;
    MutableLiveData<SignUpResponse> signUpResponse;
    MutableLiveData<VerifyOtpResponse> verifyOtpResponse;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        repository = new SignUpRepository();
    }

//    public boolean isValid() {
//        if (email.get() == null || password.get() == null || mobileNumber.get() == null || firstName.get() == null || lastName.get() == null) {
//            Utils.ShowToast(getApplication().getApplicationContext(), "Fields can not be emplty");
//            return false;
//        } else if (!Utils.isValidEmail(email.get())) {
//            Log.e("EMAIL ", email.get());
//            Utils.ShowToast(getApplication().getApplicationContext(), "Wrong Email Address");
//            return false;
//        } else if (Utils.isValidName(firstName.get()) || Utils.isValidName(lastName.get())) {
//            Log.e("EMAIL ", email.get());
//            if (Utils.isValidName(firstName.get())) {
//                Utils.ShowToast(getApplication().getApplicationContext(), "Wrong First Name");
//            } else {
//                Utils.ShowToast(getApplication().getApplicationContext(), "Wrong Last Name");
//            }
//            return false;
//        } else if (password.get().length() < 6) {
//            Utils.ShowToast(getApplication().getApplicationContext(), "Password must contain minimum 6 letters");
//            return false;
//        }
//        else if (mobileNumber.get().length() != 10 || !Utils.isValidPhone(mobileNumber.get())) {
//            Utils.ShowToast(getApplication().getApplicationContext(), "Wrong Mobile Number");
//            return false;
//        }
//        else return true;
//    }

    public void init(HashMap<String, String> hashMap) {
            signUpResponse = repository.getSignUpData(hashMap);
    }

    public MutableLiveData<SignUpResponse> getSignUpResponse() {
            return signUpResponse;
    }

    public MutableLiveData<VerifyOtpResponse> getVerifyOtp() {
        if (verifyOtpResponse != null) {
            return verifyOtpResponse;
        } else return null;
    }

    public void verifyOtp(int customerId, int otp) {
        if (verifyOtpResponse != null) {

        } else {
            verifyOtpResponse = repository.verifyOtpResponse(customerId, otp);
        }
    }
}
