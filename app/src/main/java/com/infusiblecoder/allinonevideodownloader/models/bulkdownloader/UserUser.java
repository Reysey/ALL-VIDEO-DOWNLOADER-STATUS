package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep

public class UserUser implements Serializable {
    @SerializedName("pk")
    public String pk;
    @SerializedName("username")
    public String username;
    @SerializedName("full_name")
    public String fullName;
    @SerializedName("is_private")
    public boolean is_private;
    @SerializedName("profile_pic_url")
    public String profile_pic_url;
    @SerializedName("profile_pic_id")
    public String profile_pic_id;
    @SerializedName("is_verified")
    public boolean is_verified;
    @SerializedName("has_anonymous_profile_picture")
    public boolean has_anonymous_profile_picture;
    @SerializedName("mutual_followers_count")
    public long mutual_followers_count;
    @SerializedName("account_badges")
    public List<Object> accountBadges;
    @SerializedName("latest_reel_media")
    public long latestReelMedia;

    UserUser() {
    }

}