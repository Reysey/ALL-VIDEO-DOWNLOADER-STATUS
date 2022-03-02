package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep

public class PlacePlace {
    private Location location;
    private String title;
    private String subtitle;
    private List<Object> mediaBundles;
    private String slug;

    @SerializedName("location")
    public Location getLocation() {
        return location;
    }

    @SerializedName("location")
    public void setLocation(Location value) {
        this.location = value;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String value) {
        this.title = value;
    }

    @SerializedName("subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    @SerializedName("subtitle")
    public void setSubtitle(String value) {
        this.subtitle = value;
    }

    @SerializedName("media_bundles")
    public List<Object> getMediaBundles() {
        return mediaBundles;
    }

    @SerializedName("media_bundles")
    public void setMediaBundles(List<Object> value) {
        this.mediaBundles = value;
    }

    @SerializedName("slug")
    public String getSlug() {
        return slug;
    }

    @SerializedName("slug")
    public void setSlug(String value) {
        this.slug = value;
    }
}
