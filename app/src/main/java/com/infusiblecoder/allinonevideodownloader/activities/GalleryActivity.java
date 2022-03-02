package com.infusiblecoder.allinonevideodownloader.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.fragments.GalleryAudiosFragment;
import com.infusiblecoder.allinonevideodownloader.fragments.GalleryVideosFragment;
import com.infusiblecoder.allinonevideodownloader.fragments.GalleryImagesFragment;
import com.infusiblecoder.allinonevideodownloader.fragments.GalleryStatusSaver;
import com.infusiblecoder.allinonevideodownloader.utils.AdsManager;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.ironsource.mediationsdk.IronSource;

import org.jetbrains.annotations.NotNull;

public class GalleryActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private String nn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabsgallery);
        viewPager = (ViewPager) findViewById(R.id.viewpagergallery);


        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.gallery_fragment_statussaver)).setIcon(R.drawable.ic_gallery_color_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.StatusSaver_gallery)).setIcon(R.drawable.statuspic));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.instaimage)).setIcon(R.drawable.ic_image));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.audios)).setIcon(R.drawable.ic_music_note_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        SharedPreferences prefs = getSharedPreferences("whatsapp_pref",
                Context.MODE_PRIVATE);
        nn = prefs.getString("inappads", "nnn");//"No name defined" is the default value.


        if (Constants.show_Ads) {
            if (nn.equals("nnn")) {
             //   AdsManager.loadInterstitialAd(this);

                AdsManager.loadBannerAd(this, findViewById(R.id.banner_container));
            } else {

                findViewById(R.id.banner_container).setVisibility(View.GONE);
            }
        }

    }


    public class MyAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            myContext = context;
            this.totalTabs = totalTabs;
        }


        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GalleryVideosFragment();
                case 1:
                    return new GalleryStatusSaver();
                case 2:
                    return new GalleryImagesFragment();
                case 3:
                    return new GalleryAudiosFragment();
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return totalTabs;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
}
