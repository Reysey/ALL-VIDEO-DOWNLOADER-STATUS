package com.infusiblecoder.allinonevideodownloader.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.AudioAdapter;
import com.infusiblecoder.allinonevideodownloader.models.SongInfo;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryAudiosFragment extends Fragment {


    private final Handler myHandler = new Handler();
    RecyclerView recyclerView;
    SeekBar seekBar;
    AudioAdapter audioAdapter;
    MediaPlayer mediaPlayer;
    private ArrayList<SongInfo> _songs;
    // ProgressDialog mDialog;
    private TextView noresultfound;
    private Runnable runnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audiogallery, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        noresultfound = (TextView) view.findViewById(R.id.noresultfound);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        _songs = new ArrayList<SongInfo>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);



        getAllFiles();

        Thread t = new runThread();
        t.start();


        return view;
    }

    private void getAllFiles() {
        String location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + Constants.directoryInstaShoryDirectorydownload_audio;

        File[] files = new File(location).listFiles();
        if (files != null && files.length > 1) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".m4a") || file.getName().endsWith(".wav")) {
                    Uri vv = Uri.parse(file.getAbsolutePath());
                    SongInfo s = new SongInfo(file.getName(), file.getName(), vv.toString());
                    _songs.add(s);
                }
            }
            setAdaptertoRecyclerView();
//            if (mDialog != null) {
//                mDialog.dismiss();
//            }
        } else {

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noresultfound.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    void setAdaptertoRecyclerView() {

        audioAdapter = new AudioAdapter(requireActivity(), _songs);
        recyclerView.setAdapter(audioAdapter);

        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button b, View view, final SongInfo obj, int position) {
                try {


                    if (b.getText().equals(getString(R.string.stop_btn))) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        b.setText(getString(R.string.play));
                    } else {


                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;

                        }

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setDataSource(obj.getSongUrl());
                                    mediaPlayer.prepareAsync();
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.start();
                                            seekBar.setProgress(0);
                                            seekBar.setMax(mediaPlayer.getDuration());
                                            Log.d("Prog", "run: " + mediaPlayer.getDuration());
                                        }
                                    });
                                    b.setText(getString(R.string.stop_btn));


                                } catch (Exception ignored) {
                                }
                            }

                        };
                        myHandler.postDelayed(runnable, 100);

                    }
                } catch (Exception e) {
                    b.setText(getString(R.string.play));
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            myHandler.removeCallbacks(runnable);

//            if (mDialog != null) {
//                mDialog.dismiss();
//            }


        } catch (Exception ignored) {

        }


    }

    public class runThread extends Thread {


        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Runwa", "run: " + 1);
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                            } catch (Exception ignored) {

                            }
                        }
                    });

                }
            }
        }

    }


}