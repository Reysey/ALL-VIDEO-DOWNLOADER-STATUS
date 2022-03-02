package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep

public class LocationInsta implements Serializable {
    @SerializedName("id")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    @SerializedName("has_public_page")
    public boolean getHas_public_page() {
        return this.has_public_page;
    }

    public void setHas_public_page(boolean has_public_page) {
        this.has_public_page = has_public_page;
    }

    boolean has_public_page;

    @SerializedName("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    @SerializedName("slug")
    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    String slug;
}
