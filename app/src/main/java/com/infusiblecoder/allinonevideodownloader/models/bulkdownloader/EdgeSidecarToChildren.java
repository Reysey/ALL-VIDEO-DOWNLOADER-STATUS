package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;



import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep

public class EdgeSidecarToChildren implements Serializable {
    @SerializedName("edges")
    public List<EdgeInfo> getEdges() {
        return this.edges;
    }

    public void setEdges(List<EdgeInfo> edges) {
        this.edges = edges;
    }

    List<EdgeInfo> edges;
}
