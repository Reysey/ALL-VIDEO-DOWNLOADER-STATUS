package com.infusiblecoder.allinonevideodownloader.models.storymodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class ModelVersionOfImage implements Serializable {

    @SerializedName("candidates")
    private ArrayList<ModelCandi> candidates;

    public ArrayList<ModelCandi> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<ModelCandi> candidates) {
        this.candidates = candidates;
    }
}
