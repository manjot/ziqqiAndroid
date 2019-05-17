package com.ziqqi.model.productvariantmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeValue {

    @SerializedName("product_variant_id")
    @Expose
    private String productVariantId;
    @SerializedName("attributes_id")
    @Expose
    private String attributesId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value_name")
    @Expose
    private String valueName;
    @SerializedName("attributes_value_id")
    @Expose
    private String attributesValueId;

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getAttributesId() {
        return attributesId;
    }

    public void setAttributesId(String attributesId) {
        this.attributesId = attributesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getAttributesValueId() {
        return attributesValueId;
    }

    public void setAttributesValueId(String attributesValueId) {
        this.attributesValueId = attributesValueId;
    }

}