package com.infusiblecoder.allinonevideodownloader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.fragments.DownloadMainFragment;
import com.infusiblecoder.allinonevideodownloader.statussaver.StatusSaverMainFragment;

public class StatusSaverActivity extends AppCompatActivity {
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_saver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String frags = getIntent().getStringExtra("frag");
        if (frags.equals("status")) {
            fragment = new StatusSaverMainFragment();

        } else {
            fragment = new DownloadMainFragment();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

    }


    public String getMyData() {
        return MainActivity.myString;
    }


    public void setmydata(String mysa) {
        MainActivity.myString = mysa;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}