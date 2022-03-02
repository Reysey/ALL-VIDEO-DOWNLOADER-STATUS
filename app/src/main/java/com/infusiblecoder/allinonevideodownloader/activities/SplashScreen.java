package com.infusiblecoder.allinonevideodownloader.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.allinonevideodownloader.BuildConfig;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivitySplashScreenBinding;
import com.infusiblecoder.allinonevideodownloader.utils.LocaleHelper;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;


public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.version.setText(String.format("%s %s", getString(R.string.version), BuildConfig.VERSION_NAME));


        new CountDownTimer(1000, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }.start();

        new Thread() {

            @Override
            public void run() {
//                iUtils.myInstagramTempCookies =  iUtils.showCookies("https://www.instagram.com/");
//
//
//                OkHttpClient client = new OkHttpClient().newBuilder()
//                        .build();
//                Request request = new Request.Builder()
//                        .url("https://www.instagram.com/tv/CRyVpDSAE59/?__a=1")
//                        .method("GET", null)
//                        .addHeader("Cookie",iUtils.myInstagramTempCookies)
//                        .build();
                try {

                    iUtils.myInstagramTempCookies = iUtils.showCookies("https://www.instagram.com/");
                    System.out.println("mysjdjhdjkh " + iUtils.myInstagramTempCookies);

                    iUtils.myTiktokTempCookies = iUtils.showCookiestiktokful("https://tiktokfull.com/");
                    System.out.println("mysjdjhdjkh tiktok " + iUtils.myTiktokTempCookies);


//                    Response response = client.newCall(request).execute();
//
//                    System.out.println("mysjdjhdjkh "+response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }


}



