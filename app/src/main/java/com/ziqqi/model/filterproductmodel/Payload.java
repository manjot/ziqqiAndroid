package com.ziqqi.model.filterproductmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("featureId")
    @Expose
    private String featureId;
    @SerializedName("featureName")
    @Expose
    private String featureName;
    @SerializedName("featureValue")
    @Expose
    private List<FeatureValue> featureValue = null;

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public List<FeatureValue> getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(List<FeatureValue> featureValue) {
        this.featureValue = featureValue;
    }

}