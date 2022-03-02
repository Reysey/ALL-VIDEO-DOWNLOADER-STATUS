package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class HolderSearch {
    private boolean approved;
    private boolean headOrNot;

    private String id;
    private String idNumber;
    private boolean isPrivate;
    private String name;
    private String profileImage;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String str) {
        this.profileImage = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String str) {
        this.idNumber = str;
    }

    public boolean getIsPrivate() {
        return this.isPrivate;
    }

    public void setIsPrivate(boolean z) {
        this.isPrivate = z;
    }

    public boolean getApproved() {
        return this.approved;
    }

    public void setApproved(boolean z) {
        this.approved = z;
    }

    public boolean getHead() {
        return this.headOrNot;
    }

    public void setHead(boolean z) {
        this.headOrNot = z;
    }
}
