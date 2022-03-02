package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep

public class UserInfoInsta implements Serializable {
    @SerializedName("full_name")
    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    String full_name;

    @SerializedName("id")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    @SerializedName("is_verified")
    public boolean getIs_verified() {
        return this.is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    boolean is_verified;

    @SerializedName("profile_pic_url")
    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    String profile_pic_url;

    @SerializedName("username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    @SerializedName("biography")
    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    String biography;

    @SerializedName("blocked_by_viewer")
    public boolean getBlocked_by_viewer() {
        return this.blocked_by_viewer;
    }

    public void setBlocked_by_viewer(boolean blocked_by_viewer) {
        this.blocked_by_viewer = blocked_by_viewer;
    }

    boolean blocked_by_viewer;

    @SerializedName("restricted_by_viewer")
    public boolean getRestricted_by_viewer() {
        return this.restricted_by_viewer;
    }

    public void setRestricted_by_viewer(boolean restricted_by_viewer) {
        this.restricted_by_viewer = restricted_by_viewer;
    }

    boolean restricted_by_viewer;

    @SerializedName("country_block")
    public boolean getCountry_block() {
        return this.country_block;
    }

    public void setCountry_block(boolean country_block) {
        this.country_block = country_block;
    }

    boolean country_block;

    @SerializedName("external_url")
    public Object getExternal_url() {
        return this.external_url;
    }

    public void setExternal_url(Object external_url) {
        this.external_url = external_url;
    }

    Object external_url;

    @SerializedName("external_url_linkshimmed")
    public Object getExternal_url_linkshimmed() {
        return this.external_url_linkshimmed;
    }

    public void setExternal_url_linkshimmed(Object external_url_linkshimmed) {
        this.external_url_linkshimmed = external_url_linkshimmed;
    }

    Object external_url_linkshimmed;

    @SerializedName("edge_followed_by")
    public EdgeFollowedBy getEdge_followed_by() {
        return this.edge_followed_by;
    }

    public void setEdge_followed_by(EdgeFollowedBy edge_followed_by) {
        this.edge_followed_by = edge_followed_by;
    }

    EdgeFollowedBy edge_followed_by;

    @SerializedName("fbid")
    public String getFbid() {
        return this.fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    String fbid;

    @SerializedName("followed_by_viewer")
    public boolean getFollowed_by_viewer() {
        return this.followed_by_viewer;
    }

    public void setFollowed_by_viewer(boolean followed_by_viewer) {
        this.followed_by_viewer = followed_by_viewer;
    }

    boolean followed_by_viewer;

    @SerializedName("edge_follow")
    public EdgeFollow getEdge_follow() {
        return this.edge_follow;
    }

    public void setEdge_follow(EdgeFollow edge_follow) {
        this.edge_follow = edge_follow;
    }

    EdgeFollow edge_follow;

    @SerializedName("follows_viewer")
    public boolean getFollows_viewer() {
        return this.follows_viewer;
    }

    public void setFollows_viewer(boolean follows_viewer) {
        this.follows_viewer = follows_viewer;
    }

    boolean follows_viewer;

    @SerializedName("has_ar_effects")
    public boolean getHas_ar_effects() {
        return this.has_ar_effects;
    }

    public void setHas_ar_effects(boolean has_ar_effects) {
        this.has_ar_effects = has_ar_effects;
    }

    boolean has_ar_effects;

    @SerializedName("has_clips")
    public boolean getHas_clips() {
        return this.has_clips;
    }

    public void setHas_clips(boolean has_clips) {
        this.has_clips = has_clips;
    }

    boolean has_clips;

    @SerializedName("has_guides")
    public boolean getHas_guides() {
        return this.has_guides;
    }

    public void setHas_guides(boolean has_guides) {
        this.has_guides = has_guides;
    }

    boolean has_guides;

    @SerializedName("has_channel")
    public boolean getHas_channel() {
        return this.has_channel;
    }

    public void setHas_channel(boolean has_channel) {
        this.has_channel = has_channel;
    }

    boolean has_channel;

    @SerializedName("has_blocked_viewer")
    public boolean getHas_blocked_viewer() {
        return this.has_blocked_viewer;
    }

    public void setHas_blocked_viewer(boolean has_blocked_viewer) {
        this.has_blocked_viewer = has_blocked_viewer;
    }

    boolean has_blocked_viewer;

    @SerializedName("highlight_reel_count")
    public int getHighlight_reel_count() {
        return this.highlight_reel_count;
    }

    public void setHighlight_reel_count(int highlight_reel_count) {
        this.highlight_reel_count = highlight_reel_count;
    }

    int highlight_reel_count;

    @SerializedName("has_requested_viewer")
    public boolean getHas_requested_viewer() {
        return this.has_requested_viewer;
    }

    public void setHas_requested_viewer(boolean has_requested_viewer) {
        this.has_requested_viewer = has_requested_viewer;
    }

    boolean has_requested_viewer;

    @SerializedName("is_business_account")
    public boolean getIs_business_account() {
        return this.is_business_account;
    }

    public void setIs_business_account(boolean is_business_account) {
        this.is_business_account = is_business_account;
    }

    boolean is_business_account;

    @SerializedName("is_joined_recently")
    public boolean getIs_joined_recently() {
        return this.is_joined_recently;
    }

    public void setIs_joined_recently(boolean is_joined_recently) {
        this.is_joined_recently = is_joined_recently;
    }

    boolean is_joined_recently;

    @SerializedName("business_category_name")
    public Object getBusiness_category_name() {
        return this.business_category_name;
    }

    public void setBusiness_category_name(Object business_category_name) {
        this.business_category_name = business_category_name;
    }

    Object business_category_name;

    @SerializedName("overall_category_name")
    public Object getOverall_category_name() {
        return this.overall_category_name;
    }

    public void setOverall_category_name(Object overall_category_name) {
        this.overall_category_name = overall_category_name;
    }

    Object overall_category_name;

    @SerializedName("category_enum")
    public Object getCategory_enum() {
        return this.category_enum;
    }

    public void setCategory_enum(Object category_enum) {
        this.category_enum = category_enum;
    }

    Object category_enum;

    @SerializedName("category_name")
    public String getCategory_name() {
        return this.category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    String category_name;

    @SerializedName("is_private")
    public boolean getIs_private() {
        return this.is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    boolean is_private;

    @SerializedName("edge_mutual_followed_by")
    public EdgeMutualFollowedBy getEdge_mutual_followed_by() {
        return this.edge_mutual_followed_by;
    }

    public void setEdge_mutual_followed_by(EdgeMutualFollowedBy edge_mutual_followed_by) {
        this.edge_mutual_followed_by = edge_mutual_followed_by;
    }

    EdgeMutualFollowedBy edge_mutual_followed_by;

    @SerializedName("profile_pic_url_hd")
    public String getProfile_pic_url_hd() {
        return this.profile_pic_url_hd;
    }

    public void setProfile_pic_url_hd(String profile_pic_url_hd) {
        this.profile_pic_url_hd = profile_pic_url_hd;
    }

    String profile_pic_url_hd;

    @SerializedName("requested_by_viewer")
    public boolean getRequested_by_viewer() {
        return this.requested_by_viewer;
    }

    public void setRequested_by_viewer(boolean requested_by_viewer) {
        this.requested_by_viewer = requested_by_viewer;
    }

    boolean requested_by_viewer;

    @SerializedName("should_show_category")
    public boolean getShould_show_category() {
        return this.should_show_category;
    }

    public void setShould_show_category(boolean should_show_category) {
        this.should_show_category = should_show_category;
    }

    boolean should_show_category;

    @SerializedName("connected_fb_page")
    public Object getConnected_fb_page() {
        return this.connected_fb_page;
    }

    public void setConnected_fb_page(Object connected_fb_page) {
        this.connected_fb_page = connected_fb_page;
    }

    Object connected_fb_page;

    @SerializedName("edge_felix_video_timeline")
    public EdgeFelixVideoTimeline getEdge_felix_video_timeline() {
        return this.edge_felix_video_timeline;
    }

    public void setEdge_felix_video_timeline(EdgeFelixVideoTimeline edge_felix_video_timeline) {
        this.edge_felix_video_timeline = edge_felix_video_timeline;
    }

    EdgeFelixVideoTimeline edge_felix_video_timeline;

    @SerializedName("edge_owner_to_timeline_media")
    public EdgeOwnerToTimelineMedia getEdge_owner_to_timeline_media() {
        return this.edge_owner_to_timeline_media;
    }

    public void setEdge_owner_to_timeline_media(EdgeOwnerToTimelineMedia edge_owner_to_timeline_media) {
        this.edge_owner_to_timeline_media = edge_owner_to_timeline_media;
    }

    EdgeOwnerToTimelineMedia edge_owner_to_timeline_media;

    @SerializedName("edge_saved_media")
    public EdgeSavedMedia getEdge_saved_media() {
        return this.edge_saved_media;
    }

    public void setEdge_saved_media(EdgeSavedMedia edge_saved_media) {
        this.edge_saved_media = edge_saved_media;
    }

    EdgeSavedMedia edge_saved_media;

    @SerializedName("edge_media_collections")
    public EdgeMediaCollections getEdge_media_collections() {
        return this.edge_media_collections;
    }

    public void setEdge_media_collections(EdgeMediaCollections edge_media_collections) {
        this.edge_media_collections = edge_media_collections;
    }

    EdgeMediaCollections edge_media_collections;
}
