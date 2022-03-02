package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep

public class PlaceElement {
    private PlacePlace place;
    private long position;

    @SerializedName("place")
    public PlacePlace getPlace() {
        return place;
    }

    @SerializedName("place")
    public void setPlace(PlacePlace value) {
        this.place = value;
    }

    @SerializedName("position")
    public long getPosition() {
        return position;
    }

    @SerializedName("position")
    public void setPosition(long value) {
        this.position = value;
    }
}
