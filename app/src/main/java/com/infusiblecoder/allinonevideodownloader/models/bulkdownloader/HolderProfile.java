package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class HolderProfile {
    private String accessibility_caption;
    private String commentCount;
    private boolean footOrNot;
    private boolean headOrNot;

    private String id;
    private int imHeight;
    private int imWidth;
    private boolean isAd;
    private boolean isLimitReached;
    private boolean isVideo;
    private String likes;
    private String name;
    private String post;
    private String postId;
    private String postImage;
    private String postImage150;
    private String postImage240;
    private String postImage360;
    private String postImage480;
    private String postImage640;
    private String postImageHighReso;
    private String postImageLowReso;
    private String postImageThumb;
    private String postUrl;
    private String profileBottomUrl;
    private String profileImage;
    private boolean progress;
    private String shortcode;
    private String time;
    private String user_number_id;

    public String getPostUrl() {
        return this.postUrl;
    }

    public void setPostUrl(String str) {
        this.postUrl = str;
    }

    public String getUser_number_id() {
        return this.user_number_id;
    }

    public void setUser_number_id(String str) {
        this.user_number_id = str;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String str) {
        this.profileImage = str;
    }

    public String getPostImage() {
        return this.postImage;
    }

    public void setPostImage(String str) {
        this.postImage = str;
    }

    public String getPostImageHighReso() {
        return this.postImageHighReso;
    }

    public void setPostImageHighReso(String str) {
        this.postImageHighReso = str;
    }

    public String getPostImage640() {
        return this.postImage640;
    }

    public void setPostImage640(String str) {
        this.postImage640 = str;
    }

    public String getPostImage150() {
        return this.postImage150;
    }

    public void setPostImage150(String str) {
        this.postImage150 = str;
    }

    public String getPostImage240() {
        return this.postImage240;
    }

    public void setPostImage240(String str) {
        this.postImage240 = str;
    }

    public String getPostImage360() {
        return this.postImage360;
    }

    public void setPostImage360(String str) {
        this.postImage360 = str;
    }

    public String getPostImage480() {
        return this.postImage480;
    }

    public void setPostImage480(String str) {
        this.postImage480 = str;
    }

    public String getPostImageThumb() {
        return this.postImageThumb;
    }

    public void setPostImageThumb(String str) {
        this.postImageThumb = str;
    }

    public String getPostImageLowReso() {
        return this.postImageLowReso;
    }

    public void setPostImageLowReso(String str) {
        this.postImageLowReso = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getPost() {
        return this.post;
    }

    public void setPost(String str) {
        this.post = str;
    }

    public String getLikeCount() {
        return this.likes;
    }

    public void setLikeCount(String str) {
        this.likes = str;
    }

    public String getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(String str) {
        this.commentCount = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public boolean getIsVideo() {
        return this.isVideo;
    }

    public void setIsVideo(boolean z) {
        this.isVideo = z;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String str) {
        this.postId = str;
    }

    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String str) {
        this.shortcode = str;
    }

    public String getAccessibilityCaption() {
        return this.accessibility_caption;
    }

    public void setAccessibilityCaption(String str) {
        this.accessibility_caption = str;
    }

    public int getImWidth() {
        return this.imWidth;
    }

    public void setImWidth(int i) {
        this.imWidth = i;
    }

    public int getImHeight() {
        return this.imHeight;
    }

    public void setImHeight(int i) {
        this.imHeight = i;
    }

    public boolean getHead() {
        return this.headOrNot;
    }

    public void setHead(boolean z) {
        this.headOrNot = z;
    }

    public boolean getFoot() {
        return this.footOrNot;
    }

    public void setFoot(boolean z) {
        this.footOrNot = z;
    }

    public boolean getIsAd() {
        return this.isAd;
    }

    public void setIsAd(boolean z) {
        this.isAd = z;
    }

    public boolean getIsLimitReached() {
        return this.isLimitReached;
    }

    public void setIsLimitReached(boolean z) {
        this.isLimitReached = z;
    }

    public boolean getProgress() {
        return this.progress;
    }

    public void setProgress(boolean z) {
        this.progress = z;
    }
}
