package com.infusiblecoder.allinonevideodownloader.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.activities.VideoPlayActivity;
import com.infusiblecoder.allinonevideodownloader.models.Model_Video;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.ViewHolder> {

    private ArrayList<Model_Video> al_video;

    private Context context;
    private ActionMode mActiveActionMode;
    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActiveActionMode = mode;

            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.delete, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.del_tit) + " " + selectedItems.size() + context.getString(R.string.vidd))
                    .setMessage(context.getResources().getString(R.string.delete_confirm))
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.del_tit), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Collections.sort(selectedItems, Collections.reverseOrder());
                            for (Integer intItem : selectedItems) {
                                deleteItem(Integer.valueOf(intItem.toString()));
                            }
                            mode.finish();
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel_option), null).show();

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public VideoFolderAdapter(Context context, ArrayList<Model_Video> al_video, Activity activity) {
        this.al_video = al_video;
        this.context = context;

        this.notifyDataSetChanged();
        Log.e("updated", "yesupdate" + al_video.toString());

    }

    @NotNull
    @Override
    public VideoFolderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.update(al_video.get(i), i);
    }

    private void deleteItem(int position) {


        String video = al_video.get(position).getStr_path();

        context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA + "=?", new String[]{video});

        al_video.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, al_video.size());
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return al_video.size();
    }

    @SuppressLint("DefaultLocale")
    private String secToTime(int sec) {

        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(sec),
                TimeUnit.MILLISECONDS.toSeconds(sec) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sec))
        );

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image;
        public TextView tvDuration;
        public View vCheckBackColor;
        public CheckBox chkVideoSelected;
        FrameLayout rl_select;

        public ViewHolder(View v) {

            super(v);

            iv_image = v.findViewById(R.id.media_img_bck);
            rl_select = v.findViewById(R.id.frameLayout);
            tvDuration = v.findViewById(R.id.tvDuration);
            vCheckBackColor = v.findViewById(R.id.vCheckBackColor);
            chkVideoSelected = v.findViewById(R.id.chkVideoSelected);

        }

        void selectItem(int item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(Integer.valueOf(item));
                    rl_select.setBackgroundColor(Color.WHITE);
                    chkVideoSelected.setVisibility(View.GONE);
                    vCheckBackColor.setVisibility(View.GONE);

                    Log.e("selctedItems", selectedItems.toString() + "---" + item);


                    if (selectedItems.isEmpty()) {
                        multiSelect = false;
                        mActiveActionMode.finish();
                    }
                } else {

                    selectedItems.add(item);
                    rl_select.setBackgroundColor(Color.LTGRAY);
                    chkVideoSelected.setVisibility(View.VISIBLE);
                    vCheckBackColor.setVisibility(View.VISIBLE);

                    Log.e("UnselctedItems", selectedItems.toString() + "---" + item);

                }
                mActiveActionMode.setTitle(selectedItems.size() + context.getString(R.string.selected_i));
            }
        }

        void update(final Model_Video video, final int id) {

            Glide.with(context).load(video.getStr_thumb()).skipMemoryCache(false).into(iv_image);
            tvDuration.setText(secToTime(video.getDuration()));
            rl_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  iUtils.ShowToast(context,"clicked :*");

                    if (multiSelect) {

                        selectItem(id);

                    } else {


                        final String[] options = {context.getString(R.string.watch_arr), context.getString(R.string.del_arr), context.getString(R.string.share_arr)};

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.choose);
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (options[which].contains(context.getString(R.string.watch_arr))) {

                                    try {


//                                        String Path = video.getStr_path();
//                                        Intent mVideoWatch = new Intent(Intent.ACTION_VIEW);
//                                        mVideoWatch.setDataAndType(Uri.parse(Path), "video/mp4");
//                                        context.startActivity(mVideoWatch);

//                                        context.startActivity(new Intent(context, PlayActivity.class)
//                                                .putExtra("videourl", video.getStr_path())
//                                                .putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, video.getId()));


                                        context.startActivity(new Intent(context, VideoPlayActivity.class)
                                                .putExtra("videourl", video.getStr_path()));

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
                                                    //  Log.e("Deleted",intItem.toString());
                                                    // al_video.remove(intItem);
                                                    deleteItem(id);


                                                }
                                            })
                                            .setNegativeButton(context.getString(R.string.cancel_option), null).show();

                                } else {
                                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                    File fileWithinMyDir = new File(video.getStr_path());

                                    if (fileWithinMyDir.exists()) {

                                        try {
                                            intentShareFile.setType("video/mp4");
                                            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(video.getStr_path()));

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


                    }
                }
            });

            rl_select.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(id);
                    return true;
                }
            });
            //textView.setText(value + "");
            if (selectedItems.contains(id)) {
                chkVideoSelected.setVisibility(View.VISIBLE);
                vCheckBackColor.setVisibility(View.VISIBLE);
                rl_select.setBackgroundColor(Color.LTGRAY);
            } else {
                rl_select.setBackgroundColor(Color.WHITE);
                chkVideoSelected.setVisibility(View.GONE);
                vCheckBackColor.setVisibility(View.GONE);
            }

        }
    }
}