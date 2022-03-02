package com.infusiblecoder.allinonevideodownloader.models.tiktoknewmodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Keep

public class AuthorModel implements Serializable {
    @SerializedName("name")
    public String name;
    @SerializedName("url")
    public String url;

    public AuthorModel() {


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
