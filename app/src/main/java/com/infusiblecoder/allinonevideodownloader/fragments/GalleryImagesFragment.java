package com.infusiblecoder.allinonevideodownloader.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.infusiblecoder.allinonevideodownloader.utils.Constants.directoryInstaShoryDirectorydownload_images;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.InstagramImageFileListAdapter;
import com.infusiblecoder.allinonevideodownloader.interfaces.OnClickFileDeleteListner;
import com.infusiblecoder.allinonevideodownloader.utils.FilePathUtility;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryImagesFragment extends Fragment {
    AsyncTask<Void, Void, Void> fetchRecordingsAsyncTask;
    private InstagramImageFileListAdapter instagramImageFileListAdapter;
    private ArrayList<File> fileArrayList;
    private TextView noresultfound;
    private SwipeRefreshLayout swiperefresh;
    private RecyclerView recview_insta_image;
    private Fragment InstagalleryImagesGalleryfrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instagallery_images, container, false);


        noresultfound = (TextView) view.findViewById(R.id.noresultfound);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        recview_insta_image = (RecyclerView) view.findViewById(R.id.recview_insta_image);
        fileArrayList = new ArrayList<>();
        initViews();

//        fetchRecordingsAsyncTask = new FetchRecordingsAsyncTask(getActivity());
//        fetchRecordingsAsyncTask.execute();
        getAllFiles();
        return view;
    }


    private final ActivityResultLauncher<IntentSenderRequest> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(requireActivity(), requireActivity().getString(R.string.file_del), Toast.LENGTH_SHORT).show();
                }
            });


    private void initViews() {


        swiperefresh.setOnRefreshListener(() -> {
            getAllFiles();
            swiperefresh.setRefreshing(false);
        });
    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();
        String location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/" + directoryInstaShoryDirectorydownload_images;

        File[] files = new File(location).listFiles();

        if (files != null && files.length > 1) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        if (files != null) {
            fileArrayList.addAll(Arrays.asList(files));

            setAdaptertoRecyclerView();
        } else {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noresultfound.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    void setAdaptertoRecyclerView() {

        instagramImageFileListAdapter = new InstagramImageFileListAdapter(requireActivity(), fileArrayList, new OnClickFileDeleteListner() {
            @Override
            public void delFile(String path){
                if (path == null) {
                    Toast.makeText(requireActivity(), requireActivity().getString(R.string.error_occ), Toast.LENGTH_SHORT).show();
                } else {
                    FilePathUtility.deletefileAndroid10andABOVE(requireActivity(),path,false);

                }
            }
        });
        recview_insta_image.setAdapter(instagramImageFileListAdapter);

    }


    private class FetchRecordingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public FetchRecordingsAsyncTask(Context activity) {
            dialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.loadingdata));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            getAllFiles();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setAdaptertoRecyclerView();
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (fetchRecordingsAsyncTask != null) {
                    fetchRecordingsAsyncTask.cancel(true);
                }
            }
        }


    }


}