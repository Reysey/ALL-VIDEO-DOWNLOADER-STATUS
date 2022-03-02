package com.infusiblecoder.allinonevideodownloader.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityAllSupportedBinding;
import com.infusiblecoder.allinonevideodownloader.models.RecDisplayAllWebsites_Model;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.infusiblecoder.allinonevideodownloader.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AllSupportedApps extends AppCompatActivity {

    ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList;
    ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList_otherwebsites;
    private ActivityAllSupportedBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllSupportedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.tt.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.listandstatus));
        recDisplayAllWebsitesModelArrayList = new ArrayList<>();
        recDisplayAllWebsitesModelArrayList_otherwebsites = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(loadWebsiteJSONFromAsset());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectdata = jsonArray.getJSONObject(i);


                System.out.println("dsakdjasdjasd " + jsonObjectdata.getString("website_name"));


                if (i < 8) {

                    if (!Constants.showyoutube && jsonObjectdata.getString("website_name").contains("Youtube")) {
                        continue;
                    }

//                    if (!Constants.showyoutube) {
//                        continue;
////                          break;
//
//                    }
                    recDisplayAllWebsitesModelArrayList
                            .add(new RecDisplayAllWebsites_Model(jsonObjectdata.getString("website_pic_url"), jsonObjectdata.getString("website_name"), jsonObjectdata.getString("website_status"), jsonObjectdata.getString("show_status")));


                } else {

                    recDisplayAllWebsitesModelArrayList_otherwebsites
                            .add(new RecDisplayAllWebsites_Model(jsonObjectdata.getString("website_pic_url"), jsonObjectdata.getString("website_name"), jsonObjectdata.getString("website_status"), jsonObjectdata.getString("show_status")));
                }

                //}


            }


        } catch (Exception e) {
            System.out.println("dsakdjasdjasd " + e.getMessage());
        }


        RecDisplayAllWebsitesAdapter recDisplayAllWebsitesAdapter = new RecDisplayAllWebsitesAdapter(this, recDisplayAllWebsitesModelArrayList);

        binding.recviewSocialnetwork.setAdapter(recDisplayAllWebsitesAdapter);
        binding.recviewSocialnetwork.setLayoutManager(new GridLayoutManager(this, 4));


        RecDisplayAllWebsitesAdapter recDisplayAllWebsitesAdapter_otherwesites = new RecDisplayAllWebsitesAdapter(this, recDisplayAllWebsitesModelArrayList_otherwebsites);

        binding.recviewOthernetwork.setAdapter(recDisplayAllWebsitesAdapter_otherwesites);
        binding.recviewOthernetwork.setLayoutManager(new GridLayoutManager(this, 4));


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadWebsiteJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("supported_websites.json");
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


    class RecDisplayAllWebsitesAdapter extends RecyclerView.Adapter<RecDisplayAllWebsitesAdapter.RecDisplayAllWebsitesViewHolder> {

        Context context;
        ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList;

        public RecDisplayAllWebsitesAdapter(Context context, ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList) {
            this.context = context;
            this.recDisplayAllWebsitesModelArrayList = recDisplayAllWebsitesModelArrayList;
        }

        @NonNull
        @Override
        public RecDisplayAllWebsitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecDisplayAllWebsitesViewHolder(LayoutInflater.from(context).inflate(R.layout.recdisplayallwebsites_item, null, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecDisplayAllWebsitesViewHolder holder, int position) {

            if (recDisplayAllWebsitesModelArrayList.get(position).getWebsitesshowtatue().equals("true")) {
                Glide.with(context)
                        .load(recDisplayAllWebsitesModelArrayList
                                .get(position)
                                .getWebsiteurl())
                        .into(holder.imgRecDisplayAllWebsites);

                holder.txtviewRecDisplayAllWebsites.setText(recDisplayAllWebsitesModelArrayList.get(position).getWebsitename());

                if (recDisplayAllWebsitesModelArrayList.get(position).getWebsitestatue().equals("false")) {
                    holder.statusimg.setVisibility(View.VISIBLE);
                } else {
                    holder.statusimg.setVisibility(View.INVISIBLE);

                }
            }
        }

        @Override
        public int getItemCount() {
            return recDisplayAllWebsitesModelArrayList.size();
        }

        class RecDisplayAllWebsitesViewHolder extends RecyclerView.ViewHolder {

            private final ImageView imgRecDisplayAllWebsites;
            private final ImageView statusimg;
            private final TextView txtviewRecDisplayAllWebsites;

            public RecDisplayAllWebsitesViewHolder(View view) {
                super(view);
                imgRecDisplayAllWebsites = view.findViewById(R.id.img_RecDisplayAllWebsites);
                txtviewRecDisplayAllWebsites = view.findViewById(R.id.txtview_RecDisplayAllWebsites);
                statusimg = view.findViewById(R.id.statusimg);

            }


        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }
}