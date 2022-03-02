package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbadapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.infusiblecoder.allinonevideodownloader.BuildConfig;
import com.infusiblecoder.allinonevideodownloader.activities.FullImageActivity;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.activities.VideoPlayActivity;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels.FBStory;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils.savemanager;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


public class FBstoryAdapter extends RecyclerView.Adapter<FBstoryAdapter.MyViewHolder> {
    Activity activity;
    public boolean allowDelete = false;
    public boolean allowDownload = false;
    public boolean fileTypeCheck = false;
    public List<FBStory> list;
    Context context;

    public void continueVideoSave() {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View parent;
        public View bottomButtons;
        public TextView count;
        ImageView download;
        ImageView option;

        ImageView play;
        ImageView thumb;

        public MyViewHolder(View view) {
            super(view);
            this.parent = view;

            count = ((TextView) view.findViewById(R.id.count));
            download = ((ImageView) view.findViewById(R.id.download));
            option = ((ImageView) view.findViewById(R.id.option));

            play = ((ImageView) view.findViewById(R.id.play));
            thumb = ((ImageView) view.findViewById(R.id.image));
        }
    }

    public FBstoryAdapter(Context context, List<FBStory> list2) {
        this.list = list2;
        this.context = context;
    }

    public FBstoryAdapter(Context context, List<FBStory> list2, Activity activity2, boolean z, boolean z2) {
        this.list = list2;
        this.activity = activity2;
        this.allowDownload = z;
        this.allowDelete = z2;
        this.context = context;
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_fbstory, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final FBStory FBStory = this.list.get(i);

        ((RequestBuilder) Glide.with(context).load(FBStory.source).transform((Transformation<Bitmap>[]) new Transformation[]{new CenterCrop(), new RoundedCorners(15)})).into(myViewHolder.thumb);
        if (FBStory.isVideo) {
            myViewHolder.play.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.play.setVisibility(View.GONE);
        }
        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FBstoryAdapter.this.openFile(FBStory);
            }
        });
        myViewHolder.option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FBstoryAdapter.this.showMenu(FBStory, view);
            }
        });
        myViewHolder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                FBstoryAdapter.this.showMenu(FBStory, view);
                return true;
            }
        });
        if (FBStory.isVideo) {
            if (savemanager.isVideoDownloaded(FBStory.id)) {
                myViewHolder.download.setVisibility(View.GONE);
            } else {
                myViewHolder.download.setVisibility(View.VISIBLE);
            }
        } else if (savemanager.isImageDownloaded(FBStory.id)) {
            myViewHolder.download.setVisibility(View.GONE);
        } else {
            myViewHolder.download.setVisibility(View.VISIBLE);
        }
        myViewHolder.download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FBstoryAdapter.this.startDownloadProcess(FBStory);
            }
        });
    }

    public void share(String str) {
        Uri uriForFile = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(str));
        if (str.toLowerCase().contains(".jpg")) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Intent createChooser = Intent.createChooser(intent, "Share Image using");
                createChooser.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(createChooser);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
            }
        } else if (str.toLowerCase().contains(".mp4")) {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("video/*");
            intent2.putExtra("android.intent.extra.STREAM", uriForFile);
            intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Intent createChooser2 = Intent.createChooser(intent2, "Share Video using");
                createChooser2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(createChooser2);
            } catch (ActivityNotFoundException unused2) {
                Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startDownloadProcess(final FBStory FBStory) {
        if (FBStory.isVideo) {

            String titl = "fbstory__video_" + System.currentTimeMillis();

            DownloadFileMain.startDownloading(
                    context,
                    FBStory.source,
                    titl,
                    ".mp4");
            return;
        }
        String titl = "fbstory__image_" + System.currentTimeMillis();

        System.out.println("myalldatais = " + FBStory.source);
        DownloadFileMain.startDownloading(
                context,
                FBStory.source,
                titl,
                ".png");
    }

    public void updateItem(FBStory FBStory) {
        notifyItemChanged(this.list.indexOf(FBStory));
    }

    public void showMenu(final FBStory FBStory, View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        if (FBStory.isVideo) {
            if (!savemanager.isVideoDownloaded(FBStory.id)) {
                menuInflater.inflate(R.menu.file_menu, popupMenu.getMenu());
            } else if (this.allowDelete) {
                menuInflater.inflate(R.menu.file_menu3, popupMenu.getMenu());
            } else {
                menuInflater.inflate(R.menu.file_menu2, popupMenu.getMenu());
            }
        } else if (!savemanager.isImageDownloaded(FBStory.id)) {
            menuInflater.inflate(R.menu.file_menu, popupMenu.getMenu());
        } else if (this.allowDelete) {
            menuInflater.inflate(R.menu.file_menu3, popupMenu.getMenu());
        } else {
            menuInflater.inflate(R.menu.file_menu2, popupMenu.getMenu());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        FBstoryAdapter.this.delete(FBStory);
                        return true;
                    case R.id.download:
                        FBstoryAdapter.this.startDownloadProcess(FBStory);
                        return true;
                    case R.id.share:
                        FBstoryAdapter.this.share(FBStory.getDownloadPath());
                        return true;
                    case R.id.view:
                        FBstoryAdapter.this.openFile(FBStory);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public void openFile(FBStory FBStory) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        if (!FBStory.isVideo) {
            intent = new Intent(context, FullImageActivity.class);

            System.out.println("hsdfhjdsfsdfsdf " + FBStory.source);

            intent.putExtra("myimgfile", FBStory.source);
            intent.putExtra("isfbimage", "true");
        } else if (savemanager.isVideoDownloaded(FBStory.id)) {
            intent.putExtra("videourl", FBStory.source);
            intent.putExtra("name", FBStory.source);
        } else {
            intent.putExtra("videourl", FBStory.source);
            intent.putExtra("name", FBStory.source);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public void delete(FBStory FBStory) {
        savemanager.delleteFile(FBStory.getDownloadPath());
        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
        int indexOf = this.list.indexOf(FBStory);
        this.list.remove(FBStory);
        notifyItemRemoved(indexOf);
    }

    public int getItemCount() {
        List<FBStory> list2 = this.list;
        if (list2 == null) {
            return 0;
        }
        return list2.size();
    }
}
