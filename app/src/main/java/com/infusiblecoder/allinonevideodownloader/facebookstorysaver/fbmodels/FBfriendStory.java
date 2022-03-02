package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels;

import android.util.Log;

import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FBfriendStory {
    public int count = 0;

    public String id;
    public String name;
    public List<FBStory> stories;
    public String thumb;
    public String uid;
    public boolean userSeenSomeStories = false;

    public FBfriendStory(String str, String str2, String str3, List<FBStory> list) {
        this.id = str;
        this.name = str2;
        this.thumb = str3;
        this.stories = list;
    }

    public static FBfriendStory parse(JSONObject jSONObject) {
        try {
            String string = jSONObject.getJSONObject("node").getString("id");
            String string2 = jSONObject.getJSONObject("node").getJSONObject("story_bucket_owner").getString("id");
            FBfriendStory friendstory = new FBfriendStory(string, jSONObject.getJSONObject("node").getJSONObject("story_bucket_owner").getString(AppMeasurementSdk.ConditionalUserProperty.NAME), jSONObject.getJSONObject("node").getJSONObject("owner").getJSONObject("profile_picture").getString("uri"), new ArrayList());
            friendstory.uid = string2;
            friendstory.count = jSONObject.getJSONObject("node").getJSONObject("unified_stories").getJSONArray("edges").length();
            JSONArray jSONArray = jSONObject.getJSONObject("node").getJSONObject("unified_stories").getJSONArray("edges");
            int i = 0;
            while (true) {
                if (i >= jSONArray.length()) {
                    break;
                }
                try {
                    if (jSONArray.getJSONObject(i).getJSONObject("node").getJSONObject("story_card_seen_state").getBoolean("is_seen_by_viewer")) {
                        friendstory.userSeenSomeStories = true;
                        break;
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return friendstory;
        } catch (Exception e2) {
            Log.e("tag1", "error while parsing FBfriendStory " + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
    }

    public static List<FBfriendStory> parseBulk(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            try {
                arrayList.add(parse(jSONArray.getJSONObject(i)));
                i++;
            } catch (Exception e) {
                Log.e("tag1", "error while parsing bullk FBfriendStory " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return arrayList;
    }
}
