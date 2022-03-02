package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class HolderSearchViewed {

    private String id;
    private String image;
    private String name;
    private String profileUrl;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getProfileUrl() {
        return this.profileUrl;
    }

    public void setProfileUrl(String str) {
        this.profileUrl = str;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String str) {
        this.image = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }
}
