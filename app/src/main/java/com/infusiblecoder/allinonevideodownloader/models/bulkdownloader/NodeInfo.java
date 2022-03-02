package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep

public class NodeInfo implements Serializable {

    @SerializedName("username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    @SerializedName("text")
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String text;

    @SerializedName("__typename")
    public String get__typename() {
        return this.__typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
    }

    String __typename;

    @SerializedName("id")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    @SerializedName("shortcode")
    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    String shortcode;

    @SerializedName("dimensions")
    public DimensionsInfo getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(DimensionsInfo dimensions) {
        this.dimensions = dimensions;
    }

    DimensionsInfo dimensions;

    @SerializedName("display_url")
    public String getDisplay_url() {
        return this.display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    String display_url;

    @SerializedName("edge_media_to_tagged_user")
    public EdgeMediaToTaggedUser getEdge_media_to_tagged_user() {
        return this.edge_media_to_tagged_user;
    }

    public void setEdge_media_to_tagged_user(EdgeMediaToTaggedUser edge_media_to_tagged_user) {
        this.edge_media_to_tagged_user = edge_media_to_tagged_user;
    }

    EdgeMediaToTaggedUser edge_media_to_tagged_user;

    @SerializedName("fact_check_overall_rating")
    public Object getFact_check_overall_rating() {
        return this.fact_check_overall_rating;
    }

    public void setFact_check_overall_rating(Object fact_check_overall_rating) {
        this.fact_check_overall_rating = fact_check_overall_rating;
    }

    Object fact_check_overall_rating;

    @SerializedName("fact_check_information")
    public Object getFact_check_information() {
        return this.fact_check_information;
    }

    public void setFact_check_information(Object fact_check_information) {
        this.fact_check_information = fact_check_information;
    }

    Object fact_check_information;

    @SerializedName("gating_info")
    public Object getGating_info() {
        return this.gating_info;
    }

    public void setGating_info(Object gating_info) {
        this.gating_info = gating_info;
    }

    Object gating_info;

    @SerializedName("sharing_friction_info")
    public SharingFrictionInfo getSharing_friction_info() {
        return this.sharing_friction_info;
    }

    public void setSharing_friction_info(SharingFrictionInfo sharing_friction_info) {
        this.sharing_friction_info = sharing_friction_info;
    }

    SharingFrictionInfo sharing_friction_info;

    @SerializedName("media_overlay_info")
    public Object getMedia_overlay_info() {
        return this.media_overlay_info;
    }

    public void setMedia_overlay_info(Object media_overlay_info) {
        this.media_overlay_info = media_overlay_info;
    }

    Object media_overlay_info;

    @SerializedName("media_preview")
    public String getMedia_preview() {
        return this.media_preview;
    }

    public void setMedia_preview(String media_preview) {
        this.media_preview = media_preview;
    }

    String media_preview;

    @SerializedName("owner")
    public OwnerInfo getOwner() {
        return this.owner;
    }

    public void setOwner(OwnerInfo owner) {
        this.owner = owner;
    }

    OwnerInfo owner;


    @SerializedName("is_video")
    public boolean getIs_video() {
        return this.is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    boolean is_video;

    @SerializedName("accessibility_caption")
    public Object getAccessibility_caption() {
        return this.accessibility_caption;
    }

    public void setAccessibility_caption(Object accessibility_caption) {
        this.accessibility_caption = accessibility_caption;
    }

    Object accessibility_caption;

    @SerializedName("dash_info")
    public DashInfo getDash_info() {
        return this.dash_info;
    }

    public void setDash_info(DashInfo dash_info) {
        this.dash_info = dash_info;
    }

    DashInfo dash_info;

    @SerializedName("has_audio")
    public boolean getHas_audio() {
        return this.has_audio;
    }

    public void setHas_audio(boolean has_audio) {
        this.has_audio = has_audio;
    }

    boolean has_audio;

    @SerializedName("tracking_token")
    public String getTracking_token() {
        return this.tracking_token;
    }

    public void setTracking_token(String tracking_token) {
        this.tracking_token = tracking_token;
    }

    String tracking_token;

    @SerializedName("video_url")
    public String getVideo_url() {
        return this.video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    String video_url;

    @SerializedName("video_view_count")
    public int getVideo_view_count() {
        return this.video_view_count;
    }

    public void setVideo_view_count(int video_view_count) {
        this.video_view_count = video_view_count;
    }

    int video_view_count;

    @SerializedName("edge_media_to_caption")
    public EdgeMediaToCaption getEdge_media_to_caption() {
        return this.edge_media_to_caption;
    }

    public void setEdge_media_to_caption(EdgeMediaToCaption edge_media_to_caption) {
        this.edge_media_to_caption = edge_media_to_caption;
    }

    EdgeMediaToCaption edge_media_to_caption;

    @SerializedName("edge_media_to_comment")
    public EdgeMediaToComment getEdge_media_to_comment() {
        return this.edge_media_to_comment;
    }

    public void setEdge_media_to_comment(EdgeMediaToComment edge_media_to_comment) {
        this.edge_media_to_comment = edge_media_to_comment;
    }

    EdgeMediaToComment edge_media_to_comment;

    @SerializedName("comments_disabled")
    public boolean getComments_disabled() {
        return this.comments_disabled;
    }

    public void setComments_disabled(boolean comments_disabled) {
        this.comments_disabled = comments_disabled;
    }

    boolean comments_disabled;

    @SerializedName("taken_at_timestamp")
    public int getTaken_at_timestamp() {
        return this.taken_at_timestamp;
    }

    public void setTaken_at_timestamp(int taken_at_timestamp) {
        this.taken_at_timestamp = taken_at_timestamp;
    }

    int taken_at_timestamp;

    @SerializedName("edge_liked_by")
    public EdgeLikedBy getEdge_liked_by() {
        return this.edge_liked_by;
    }

    public void setEdge_liked_by(EdgeLikedBy edge_liked_by) {
        this.edge_liked_by = edge_liked_by;
    }

    EdgeLikedBy edge_liked_by;

    @SerializedName("edge_media_preview_like")
    public EdgeMediaPreviewLike getEdge_media_preview_like() {
        return this.edge_media_preview_like;
    }

    public void setEdge_media_preview_like(EdgeMediaPreviewLike edge_media_preview_like) {
        this.edge_media_preview_like = edge_media_preview_like;
    }

    EdgeMediaPreviewLike edge_media_preview_like;

    @SerializedName("location")
    public Object getLocation() {
        return this.location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    Object location;

    @SerializedName("thumbnail_src")
    public String getThumbnail_src() {
        return this.thumbnail_src;
    }

    public void setThumbnail_src(String thumbnail_src) {
        this.thumbnail_src = thumbnail_src;
    }

    String thumbnail_src;

    @SerializedName("thumbnail_resources")
    public List<ThumbnailResource> getThumbnail_resources() {
        return this.thumbnail_resources;
    }

    public void setThumbnail_resources(List<ThumbnailResource> thumbnail_resources) {
        this.thumbnail_resources = thumbnail_resources;
    }

    List<ThumbnailResource> thumbnail_resources;

    @SerializedName("felix_profile_grid_crop")
    public Object getFelix_profile_grid_crop() {
        return this.felix_profile_grid_crop;
    }

    public void setFelix_profile_grid_crop(Object felix_profile_grid_crop) {
        this.felix_profile_grid_crop = felix_profile_grid_crop;
    }

    Object felix_profile_grid_crop;

    @SerializedName("encoding_status")
    public Object getEncoding_status() {
        return this.encoding_status;
    }

    public void setEncoding_status(Object encoding_status) {
        this.encoding_status = encoding_status;
    }

    Object encoding_status;

    @SerializedName("is_published")
    public boolean getIs_published() {
        return this.is_published;
    }

    public void setIs_published(boolean is_published) {
        this.is_published = is_published;
    }

    boolean is_published;

    @SerializedName("product_type")
    public String getProduct_type() {
        return this.product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    String product_type;

    @SerializedName("title")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    @SerializedName("video_duration")
    public double getVideo_duration() {
        return this.video_duration;
    }

    public void setVideo_duration(double video_duration) {
        this.video_duration = video_duration;
    }

    double video_duration;

    @SerializedName("user")
    public UserInfoInsta getUser() {
        return this.user;
    }

    public void setUser(UserInfoInsta user) {
        this.user = user;
    }

    UserInfoInsta user;

    @SerializedName("x")
    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    double x;

    @SerializedName("y")
    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    double y;

    @SerializedName("edge_sidecar_to_children")
    public EdgeSidecarToChildren getEdge_sidecar_to_children() {
        return this.edge_sidecar_to_children;
    }

    public void setEdge_sidecar_to_children(EdgeSidecarToChildren edge_sidecar_to_children) {
        this.edge_sidecar_to_children = edge_sidecar_to_children;
    }

    EdgeSidecarToChildren edge_sidecar_to_children;

    @SerializedName("clips_music_attribution_info")
    public Object getClips_music_attribution_info() {
        return this.clips_music_attribution_info;
    }

    public void setClips_music_attribution_info(Object clips_music_attribution_info) {
        this.clips_music_attribution_info = clips_music_attribution_info;
    }

    Object clips_music_attribution_info;
}
