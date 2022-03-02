package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;



import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep

public class EdgeFelixVideoTimeline implements Serializable {
    @SerializedName("count")
    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;

    @SerializedName("page_info")
    public PageInfo getPage_info() {
        return this.page_info;
    }

    public void setPage_info(PageInfo page_info) {
        this.page_info = page_info;
    }

    PageInfo page_info;

    @SerializedName("edges")
    public List<EdgeInfo> getEdges() {
        return this.edges;
    }

    public void setEdges(List<EdgeInfo> edges) {
        this.edges = edges;
    }

    List<EdgeInfo> edges;
}
