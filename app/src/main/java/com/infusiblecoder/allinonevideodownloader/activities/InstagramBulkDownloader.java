package com.infusiblecoder.allinonevideodownloader.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.ProfileBulkLayoutAdapter;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityInstagramBulkDownloaderBinding;
import com.infusiblecoder.allinonevideodownloader.models.bulkdownloader.UserUser;
import com.infusiblecoder.allinonevideodownloader.utils.SharedPrefsForInstagram;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Keep

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class InstagramBulkDownloader extends AppCompatActivity {

    private ProfileBulkLayoutAdapter adapter;

    private ActivityInstagramBulkDownloaderBinding binding;
    private ProgressDialog progressDralogGenaratinglink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstagramBulkDownloaderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDralogGenaratinglink = new ProgressDialog(InstagramBulkDownloader.this);


        binding.searchinstaprofile.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                //  https://www.instagram.com/web/search/topsearch/?query=

                System.out.println("hvjksdhfhdkd bb " + query);


                SharedPrefsForInstagram sharedPrefsFor = new SharedPrefsForInstagram(InstagramBulkDownloader.this);
                Map<String, String> map = sharedPrefsFor.getPreference(SharedPrefsForInstagram.PREFERENCE);
                if (map != null) {
                    String cookie = "ds_user_id=" + map.get(SharedPrefsForInstagram.PREFERENCE_USERID) + "; sessionid=" + map.get(SharedPrefsForInstagram.PREFERENCE_SESSIONID);

                    progressDralogGenaratinglink.setMessage(getResources().getString(R.string.loading));
                    progressDralogGenaratinglink.show();

                    AndroidNetworking.get("https://www.instagram.com/web/search/topsearch/?query={query1}")
                            .addPathParameter("query1", query)
                            .setPriority(Priority.LOW)
                            .addHeaders("Cookie", cookie)
                            .addHeaders(
                                    "User-Agent",
                                    "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\""
                            )
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (progressDralogGenaratinglink != null)
                                        progressDralogGenaratinglink.dismiss();
                                    List<UserUser> list = new ArrayList<>();


                                    //   System.out.println("hvjksdhfhdkd " + response.toString().substring(0,150));
                                    //4162923872
                                    //3401888503
                                    try {


                                        JSONArray responseJSONArray = response.getJSONArray("users");
                                        for (int i = 0; i < responseJSONArray.length(); i++) {
                                            Gson gson = new Gson();

                                            UserUser gsonObj = gson.fromJson(responseJSONArray.getJSONObject(i).getJSONObject("user").toString(), UserUser.class);
                                            list.add(gsonObj);
                                            System.out.println("hvjksdhfhdkd " + gsonObj.is_verified);

                                        }


                                        //     System.out.println("hvjksdhfhdkd length " + response.getJSONArray("places").getJSONObject(0).getJSONObject("place").getJSONObject("location").getString("short_name"));

                                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(InstagramBulkDownloader.this));

                                        adapter = new ProfileBulkLayoutAdapter(list, InstagramBulkDownloader.this);

                                        binding.recyclerView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();


                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onError(ANError anError) {
                                    if (progressDralogGenaratinglink != null)
                                        progressDralogGenaratinglink.dismiss();
                                    Toast.makeText(InstagramBulkDownloader.this, getResources().getString(R.string.error_occ), Toast.LENGTH_LONG).show();

                                    System.out.println("hvjksdhfhdkd eeee" + anError.getResponse());

                                }
                            });


//https://www.instagram.com/graphql/query/
//https://i.instagram.com/api/v1/user/629205195/reel_media
                    //  https://i.instagram.com/api/v1/users/$UserId/full_detail_info?max_id=
                    //https://www.instagram.com/{{username}}/?__a=1
                    //https://i.instagram.com/api/v1/users/{userid}/info/

                } else {

                    Toast.makeText(InstagramBulkDownloader.this, R.string.loginfirst, Toast.LENGTH_LONG).show();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        loadDummyData();

    }


    void loadDummyData() {


        List<UserUser> list = new ArrayList<>();


        //   System.out.println("hvjksdhfhdkd " + response.toString().substring(0,150));
        //4162923872
        //3401888503
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            JSONArray responseJSONArray = obj.getJSONArray("users");
            for (int i = 0; i < responseJSONArray.length(); i++) {
                Gson gson = new Gson();

                UserUser gsonObj = gson.fromJson(responseJSONArray.getJSONObject(i).getJSONObject("user").toString(), UserUser.class);
                list.add(gsonObj);
                System.out.println("hvjksdhfhdkd " + gsonObj.is_verified);

            }


            //     System.out.println("hvjksdhfhdkd length " + response.getJSONArray("places").getJSONObject(0).getJSONObject("place").getJSONObject("location").getString("short_name"));

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(InstagramBulkDownloader.this));

            adapter = new ProfileBulkLayoutAdapter(list, InstagramBulkDownloader.this);

            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("dummy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}