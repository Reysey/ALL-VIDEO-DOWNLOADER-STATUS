package com.infusiblecoder.allinonevideodownloader.models.DlApismodels.mapper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoThumbnail {
    private String url;
    private String id;

    public String getUrl() {
        return this.url;
    }

    public String getId() {
        return this.id;
    }
}
