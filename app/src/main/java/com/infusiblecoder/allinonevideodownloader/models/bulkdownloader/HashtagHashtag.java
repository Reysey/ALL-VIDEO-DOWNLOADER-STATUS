package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep

public class HashtagHashtag {
    private String name;
    private double id;
    private long mediaCount;
    private boolean useDefaultAvatar;
    private String profilePicURL;
    private String searchResultSubtitle;

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String value) {
        this.name = value;
    }

    @SerializedName("id")
    public double getID() {
        return id;
    }

    @SerializedName("id")
    public void setID(double value) {
        this.id = value;
    }

    @SerializedName("media_count")
    public long getMediaCount() {
        return mediaCount;
    }

    @SerializedName("media_count")
    public void setMediaCount(long value) {
        this.mediaCount = value;
    }

    @SerializedName("use_default_avatar")
    public boolean getUseDefaultAvatar() {
        return useDefaultAvatar;
    }

    @SerializedName("use_default_avatar")
    public void setUseDefaultAvatar(boolean value) {
        this.useDefaultAvatar = value;
    }

    @SerializedName("profile_pic_url")
    public String getProfilePicURL() {
        return profilePicURL;
    }

    @SerializedName("profile_pic_url")
    public void setProfilePicURL(String value) {
        this.profilePicURL = value;
    }

    @SerializedName("search_result_subtitle")
    public String getSearchResultSubtitle() {
        return searchResultSubtitle;
    }

    @SerializedName("search_result_subtitle")
    public void setSearchResultSubtitle(String value) {
        this.searchResultSubtitle = value;
    }
}
