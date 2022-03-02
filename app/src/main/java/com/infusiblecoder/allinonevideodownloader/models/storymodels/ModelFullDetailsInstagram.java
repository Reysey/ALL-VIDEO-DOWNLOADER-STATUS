package com.infusiblecoder.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class ModelFullDetailsInstagram implements Serializable {

    @SerializedName("user_detail")
    private UserDetailModel user_detail;

    @SerializedName("reel_feed")
    private ModelFeedDetails reel_feed;

    public UserDetailModel getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(UserDetailModel user_detail) {
        this.user_detail = user_detail;
    }

    public ModelFeedDetails getReel_feed() {
        return reel_feed;
    }

    public void setReel_feed(ModelFeedDetails reel_feed) {
        this.reel_feed = reel_feed;
    }
}
