package com.infusiblecoder.allinonevideodownloader.adapters;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infusiblecoder.allinonevideodownloader.activities.FullImageActivity;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.activities.VideoPlayActivity;
import com.infusiblecoder.allinonevideodownloader.interfaces.OnClickFileDeleteListner;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import java.io.File;
import java.util.ArrayList;

public class InstagramImageFileListAdapter extends RecyclerView.Adapter<InstagramImageFileListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> fileArrayList;
    private LayoutInflater layoutInflater;
    private boolean isVideo = true;

    OnClickFileDeleteListner onClickFileDeleteListner;

    public InstagramImageFileListAdapter(Context context, ArrayList<File> fileArrayList, OnClickFileDeleteListner onClickFileDeleteListner) {
        this.context = context;
        this.fileArrayList = fileArrayList;
        this.onClickFileDeleteListner = onClickFileDeleteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.image_item_instagram, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        File fileItem = fileArrayList.get(i);


        try {
            String extension = fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
            if (fileItem.getAbsolutePath().endsWith(".mp4") || fileItem.getAbsolutePath().endsWith(".webm")) {
                viewHolder.playButtonImageadapter.setVisibility(View.VISIBLE);
                isVideo = true;
            } else {
                viewHolder.playButtonImageadapter.setVisibility(View.GONE);
                isVideo = false;

            }
            Glide.with(context)
                    .load(fileItem.getPath())
                    .into(viewHolder.myimage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        viewHolder.mainrelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVideo) {

                    final String[] options = {context.getString(R.string.watch_arr), context.getString(R.string.del_arr), context.getString(R.string.share_arr)};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.choose);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].contains(context.getString(R.string.watch_arr))) {

                                try {

                                    dialog.dismiss();


                                    if (!fileItem.getAbsolutePath().contains(".gif")) {
//                                        context.startActivity(new Intent(context, PlayActivity.class)
//                                                .putExtra("videourl", fileItem.getAbsolutePath())
//                                                .putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, fileItem.getName()));
//

                                        context.startActivity(new Intent(context, VideoPlayActivity.class)
                                                .putExtra("videourl", fileItem.getAbsolutePath()));

                                    } else {
                                        iUtils.ShowToast(context, context.getString(R.string.preview_not));

                                    }

                                } catch (Exception e) {
                                    iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                                    Log.e("Errorisnewis", e.getMessage());
                                }
                            } else if (options[which].contains(context.getString(R.string.del_arr))) {
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.del_arr))
                                        .setMessage(context.getResources().getString(R.string.delete_confirm))
                                        .setCancelable(false)
                                        .setPositiveButton(context.getString(R.string.del_arr), new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                try {
                                                    fileArrayList.get(i).delete();
                                                    System.out.println("mudatauriis = "+fileArrayList.get(i).getAbsolutePath());

                                                    onClickFileDeleteListner.delFile(fileArrayList.get(i).getAbsolutePath());

                                                    fileArrayList.remove(i);
                                                    notifyDataSetChanged();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                        .setNegativeButton(context.getString(R.string.cancel_option), null).show();

                            } else {
                                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                File fileWithinMyDir = new File(fileItem.getAbsolutePath());

                                if (fileWithinMyDir.exists()) {

                                    try {
                                        intentShareFile.setType("video/mp4");
                                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileItem.getAbsolutePath()));

                                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                                context.getString(R.string.SharingVideoSubject));
                                        intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingVideoBody));

                                        context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.SharingVideoTitle)));
                                    } catch (ActivityNotFoundException e) {
                                        iUtils.ShowToast(context, context.getResources().getString(R.string.went_shARRE));

                                    }

                                }
                            }
                        }
                    });
                    builder.show();

                } else {
                    final String[] options = {context.getString(R.string.viewimage), context.getString(R.string.del_arr), context.getString(R.string.share_arr)};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.choose);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].contains(context.getString(R.string.viewimage))) {

                                try {

                                    Intent intent = new Intent(context, FullImageActivity.class);
                                    intent.putExtra("myimgfile", fileArrayList.get(i).getAbsolutePath());
                                    context.startActivity(intent);

                                } catch (Exception e) {
                                    iUtils.ShowToast(context, context.getResources().getString(R.string.somth_video_wrong));
                                    Log.e("Errorisnewis", e.getMessage());
                                }
                            } else if (options[which].contains(context.getString(R.string.del_arr))) {
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.del_arr))
                                        .setMessage(context.getResources().getString(R.string.delete_confirm_image))
                                        .setCancelable(false)
                                        .setPositiveButton(context.getString(R.string.del_arr), new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                try {
                                                    fileArrayList.get(i).delete();
                                                    System.out.println("mudatauriis = "+fileArrayList.get(i).getAbsolutePath());

                                                    onClickFileDeleteListner.delFile(fileArrayList.get(i).getAbsolutePath());

                                                    fileArrayList.remove(i);
                                                    notifyDataSetChanged();
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                        .setNegativeButton(context.getString(R.string.cancel_option), null).show();

                            } else {
                                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                File fileWithinMyDir = new File(fileArrayList.get(i).getAbsolutePath());

                                if (fileWithinMyDir.exists()) {

                                    try {
                                        intentShareFile.setType("image/*");
                                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileArrayList.get(i).getAbsolutePath()));

                                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                                context.getString(R.string.SharingimageSubject));
                                        intentShareFile.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.SharingimageBody));

                                        context.startActivity(Intent.createChooser(intentShareFile, context.getString(R.string.SharingimageTitle)));
                                    } catch (ActivityNotFoundException e) {
                                        iUtils.ShowToast(context, context.getResources().getString(R.string.went_shARRE));

                                    }

                                }
                            }
                        }
                    });
                    builder.show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileArrayList == null ? 0 : fileArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mainrelativelayout;
        private ImageView myimage, playButtonImageadapter;


        public ViewHolder(View view) {
            super(view);
            mainrelativelayout = (RelativeLayout) view.findViewById(R.id.mainrelativelayout);
            myimage = (ImageView) view.findViewById(R.id.myimage);
            playButtonImageadapter = (ImageView) view.findViewById(R.id.playButtonImageadapter);

        }
    }
}