
package com.ziqqi.model.languagemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("language_name")
    @Expose
    private String languageName;
    @SerializedName("language_shortname")
    @Expose
    private String languageShortname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageShortname() {
        return languageShortname;
    }

    public void setLanguageShortname(String languageShortname) {
        this.languageShortname = languageShortname;
    }

}
