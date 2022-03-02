package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep

public class Location {
    private String pk;
    private String shortName;
    private long facebookPlacesID;
    private String externalSource;
    private String name;
    private String address;
    private String city;
    private double lng;
    private double lat;

    @SerializedName("pk")
    public String getPk() {
        return pk;
    }

    @SerializedName("pk")
    public void setPk(String value) {
        this.pk = value;
    }

    @SerializedName("short_name")
    public String getShortName() {
        return shortName;
    }

    @SerializedName("short_name")
    public void setShortName(String value) {
        this.shortName = value;
    }

    @SerializedName("facebook_places_id")
    public long getFacebookPlacesID() {
        return facebookPlacesID;
    }

    @SerializedName("facebook_places_id")
    public void setFacebookPlacesID(long value) {
        this.facebookPlacesID = value;
    }

    @SerializedName("external_source")
    public String getExternalSource() {
        return externalSource;
    }

    @SerializedName("external_source")
    public void setExternalSource(String value) {
        this.externalSource = value;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String value) {
        this.name = value;
    }

    @SerializedName("address")
    public String getAddress() {
        return address;
    }

    @SerializedName("address")
    public void setAddress(String value) {
        this.address = value;
    }

    @SerializedName("city")
    public String getCity() {
        return city;
    }

    @SerializedName("city")
    public void setCity(String value) {
        this.city = value;
    }

    @SerializedName("lng")
    public double getLng() {
        return lng;
    }

    @SerializedName("lng")
    public void setLng(double value) {
        this.lng = value;
    }

    @SerializedName("lat")
    public double getLat() {
        return lat;
    }

    @SerializedName("lat")
    public void setLat(double value) {
        this.lat = value;
    }
}
