package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;



import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep

public class EdgeMutualFollowedBy implements Serializable {
    @SerializedName("count")
    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;

    @SerializedName("edges")
    public List<Object> getEdges() {
        return this.edges;
    }

    public void setEdges(List<Object> edges) {
        this.edges = edges;
    }

    List<Object> edges;
}
