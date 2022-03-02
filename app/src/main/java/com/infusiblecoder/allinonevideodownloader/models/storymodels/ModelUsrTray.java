package com.infusiblecoder.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class ModelUsrTray implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private ModelUserData user;

    @SerializedName("media_count")
    private int media_count;

    @SerializedName("items")
    private ArrayList<ModelInstaItem> items;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelUserData getUser() {
        return user;
    }

    public void setUser(ModelUserData user) {
        this.user = user;
    }

    public int getMedia_count() {
        return media_count;
    }

    public void setMedia_count(int media_count) {
        this.media_count = media_count;
    }

    public ArrayList<ModelInstaItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ModelInstaItem> items) {
        this.items = items;
    }
}
