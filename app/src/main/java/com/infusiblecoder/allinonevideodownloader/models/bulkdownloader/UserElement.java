package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep

public class UserElement implements Serializable {
    @SerializedName("position")
    private long position;
    @SerializedName("user")
    private UserUser user;

    public long getPosition() {
        return position;
    }

    @SerializedName("position")
    public void setPosition(long value) {
        this.position = value;
    }

    @SerializedName("user")
    public UserUser getUser() {
        return user;
    }

    @SerializedName("user")
    public void setUser(UserUser value) {
        this.user = value;
    }
}

