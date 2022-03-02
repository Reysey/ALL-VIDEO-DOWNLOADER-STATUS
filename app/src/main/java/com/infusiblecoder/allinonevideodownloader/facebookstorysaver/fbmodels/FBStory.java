package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels;

import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FBStory {

    public String id;
    public boolean isVideo = false;
    public String source;
    public String thumb;
    public long time;

    public FBStory(String str, String str2, String str3, long j, boolean z) {
        this.id = str;
        this.time = j;
        this.thumb = str2;
        this.source = str3;
        this.isVideo = z;
    }

    public static FBStory parse(JSONObject jSONObject) {
        String str;
        String str2;
        String str3;
        try {
            long j = jSONObject.getJSONObject("node").getLong("creation_time");
            JSONArray jSONArray = jSONObject.getJSONObject("node").getJSONArray("attachments");
            if (jSONArray.length() == 0) {
                return null;
            }
            boolean z = false;
            String string = jSONArray.getJSONObject(0).getJSONObject("media").getString("__typename");
            JSONObject jSONObject2 = jSONArray.getJSONObject(0).getJSONObject("media");
            if (string != "Photo") {
                if (!string.equals("Photo")) {
                    if (string != "Video") {
                        if (!string.equals("Video")) {
                            str2 = "";
                            str = str2;
                            z = true;
                            return new FBStory(j + "", str2, str, j, z);
                        }
                    }
                    String string2 = jSONObject2.getJSONObject("previewImage").getString("uri");
                    if (jSONObject2.has("playable_url_quality_hd")) {
                        str3 = jSONObject2.getString("playable_url_quality_hd");
                    } else {
                        str3 = "";
                    }
                    if (str3 != null) {
                        if (str3.length() >= 10) {
                            str = str3;
                            str2 = string2;
                            z = true;
                            return new FBStory(j + "", str2, str, j, z);
                        }
                    }
                    str = jSONArray.getJSONObject(0).getJSONObject("media").getString("playable_url");
                    str2 = string2;
                    z = true;
                    return new FBStory(j + "", str2, str, j, z);
                }
            }
            str2 = jSONObject2.getJSONObject("previewImage").getString("uri");
            str = jSONObject2.getJSONObject("image").getString("uri");
            return new FBStory(j + "", str2, str, j, z);
        } catch (Exception e) {
            Log.e("tag1", "error while parsing story " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String parseSource(String str) {
        String str2 = "";
        try {
            JSONObject jSONObject = new JSONObject(str.replace("for (;;);", "")).getJSONObject("payload");
            if (jSONObject.has("error")) {
                return null;
            }
            if (jSONObject.has("hd_src")) {
                str2 = jSONObject.getString("hd_src");
                Log.e("tag1", "hd src " + str2);
            } else {
                str2 = null;
            }
            if ((str2 == null || str2.length() < 10) && jSONObject.has("sd_src")) {
                str2 = jSONObject.getString("sd_src");
                Log.e("tag1", "sd src " + str2);
            }
            if ((str2 == null || str2.length() < 10) && jSONObject.has("sd_src_no_ratelimit")) {
                str2 = jSONObject.getString("sd_src_no_ratelimit");
                Log.e("tag1", "sd src no limit  " + str2);
            }
            if (str2 != null) {
                return str2;
            }
            Log.e("tag1", "cant find video source");
            return null;
        } catch (Exception e) {
            Log.e("tag1", "error while parsing video story rource : " + e.getMessage());
            e.printStackTrace();
        }

        return str2;
    }

    public static List<FBStory> parseBulk(String str) {
        try {
            return parseBulk(new JSONObject(str).getJSONObject("data").getJSONObject("bucket").getJSONObject("unified_stories").getJSONArray("edges"));
        } catch (Exception e) {
            Log.e("tag1", "error while parsBulk story string : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public String getDownloadPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/facebooKStories/");
        sb.append(this.id);
        sb.append(this.isVideo ? ".mp4" : ".jpg");
        return sb.toString();
    }

    public static List<FBStory> parseBulk(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            try {
                FBStory parse = parse(jSONArray.getJSONObject(i));
                if (parse != null) {
                    arrayList.add(0, parse);
                }
                i++;
            } catch (Exception e) {
                Log.e("tag1", "error while parsing bullk story " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return arrayList;
    }
}
