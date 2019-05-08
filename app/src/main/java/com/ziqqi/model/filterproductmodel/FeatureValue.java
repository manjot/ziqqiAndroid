package com.ziqqi.model.filterproductmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureValue {

    @SerializedName("featureValueId")
    @Expose
    private String featureValueId;
    @SerializedName("features_value_name")
    @Expose
    private String featuresValueName;

    public String getFeatureValueId() {
        return featureValueId;
    }

    public void setFeatureValueId(String featureValueId) {
        this.featureValueId = featureValueId;
    }

    public String getFeaturesValueName() {
        return featuresValueName;
    }

    public void setFeaturesValueName(String featuresValueName) {
        this.featuresValueName = featuresValueName;
    }

}