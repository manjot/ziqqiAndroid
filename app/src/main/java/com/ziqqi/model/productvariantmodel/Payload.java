package com.ziqqi.model.productvariantmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("attrId")
    @Expose
    private String attrId;
    @SerializedName("attributeName")
    @Expose
    private String attributeName;
    @SerializedName("attributeValue")
    @Expose
    private List<AttributeValue> attributeValue = null;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public List<AttributeValue> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<AttributeValue> attributeValue) {
        this.attributeValue = attributeValue;
    }
}
