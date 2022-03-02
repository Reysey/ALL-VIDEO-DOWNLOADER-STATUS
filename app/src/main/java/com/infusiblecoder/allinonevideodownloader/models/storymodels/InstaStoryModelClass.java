package com.infusiblecoder.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class InstaStoryModelClass implements Serializable {

    @SerializedName("tray")
    private ArrayList<ModelUsrTray> tray;

    @SerializedName("status")
    private String status;

    public ArrayList<ModelUsrTray> getTray() {
        return tray;
    }

    public void setTray(ArrayList<ModelUsrTray> tray) {
        this.tray = tray;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
