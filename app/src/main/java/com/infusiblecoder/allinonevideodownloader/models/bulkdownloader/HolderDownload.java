package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class HolderDownload {
    private boolean isVideo;
    private String postURL;
    private String postURLVideo;
    private String profileId;
    private String shortCode;

    public String getProfileId() {
        return this.profileId;
    }

    public void setProfileId(String str) {
        this.profileId = str;
    }

    public String getPostURL() {
        return this.postURL;
    }

    public void setPostURL(String str) {
        this.postURL = str;
    }

    public String getPostURLVideo() {
        return this.postURLVideo;
    }

    public void setPostURLVideo(String str) {
        this.postURLVideo = str;
    }

    public String getShortCode() {
        return this.shortCode;
    }

    public void setShortCode(String str) {
        this.shortCode = str;
    }

    public boolean getIsVideo() {
        return this.isVideo;
    }

    public void setIsVideo(boolean z) {
        this.isVideo = z;
    }
}



