package com.infusiblecoder.allinonevideodownloader.utils;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.infusiblecoder.allinonevideodownloader.R;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    private static boolean isShowingAd = false;
    private final Application myApplication;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;
    private String nn = "nnn";


    public AppOpenManager(Application myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        try {


            // Have unused ad, no need to fetch another.
            if (isAdAvailable()) {
                return;
            }
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    AppOpenManager.this.appOpenAd = appOpenAd;

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.d(LOG_TAG, "error in loading " + loadAdError);

                }
            };

//        loadCallback =
//                new AppOpenAd.AppOpenAdLoadCallback() {
//                    /**
//                     * Called when an app open ad has loaded.
//                     *
//                     * @param ad the loaded app open ad.
//                     */
//                    @Override
//                    public void onAppOpenAdLoaded(AppOpenAd ad) {
//                        AppOpenManager.this.appOpenAd = ad;
//                    }
//
//                    /**
//                     * Called when an app open ad has failed to load.
//                     *
//                     * @param loadAdError the error.
//                     */
//                    @Override
//                    public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d(LOG_TAG, "error in loading " + loadAdError);
//                    }
//
//                };

            AdRequest request = getAdRequest();
            AppOpenAd.load(
                    myApplication, currentActivity.getResources().getString(R.string.AdmobAppopenads), request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.


        if (nn.equals("nnn")) {

            if (Constants.show_Ads) {
                if (!isShowingAd && isAdAvailable()) {
                    Log.d(LOG_TAG, "Will show ad.");

                    FullScreenContentCallback fullScreenContentCallback =
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Set the reference to null so isAdAvailable() returns false.
                                    AppOpenManager.this.appOpenAd = null;
                                    isShowingAd = false;
                                    fetchAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    isShowingAd = true;
                                }
                            };

                    appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                    appOpenAd.show(currentActivity);

                } else {
                    Log.d(LOG_TAG, "Can not show ad.");
                    fetchAd();
                }
            }
        }
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        try {
            SharedPreferences prefs = activity.getSharedPreferences("whatsapp_pref",
                    Context.MODE_PRIVATE);
            nn = prefs.getString("inappads", "nnn");//"No name defined" is the default value.
        } catch (Exception ignored) {

        }

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;

    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }
}