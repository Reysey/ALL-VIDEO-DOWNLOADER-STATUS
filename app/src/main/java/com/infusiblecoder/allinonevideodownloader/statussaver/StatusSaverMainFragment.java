package com.infusiblecoder.allinonevideodownloader.statussaver;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.WhatsappStoryAdapter;
import com.infusiblecoder.allinonevideodownloader.models.WAStoryModel;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class StatusSaverMainFragment extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private static final int OPEN_DOCOMENT_TREE_REQUEST_CODE = 1012;


    ArrayList<Object> filesList = new ArrayList<>();
    private WhatsappStoryAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private File[] files;
    private SwipeRefreshLayout recyclerLayout;
    private TextView grantpermissionand11;
    private String namedataprefs;
    private FragmentActivity myselectedActivity = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_saver_main, container, false);
        setActivityAfterAttached();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerLayout = view.findViewById(R.id.swipeRecyclerViewlayout);
        grantpermissionand11 = view.findViewById(R.id.grantpermissionand11);


        SharedPreferences prefs = myselectedActivity.getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
        namedataprefs = prefs.getString("whatsapp", "");//"No name defined" is the default value.


        grantpermissionand11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grantAndroid11StorageAccessPermission();
            }
        });


        boolean result = checkPermission();
        if (result) {
            if (namedataprefs.equals("")) {
                grantpermissionand11.setVisibility(View.VISIBLE);
            } else {
                if (getDataFromWhatsAppFolder() != null) {

                    grantpermissionand11.setVisibility(View.GONE);
                    setUpRecyclerView();
                } else {
                    grantpermissionand11.setVisibility(View.VISIBLE);

                }

            }
        }
        recyclerLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {


                    if (result) {
                        if (!namedataprefs.equals("") && getDataFromWhatsAppFolder() != null) {
                            grantpermissionand11.setVisibility(View.VISIBLE);

                            recyclerLayout.setRefreshing(true);
                            setUpRecyclerView();


                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        recyclerLayout.setRefreshing(false);
                                        iUtils.ShowToast(myselectedActivity, getString(R.string.refre));

                                    }catch (Exception e){
                                    e.printStackTrace();}
                                    //Toast.makeText(myselectedActivity, R.string.refre, Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);


                        }
                    }


                } catch (Exception e) {
                    iUtils.ShowToastError(myselectedActivity, "Error in swipe refresh " + e.getMessage());

                    //Toast.makeText(myselectedActivity, "Error in swipe refresh " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @SuppressLint("WrongConstant")
    public void grantAndroid11StorageAccessPermission() {
        System.out.println("mydhfhsdkfsd 00");

        if (iUtils.isMyPackedgeInstalled(myselectedActivity, "com.whatsapp")) {


            Intent intent;
            String whatsappfolderdir;
            StorageManager storageManager = (StorageManager) myselectedActivity.getSystemService(STORAGE_SERVICE);

            if (new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses").isDirectory()) {
                whatsappfolderdir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
            } else {
                whatsappfolderdir = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses";
            }

            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String scheme = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI"))
                        .toString()
                        .replace("/root/", "/document/");
                String stringBuilder = scheme +"%3A" +whatsappfolderdir;
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(stringBuilder));

                System.out.println("mydhfhsdkfsd " + stringBuilder);
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(whatsappfolderdir));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            startActivityForResult(intent, OPEN_DOCOMENT_TREE_REQUEST_CODE);
            return;
        } else {
            iUtils.ShowToastError(myselectedActivity, myselectedActivity.getString(R.string.appnotinstalled));
        }
    }

    private DocumentFile[] getDataFromWhatsAppFolder() {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(requireContext().getApplicationContext(), Uri.parse(namedataprefs));
        if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory() && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
            return fromTreeUri.listFiles();
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.pernecessory);
                    alertBuilder.setMessage(R.string.write_neesory);
                    alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(myselectedActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(myselectedActivity);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(R.string.pernecessory);
            alertBuilder.setMessage(R.string.write_neesory);
            alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(myselectedActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpRecyclerView();
            } else {
                checkAgain();
            }
        }
    }

    private void setUpRecyclerView() {
        try {

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(myselectedActivity, 2));
            recyclerViewAdapter = new WhatsappStoryAdapter(myselectedActivity, getData());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(myselectedActivity, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private ArrayList<Object> getData() {


        if (Build.VERSION.SDK_INT >= 30) {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";
            try {
                DocumentFile[] allFiles = getDataFromWhatsAppFolder();

                for (int i = 0; i < allFiles.length; i++) {
                    Uri file = allFiles[i].getUri();
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(file);
                    f.setPath(allFiles[i].getUri().toString());
                    f.setFilename(allFiles[i].getUri().getLastPathSegment());


                    System.out.println("dhasjhdahsdhas " + allFiles[i].getUri().toString());
                    if (!allFiles[i].getUri().toString().contains(".nomedia") && !allFiles[i].getUri().toString().equals("")) {
                        filesList.add(f);
                    }

                    grantpermissionand11.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            if (filesList != null) {
                filesList = new ArrayList<>();
            }
            WAStoryModel f;
            String targetPath = "";

            SharedPreferences prefs = myselectedActivity.getSharedPreferences("whatsapp_pref", MODE_PRIVATE);
            String name = prefs.getString("whatsapp", "main");//"No name defined" is the default value.


            String mainWP = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";

            String mainWP_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsapp_and11 + "Media/.Statuses";

            String mainWP_B = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsappbusiness + "Media/.Statuses";

            String mainWP_B_11 = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME_Whatsapp_and11_B + "Media/.Statuses";

            String mainFM = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FMWhatsApp/Media/.Statuses";

            String mainGB = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBWhatsApp/Media/.Statuses";


            File targetDirector1 = new File(mainWP);
            File targetDirector2 = new File(mainWP_11);

            File targetDirector3 = new File(mainWP_B);

            File targetDirector4 = new File(mainWP_B_11);

            File targetDirector5 = new File(mainFM);

            File targetDirector6 = new File(mainGB);


            ArrayList<File> aList = new ArrayList<File>(Arrays.asList(targetDirector1.listFiles() != null ? targetDirector1.listFiles() : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector2.listFiles() != null ? targetDirector2.listFiles() : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector3.listFiles() != null ? targetDirector3.listFiles() : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector4.listFiles() != null ? targetDirector4.listFiles() : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector5.listFiles() != null ? targetDirector5.listFiles() : new File[]{new File("")}));
            aList.addAll(Arrays.asList(targetDirector6.listFiles() != null ? targetDirector6.listFiles() : new File[]{new File("")}));


            File[] n = new File[aList.size()];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Arrays.setAll(n, aList::get);
                files = n;

            } else {

                files = targetDirector1.listFiles();
            }


            try {
                Arrays.sort(files, new Comparator() {
                    public int compare(Object o1, Object o2) {

                        if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                            return -1;
                        } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }

                });

                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(files[i].getAbsolutePath());
                    f.setFilename(file.getName());

                    if (!file.getName().equals(".nomedia") && !file.getPath().equals("")) {
                        filesList.add(f);
                    }
                }

                grantpermissionand11.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return filesList;

    }

    void setActivityAfterAttached() {

        try {

            if (getActivity() != null && isAdded()) {
                myselectedActivity = getActivity();
            }
        } catch (Exception e) {
            myselectedActivity = myselectedActivity;
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1239) {
            setUpRecyclerView();

        } else if (requestCode == OPEN_DOCOMENT_TREE_REQUEST_CODE && resultCode == -1) {

            Uri uri = data.getData();


            try {
                requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            namedataprefs = uri.toString();
            SharedPreferences.Editor editor = myselectedActivity.getSharedPreferences("whatsapp_pref", MODE_PRIVATE).edit();
            editor.putString("whatsapp", uri.toString());
            editor.apply();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpRecyclerView();

                }
            }, 2000);

        }

    }
}
