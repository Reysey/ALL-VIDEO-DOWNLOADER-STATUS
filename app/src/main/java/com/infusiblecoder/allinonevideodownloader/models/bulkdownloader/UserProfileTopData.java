package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep

public class UserProfileTopData implements Serializable {
    @SerializedName("logging_page_id")
    public String getLogging_page_id() {
        return this.logging_page_id;
    }

    public void setLogging_page_id(String logging_page_id) {
        this.logging_page_id = logging_page_id;
    }

    String logging_page_id;

    @SerializedName("show_suggested_profiles")
    public boolean getShow_suggested_profiles() {
        return this.show_suggested_profiles;
    }

    public void setShow_suggested_profiles(boolean show_suggested_profiles) {
        this.show_suggested_profiles = show_suggested_profiles;
    }

    boolean show_suggested_profiles;

    @SerializedName("show_follow_dialog")
    public boolean getShow_follow_dialog() {
        return this.show_follow_dialog;
    }

    public void setShow_follow_dialog(boolean show_follow_dialog) {
        this.show_follow_dialog = show_follow_dialog;
    }

    boolean show_follow_dialog;

    @SerializedName("graphql")
    public Graphql getGraphql() {
        return this.graphql;
    }

    public void setGraphql(Graphql graphql) {
        this.graphql = graphql;
    }

    Graphql graphql;

    @SerializedName("toast_content_on_load")
    public Object getToast_content_on_load() {
        return this.toast_content_on_load;
    }

    public void setToast_content_on_load(Object toast_content_on_load) {
        this.toast_content_on_load = toast_content_on_load;
    }

    Object toast_content_on_load;

    @SerializedName("show_view_shop")
    public boolean getShow_view_shop() {
        return this.show_view_shop;
    }

    public void setShow_view_shop(boolean show_view_shop) {
        this.show_view_shop = show_view_shop;
    }

    boolean show_view_shop;

    @SerializedName("profile_pic_edit_sync_props")
    public Object getProfile_pic_edit_sync_props() {
        return this.profile_pic_edit_sync_props;
    }

    public void setProfile_pic_edit_sync_props(Object profile_pic_edit_sync_props) {
        this.profile_pic_edit_sync_props = profile_pic_edit_sync_props;
    }

    Object profile_pic_edit_sync_props;
}

















