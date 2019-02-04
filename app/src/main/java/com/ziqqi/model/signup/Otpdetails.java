
package com.ziqqi.model.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Otpdetails {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

}
