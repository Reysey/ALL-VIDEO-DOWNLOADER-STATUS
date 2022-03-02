package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class DashInfo implements Serializable {
    @SerializedName("is_dash_eligible")
    public boolean getIs_dash_eligible() {
        return this.is_dash_eligible;
    }

    public void setIs_dash_eligible(boolean is_dash_eligible) {
        this.is_dash_eligible = is_dash_eligible;
    }

    boolean is_dash_eligible;

    @SerializedName("video_dash_manifest")
    public Object getVideo_dash_manifest() {
        return this.video_dash_manifest;
    }

    public void setVideo_dash_manifest(Object video_dash_manifest) {
        this.video_dash_manifest = video_dash_manifest;
    }

    Object video_dash_manifest;

    @SerializedName("number_of_qualities")
    public int getNumber_of_qualities() {
        return this.number_of_qualities;
    }

    public void setNumber_of_qualities(int number_of_qualities) {
        this.number_of_qualities = number_of_qualities;
    }

    int number_of_qualities;
}
