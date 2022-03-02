package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

@Keep

public class ProfileInstaData {
    private List<UserElement> users;
    private List<PlaceElement> places;
    private List<HashtagElement> hashtags;
    private boolean hasMore;
    private UUID rankToken;
    private Object clearClientCache;
    private String status;

    @SerializedName("users")
    public List<UserElement> getUsers() {
        return users;
    }

    @SerializedName("users")
    public void setUsers(List<UserElement> value) {
        this.users = value;
    }

    @SerializedName("places")
    public List<PlaceElement> getPlaces() {
        return places;
    }

    @SerializedName("places")
    public void setPlaces(List<PlaceElement> value) {
        this.places = value;
    }

    @SerializedName("hashtags")
    public List<HashtagElement> getHashtags() {
        return hashtags;
    }

    @SerializedName("hashtags")
    public void setHashtags(List<HashtagElement> value) {
        this.hashtags = value;
    }

    @SerializedName("has_more")
    public boolean getHasMore() {
        return hasMore;
    }

    @SerializedName("has_more")
    public void setHasMore(boolean value) {
        this.hasMore = value;
    }

    @SerializedName("rank_token")
    public UUID getRankToken() {
        return rankToken;
    }

    @SerializedName("rank_token")
    public void setRankToken(UUID value) {
        this.rankToken = value;
    }

    @SerializedName("clear_client_cache")
    public Object getClearClientCache() {
        return clearClientCache;
    }

    @SerializedName("clear_client_cache")
    public void setClearClientCache(Object value) {
        this.clearClientCache = value;
    }

    @SerializedName("status")
    public String getStatus() {
        return status;
    }

    @SerializedName("status")
    public void setStatus(String value) {
        this.status = value;
    }
}
