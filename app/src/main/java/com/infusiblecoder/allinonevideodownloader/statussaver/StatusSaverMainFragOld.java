package com.infusiblecoder.allinonevideodownloader.statussaver;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.WhatsappStoryAdapter;
import com.infusiblecoder.allinonevideodownloader.models.WAStoryModel;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class StatusSaverMainFragOld extends Fragment {


    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;


    ArrayList<Object> filesList = new ArrayList<>();
    private WhatsappStoryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_saver_main, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerLayout = view.findViewById(R.id.swipeRecyclerViewlayout);
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    recyclerLayout.setRefreshing(true);
                    setUpRecyclerView();
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerLayout.setRefreshing(false);
                            Toast.makeText(requireActivity(), R.string.refre, Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                } catch (Exception e) {
                    Toast.makeText(requireActivity(), "Error in swipe refresh " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        boolean result = checkPermission();
        if (result) {
            setUpRecyclerView();

        }


        return view;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.pernecessory);
                    alertBuilder.setMessage(R.string.write_neesory);
                    alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireActivity());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(R.string.pernecessory);
            alertBuilder.setMessage(R.string.write_neesory);
            alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpRecyclerView();
                } else {
                    checkAgain();
                }
                break;
        }
    }

    private void setUpRecyclerView() {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        recyclerViewAdapter = new WhatsappStoryAdapter(requireActivity(), getData());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<Object> getData() {

        try {
            File[] fileslisttemp = getWhatsupFolder().listFiles();
            File[] fileslisttemp2 = getWhatsupBusinessFolder().listFiles();
            files = ArrayUtils.addAll(fileslisttemp, fileslisttemp2);


            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                WAStoryModel f = new WAStoryModel();
                f.setName(getString(R.string.stor_saver));
                f.setUri(Uri.fromFile(file));
                f.setPath(files[i].getAbsolutePath());
                f.setFilename(file.getName());

                if (!file.getName().equals(".nomedia") && !file.getPath().equals("")) {
                    filesList.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return filesList;

    }


    public File getWhatsupFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    public File getWhatsupBusinessFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1239) {
            setUpRecyclerView();

        }

    }
}
