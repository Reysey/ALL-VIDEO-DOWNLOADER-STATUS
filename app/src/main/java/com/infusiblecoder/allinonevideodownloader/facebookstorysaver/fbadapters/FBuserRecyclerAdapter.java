package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbadapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbinterfaces.OnFbUserClicked;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels.FBfriendStory;
import com.infusiblecoder.allinonevideodownloader.models.storymodels.ModelUsrTray;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FBuserRecyclerAdapter extends RecyclerView.Adapter<FBuserRecyclerAdapter.MyViewHolder> implements Filterable {
    public Context context;
    Activity activity;
    public boolean allowDelete = false;
    public boolean allowDownload = false;
    public boolean enableFilter = false;
    public boolean fileTypeCheck = false;
    public List<FBfriendStory> filterList;
    public List<FBfriendStory> list;
    public List<FBfriendStory> originalCopy;
    OnFbUserClicked onFbUserClicked;

    public void clearFilter() {
        this.filterList = null;
        this.enableFilter = false;
        notifyDataSetChanged();
    }

    public void filterNormal() {
        this.list = this.originalCopy;
        notifyDataSetChanged();
    }

    public void filterSeen() {
        this.list = new ArrayList();
        for (int i = 0; i < this.originalCopy.size(); i++) {
            if (this.originalCopy.get(i).userSeenSomeStories) {
                this.list.add(this.originalCopy.get(i));
            }
        }
        for (int i2 = 0; i2 < this.originalCopy.size(); i2++) {
            if (!this.originalCopy.get(i2).userSeenSomeStories) {
                this.list.add(this.originalCopy.get(i2));
            }
        }
        notifyDataSetChanged();
    }

    public void filter(String str) {
        this.filterList = new ArrayList();
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).name.toLowerCase().contains(str.toLowerCase())) {
                this.filterList.add(this.list.get(i));
            }
        }
        this.enableFilter = true;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View bottomButtons;
        public TextView count;
        public View parent;
        ImageView play;
        ImageView thumb;
        public TextView title;
        ImageView userThumb;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;
            count = ((TextView) view.findViewById(R.id.count));
            play = ((ImageView) view.findViewById(R.id.play));
            thumb = ((ImageView) view.findViewById(R.id.image));
            title = ((TextView) view.findViewById(R.id.title));
            userThumb = ((ImageView) view.findViewById(R.id.userThumb));

        }
    }

    public FBuserRecyclerAdapter(Context context, List<FBfriendStory> list2, OnFbUserClicked onFbUserClicked) {
        this.list = list2;
        this.originalCopy = list2;
        this.context = context;
        this.onFbUserClicked = onFbUserClicked;
    }

    public FBuserRecyclerAdapter(List<FBfriendStory> list2, Activity activity2, boolean z, boolean z2) {
        this.list = list2;
        this.activity = activity2;
        this.allowDownload = z;
        this.allowDelete = z2;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_fbstory, viewGroup, false));
    }

    public FBfriendStory getItem(int i) {
        if (this.enableFilter) {
            return this.filterList.get(i);
        }
        return this.list.get(i);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<FBfriendStory>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<FBfriendStory> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalCopy;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<FBfriendStory> getFilteredResults(String constraint) {
        List<FBfriendStory> results = new ArrayList<>();

        for (FBfriendStory item : originalCopy) {
            if (item.name.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final FBfriendStory item = getItem(i);
        myViewHolder.title.setText(item.name);
        TextView textView = myViewHolder.count;
        textView.setText(item.count + "");
        ((RequestBuilder) Glide.with((Context) context).load(item.thumb).transform((Transformation<Bitmap>[]) new Transformation[]{new CenterCrop(), new RoundedCorners(15)})).into(myViewHolder.thumb);
        Glide.with((Context) context).load(item.thumb).into(myViewHolder.userThumb);
        myViewHolder.play.setVisibility(View.GONE);
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                onFbUserClicked.onclick_on_user(item.id);
            }
        });
    }

    public int getItemCount() {
        if (this.enableFilter) {
            return this.filterList.size();
        }
        List<FBfriendStory> list2 = this.list;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }
}
