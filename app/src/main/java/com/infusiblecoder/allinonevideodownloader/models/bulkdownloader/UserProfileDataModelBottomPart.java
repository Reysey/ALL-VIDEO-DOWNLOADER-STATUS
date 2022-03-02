package com.infusiblecoder.allinonevideodownloader.models.bulkdownloader;

import androidx.annotation.Keep;

@Keep

public class UserProfileDataModelBottomPart {

    String status;

    public DataInfoInsta getData() {
        return this.data;
    }

    public void setData(DataInfoInsta data) {
        this.data = data;
    }

    DataInfoInsta data;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}










