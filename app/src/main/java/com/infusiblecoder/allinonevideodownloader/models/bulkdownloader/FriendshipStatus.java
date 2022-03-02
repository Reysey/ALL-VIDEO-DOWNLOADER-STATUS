package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep

public class FriendshipStatus {
    private boolean following;
    private boolean isPrivate;
    private boolean incomingRequest;
    private boolean outgoingRequest;
    private boolean isBestie;
    private boolean isRestricted;

    @SerializedName("following")
    public boolean getFollowing() {
        return following;
    }

    @SerializedName("following")
    public void setFollowing(boolean value) {
        this.following = value;
    }

    @SerializedName("is_private")
    public boolean getIsPrivate() {
        return isPrivate;
    }

    @SerializedName("is_private")
    public void setIsPrivate(boolean value) {
        this.isPrivate = value;
    }

    @SerializedName("incoming_request")
    public boolean getIncomingRequest() {
        return incomingRequest;
    }

    @SerializedName("incoming_request")
    public void setIncomingRequest(boolean value) {
        this.incomingRequest = value;
    }

    @SerializedName("outgoing_request")
    public boolean getOutgoingRequest() {
        return outgoingRequest;
    }

    @SerializedName("outgoing_request")
    public void setOutgoingRequest(boolean value) {
        this.outgoingRequest = value;
    }

    @SerializedName("is_bestie")
    public boolean getIsBestie() {
        return isBestie;
    }

    @SerializedName("is_bestie")
    public void setIsBestie(boolean value) {
        this.isBestie = value;
    }

    @SerializedName("is_restricted")
    public boolean getIsRestricted() {
        return isRestricted;
    }

    @SerializedName("is_restricted")
    public void setIsRestricted(boolean value) {
        this.isRestricted = value;
    }
}
