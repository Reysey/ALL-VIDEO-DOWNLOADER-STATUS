package com.infusiblecoder.allinonevideodownloader.extraFeatures.videolivewallpaper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityMainLivevideoBinding;
import com.infusiblecoder.allinonevideodownloader.utils.AdsManager;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.infusiblecoder.allinonevideodownloader.utils.LocaleHelper;
import com.ironsource.mediationsdk.IronSource;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class MainActivityLivewallpaper extends AppCompatActivity {
    public CinimaWallService cinimaService;

    private String url = null;

    private ActivityMainLivevideoBinding binding;
    private String nn = "nnn";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityMainLivevideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.spinKit.setVisibility(View.GONE);
        cinimaService = new CinimaWallService();
        binding.checkboxSound.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                cinimaService.setEnableVideoAudio(MainActivityLivewallpaper.this, binding.checkboxSound.isChecked());
            }
        });
        binding.checkboxPlayBegin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                cinimaService.setPlayB(MainActivityLivewallpaper.this, binding.checkboxPlayBegin.isChecked());
            }
        });
        binding.checkboxBattery.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                cinimaService.setPlayBatterySaver(MainActivityLivewallpaper.this, binding.checkboxBattery.isChecked());
            }
        });


        SharedPreferences prefs = getSharedPreferences("whatsapp_pref",
                Context.MODE_PRIVATE);
        nn = prefs.getString("inappads", "nnn");//"No name defined" is the default value.


        if (Constants.show_Ads) {
            if (nn.equals("nnn")) {
              //  AdsManager.loadInterstitialAd(this);

                AdsManager.loadBannerAd(this, findViewById(R.id.banner_container));
            } else {

                findViewById(R.id.banner_container).setVisibility(View.GONE);
            }
        }

    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 53213 && i2 == -1) {

            this.url = getPath(intent.getData());

            binding.spinKit.setVisibility(View.VISIBLE);
            binding.videoSelectButton.setVisibility(View.GONE);
            Glide.with(this).asBitmap().addListener(new RequestListener<Bitmap>() {
                public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                    binding.spinKit.setVisibility(View.GONE);
                    binding.videoSelectButton.setVisibility(View.VISIBLE);
                    return false;
                }

                public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                    binding.spinKit.setVisibility(View.GONE);
                    binding.videoSelectButton.setVisibility(View.VISIBLE);
                    return false;
                }
            }).load(Uri.fromFile(new File((String) this.url))).into(binding.imgThumb);
        }

    }

    public String getPath(Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }


    private void showAdmobAds() {
        if (Constants.show_Ads) {
            if (nn.equals("nnn")) {
                AdsManager.loadInterstitialAd(this);

            }
        }
    }


    public void set_up_video_clicked(View view) {
        showAdmobAds();
        if (this.url == null) {
            Toasty.error(this, getString(R.string.please_select_video)).show();
            return;
        }
        cinimaService.setEnableVideoAudio(this, binding.checkboxSound.isChecked());
        cinimaService.setPlayB(this, binding.checkboxPlayBegin.isChecked());
        cinimaService.setPlayBatterySaver(this, binding.checkboxBattery.isChecked());
        cinimaService.setVidSource(this, this.url);
        if (cinimaService.getVideoSource(this) == null) {
            Toasty.info(this, getString(R.string.error_emty_video)).show();
            return;
        }
        try {
            clearWallpaper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(this, CinimaWallService.class));
        startActivity(intent);


    }

    public void video_on_clicked(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 53213);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
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
