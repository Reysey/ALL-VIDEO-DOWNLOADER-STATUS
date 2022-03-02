package com.infusiblecoder.allinonevideodownloader.models.tiktoknewmodels;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


@Keep
public class VideoModelTiktok implements Serializable {
    @SerializedName("author")
    public AuthorModel author;
    @SerializedName("author_id")
    public String author_id;
    @SerializedName("author_nickname")
    public String author_nickname;
    @SerializedName("author_unique_id")
    public String author_unique_id;
    @SerializedName("aweme_link")
    public String aweme_link;
    @SerializedName("cover_link")
    public String cover_link;
    @SerializedName("create_time")
    public String create_time;
    @SerializedName("text")
    public String description;
    @SerializedName("duration")
    public String duration;
    @SerializedName("error")
    public ErrorModel error;
    @SerializedName("itemId")
    public String itemId;
    public String local_url_video;
    @SerializedName("music")
    public ArrayList<MusicModel> music;
    public String size;
    @SerializedName("thumbnails")
    public ArrayList<String> thumbnails;
    public String time;
    public String title;
    @SerializedName("watermark_link")
    public String url_video;
    @SerializedName("no_watermark_link")
    public String video_without_watermark_url;

    public VideoModelTiktok() {

    }

    public AuthorModel getAuthor() {
        return author;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_nickname() {
        return author_nickname;
    }

    public void setAuthor_nickname(String author_nickname) {
        this.author_nickname = author_nickname;
    }

    public String getAuthor_unique_id() {
        return author_unique_id;
    }

    public void setAuthor_unique_id(String author_unique_id) {
        this.author_unique_id = author_unique_id;
    }

    public String getAweme_link() {
        return aweme_link;
    }

    public void setAweme_link(String aweme_link) {
        this.aweme_link = aweme_link;
    }

    public String getCover_link() {
        return cover_link;
    }

    public void setCover_link(String cover_link) {
        this.cover_link = cover_link;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLocal_url_video() {
        return local_url_video;
    }

    public void setLocal_url_video(String local_url_video) {
        this.local_url_video = local_url_video;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public String getVideo_without_watermark_url() {
        return video_without_watermark_url;
    }

    public void setVideo_without_watermark_url(String video_without_watermark_url) {
        this.video_without_watermark_url = video_without_watermark_url;
    }

    public ArrayList<MusicModel> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<MusicModel> music) {
        this.music = music;
    }

    public ArrayList<String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<String> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
