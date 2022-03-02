package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class UserInfoInstaProfile {
    public EdgeOwnerToTimelineMedia getEdge_owner_to_timeline_media() {
        return this.edge_owner_to_timeline_media;
    }

    public void setEdge_owner_to_timeline_media(EdgeOwnerToTimelineMedia edge_owner_to_timeline_media) {
        this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
    }

    EdgeOwnerToTimelineMedia edge_owner_to_timeline_media;
}
