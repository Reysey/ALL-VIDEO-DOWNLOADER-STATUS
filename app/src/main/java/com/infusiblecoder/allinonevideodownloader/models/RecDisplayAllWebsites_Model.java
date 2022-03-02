package com.infusiblecoder.allinonevideodownloader.models;

public class RecDisplayAllWebsites_Model {


    String websiteurl;
    String websitename;
    String websitestatue;
    String websitesshowtatue;


    public RecDisplayAllWebsites_Model() {
    }

    public RecDisplayAllWebsites_Model(String websiteurl, String websitename, String websitestatue, String websitesshowtatue) {
        this.websiteurl = websiteurl;
        this.websitename = websitename;
        this.websitestatue = websitestatue;
        this.websitesshowtatue = websitesshowtatue;
    }


    public String getWebsitesshowtatue() {
        return websitesshowtatue;
    }

    public void setWebsitesshowtatue(String websitesshowtatue) {
        this.websitesshowtatue = websitesshowtatue;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getWebsitename() {
        return websitename;
    }

    public void setWebsitename(String websitename) {
        this.websitename = websitename;
    }

    public String getWebsitestatue() {
        return websitestatue;
    }

    public void setWebsitestatue(String websitestatue) {
        this.websitestatue = websitestatue;
    }
}
