package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class EdgeInfo {
    public NodeInfo getNode() {
        return this.node;
    }

    public void setNode(NodeInfo node) {
        this.node = node;
    }

    NodeInfo node;
}
