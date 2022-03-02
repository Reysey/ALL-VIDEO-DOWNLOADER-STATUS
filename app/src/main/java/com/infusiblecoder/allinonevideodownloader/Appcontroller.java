package com.infusiblecoder.allinonevideodownloader;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.infusiblecoder.allinonevideodownloader.utils.AppOpenManager;
import com.infusiblecoder.allinonevideodownloader.utils.LocaleHelper;
import com.onesignal.OneSignal;

import java.util.Locale;

public class Appcontroller extends MultiDexApplication {

    AppOpenManager appOpenManager;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MobileAds.initialize(
                    this,
                    new OnInitializationCompleteListener() {
                        @Override
                        public void onInitializationComplete(InitializationStatus initializationStatus) {
                        }
                    });


            appOpenManager = new AppOpenManager(this);

            //firebase
            FirebaseMessaging.getInstance().subscribeToTopic("all");
            // OneSignal Initialization
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

            OneSignal.initWithContext(this);
            OneSignal.setAppId(getString(R.string.onsignalappid));

            AudienceNetworkAds.initialize(this);

            SharedPreferences prefs = getSharedPreferences("lang_pref", MODE_PRIVATE);
            // System.out.println("qqqqqqqqqqqqqqqqq = "+Locale.getDefault().getLanguage());

            String lang = prefs.getString("lang", Locale.getDefault().getLanguage());//"No name defined" is the default value.


            LocaleHelper.setLocale(getApplicationContext(), lang);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
