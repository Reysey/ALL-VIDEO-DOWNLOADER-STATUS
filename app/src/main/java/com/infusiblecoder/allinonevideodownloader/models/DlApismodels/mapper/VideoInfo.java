package com.infusiblecoder.allinonevideodownloader.models.DlApismodels.mapper;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class VideoInfo implements Serializable {
    private String id;
    private String fulltitle;
    private String title;
    @SerializedName("upload_date")
    private String uploadDate;
    @SerializedName("display_id")
    private String displayId;
    private int duration;
    private String description;
    private String thumbnail;
    private String license;
    @SerializedName("view_count")
    private String viewCount;
    @SerializedName("like_count")
    private String likeCount;
    @SerializedName("dislike_count")
    private String dislikeCount;
    @SerializedName("repost_count")
    private String repostCount;
    @SerializedName("average_rating")
    private String averageRating;
    @SerializedName("uploader_id")
    private String uploaderId;
    private String uploader;
    @SerializedName("player_url")
    private String playerUrl;
    @SerializedName("webpage_url")
    private String webpageUrl;
    @SerializedName("webpage_url_basename")
    private String webpageUrlBasename;
    private String resolution;
    private int width;
    private int height;
    private String format;
    @SerializedName("format_id")
    private String formatId;
    private String ext;
    @SerializedName("http_headers")
    private Map<String, String> httpHeaders;
    private ArrayList<String> categories;
    private ArrayList<String> tags;
    @SerializedName("requested_formats")
    private ArrayList<VideoFormat> requestedFormats;
    private ArrayList<VideoFormat> formats;
    private ArrayList<VideoThumbnail> thumbnails;
    @SerializedName("manifest_url")
    private String manifestUrl;
    private String url;

    public String getId() {
        return this.id;
    }

    public String getFulltitle() {
        return this.fulltitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUploadDate() {
        return this.uploadDate;
    }

    public String getDisplayId() {
        return this.displayId;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getDescription() {
        return this.description;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getLicense() {
        return this.license;
    }

    public String getViewCount() {
        return this.viewCount;
    }

    public String getLikeCount() {
        return this.likeCount;
    }

    public String getDislikeCount() {
        return this.dislikeCount;
    }

    public String getRepostCount() {
        return this.repostCount;
    }

    public String getAverageRating() {
        return this.averageRating;
    }

    public String getUploaderId() {
        return this.uploaderId;
    }

    public String getUploader() {
        return this.uploader;
    }

    public String getPlayerUrl() {
        return this.playerUrl;
    }

    public String getWebpageUrl() {
        return this.webpageUrl;
    }

    public String getWebpageUrlBasename() {
        return this.webpageUrlBasename;
    }

    public String getResolution() {
        return this.resolution;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getFormat() {
        return this.format;
    }

    public String getFormatId() {
        return this.formatId;
    }

    public String getExt() {
        return this.ext;
    }

    public Map<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public ArrayList<VideoFormat> getRequestedFormats() {
        return this.requestedFormats;
    }

    public ArrayList<VideoFormat> getFormats() {
        return this.formats;
    }

    public ArrayList<VideoThumbnail> getThumbnails() {
        return this.thumbnails;
    }

    public String getManifestUrl() {
        return this.manifestUrl;
    }

    public String getUrl() {
        return this.url;
    }
}
