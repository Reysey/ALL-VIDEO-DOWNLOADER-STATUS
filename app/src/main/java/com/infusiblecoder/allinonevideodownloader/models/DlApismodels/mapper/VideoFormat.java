package com.infusiblecoder.allinonevideodownloader.models.DlApismodels.mapper;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;


public class VideoFormat implements Serializable {
    private int asr;
    private int tbr;
    private int abr;
    private String format;
    @SerializedName("format_id")
    private String formatId;
    @SerializedName("format_note")
    private String formatNote;
    private String ext;
    private int preference;
    private String vcodec;
    private String acodec;
    private int width;
    private int height;
    private long filesize;
    private int fps;
    private String url;
    @SerializedName("manifest_url")
    private String manifestUrl;
    @SerializedName("http_headers")
    private Map<String, String> httpHeaders;

    public int getAsr() {
        return this.asr;
    }

    public int getTbr() {
        return this.tbr;
    }

    public int getAbr() {
        return this.abr;
    }

    public String getFormat() {
        return this.format;
    }

    public String getFormatId() {
        return this.formatId;
    }

    public String getFormatNote() {
        return this.formatNote;
    }

    public String getExt() {
        return this.ext;
    }

    public int getPreference() {
        return this.preference;
    }

    public String getVcodec() {
        return this.vcodec;
    }

    public String getAcodec() {
        return this.acodec;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public long getFilesize() {
        return this.filesize;
    }

    public int getFps() {
        return this.fps;
    }

    public String getUrl() {
        return this.url;
    }

    public String getManifestUrl() {
        return this.manifestUrl;
    }

    public Map<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }
}
