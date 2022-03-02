package com.infusiblecoder.allinonevideodownloader.models.tiktoknewmodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Keep
public class ErrorModel implements Serializable {
    @SerializedName("code")
    public String code = "";
    @SerializedName("message")
    public String message = "";
}
