package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbmodels.FBStory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class savemanager {


    public static void saveImage(Context context, final String str, final String str2, Bitmap bitmap) {
        File file = new File(Environment.DIRECTORY_DOWNLOADS + "/facebookStories");
        if (!file.exists()) {
            file.mkdir();
        }
        if (bitmap != null) {
            saveBitmap(bitmap, str2);
            return;
        }
        Log.e("tag1", "downloading now");
        Glide.with((Context) context).asBitmap().load(str).addListener(new RequestListener<Bitmap>() {
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                Log.e("tag1", "downloading faild " + glideException.getMessage());

                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Log.e("tag1", "downloading finished");
                savemanager.saveBitmap(bitmap, str2);
                Log.e("tag1", "or is not null good");
                return false;

            }

        }).submit();

        Toast.makeText(context, "story downloaded", Toast.LENGTH_SHORT).show();
    }

    static void saveBitmap(Bitmap bitmap, String str) {
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(str));
            Log.e("tag1", " image saved " + str);
        } catch (IOException e) {
            Log.e("tag1", "error saving image " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isImageDownloaded(String str) {
        String str2 = str + ".jpg";
        Log.e("tag1", "file name is " + str2);
        File file = new File(Environment.DIRECTORY_DOWNLOADS + "/facebookStories");

        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file.toString() + "/" + str2).exists();
    }

    public static boolean isVideoDownloaded(String str) {
        String str2 = str + ".mp4";
        Log.e("tag1", "file name is " + str2);
        File file = new File(Environment.DIRECTORY_DOWNLOADS + "/facebookStories");

        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file.toString() + "/" + str2).exists();
    }


    public static void scanVideo(Context context, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("_data", str);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static ArrayList<FBStory> loadSavedStories() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/facebooKStories");
        if (!file.exists()) {
            file.mkdir();
        }
        String[] list = file.list();
        ArrayList<FBStory> arrayList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String str = file.getPath() + "/" + list[i];
            arrayList.add(new FBStory(list[i].substring(0, list[i].indexOf(".")), str, str, new File(str).lastModified(), list[i].contains(".mp4")));
            Log.e("tag2", "found file " + arrayList.get(0).source);
        }
        return arrayList;
    }

    public static void delleteFile(String str) {
        new File(str).delete();
    }
}
