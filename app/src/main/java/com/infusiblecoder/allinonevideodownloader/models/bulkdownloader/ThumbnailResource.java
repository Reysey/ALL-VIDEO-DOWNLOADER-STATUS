package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class ThumbnailResource {
    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    String src;

    public int getConfig_width() {
        return this.config_width;
    }

    public void setConfig_width(int config_width) {
        this.config_width = config_width;
    }

    int config_width;

    public int getConfig_height() {
        return this.config_height;
    }

    public void setConfig_height(int config_height) {
        this.config_height = config_height;
    }

    int config_height;
}
