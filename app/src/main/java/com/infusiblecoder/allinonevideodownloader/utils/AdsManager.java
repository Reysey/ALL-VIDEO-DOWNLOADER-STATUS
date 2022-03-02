package com.infusiblecoder.allinonevideodownloader.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.adapters.AdapterBannerAdsSliderRecyclerView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;

public class AdsManager {

    //TODO change this if you want to limit interstishal ads
    public static int NUMBER_OF_INTERSTISHAL_ADS_PER_SESSION = 10;
    //TODO dont touch this
    public static int NUMBER_OF_INTERSTISHAL_ADS_SHOWN = 0;


    //TODO change this if you want to limit banner ads
    public static int NUMBER_OF_BANNER_ADS_PER_SESSION = 5;
    //TODO dont touch this
    public static int NUMBER_OF_BANNER_ADS_SHOWN = 0;


    private static com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    public static void loadInterstitialAd(final Activity activity) {

        loadAdmobInterstitialAd(activity);


    }


    @SuppressLint("MissingPermission")
    public static void loadBannerAd(final Activity activity, final ViewGroup adContainer) {
        if (AdsManager.NUMBER_OF_BANNER_ADS_SHOWN < AdsManager.NUMBER_OF_BANNER_ADS_PER_SESSION) {
            AdsManager.NUMBER_OF_BANNER_ADS_SHOWN++;

            com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(activity);
            adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            adView.setAdUnitId(activity.getResources().getString(R.string.AdmobBanner));
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    try{
                        adContainer.setVisibility(View.GONE);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    try{
                        adContainer.setVisibility(View.VISIBLE);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });
            adContainer.addView(adView);

        } else {
            adContainer.setVisibility(View.GONE);
        }
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);

            AlertDialog.Builder cacheSIzeAlert = new AlertDialog.Builder(context);
            cacheSIzeAlert.setTitle("Cache Cleared");
            cacheSIzeAlert.setMessage("Cache has been cleared");
            final AlertDialog cacheDialog = cacheSIzeAlert.create();
            cacheSIzeAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            cacheDialog.show();
        } catch (Exception e) {
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }
    public static void loadAdmobInterstitialAd(final Context context) {


        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, context.getString(R.string.AdmobInterstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                if (AdsManager.NUMBER_OF_INTERSTISHAL_ADS_SHOWN < AdsManager.NUMBER_OF_INTERSTISHAL_ADS_PER_SESSION) {
                    mInterstitialAd = interstitialAd;
                    if (mInterstitialAd != null) {
                        AdsManager.NUMBER_OF_INTERSTISHAL_ADS_SHOWN++;
                        mInterstitialAd.show((Activity) context);
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });


    }



    private static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        try {


            // Set the media view.
            adView.setMediaView(adView.findViewById(R.id.ad_media));

            // Set other ad assets.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());

            }

            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);

            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);

            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);

            }

            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);

            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());

            }

            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);

            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());

            }

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);

            } else {
                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());

                adView.getStarRatingView().setVisibility(View.VISIBLE);

            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);

            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd);

            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.
            VideoController vc = nativeAd.getMediaContent().getVideoController();

            // Updates the UI to say whether or not this ad has a video asset.
            if (vc.hasVideoContent()) {
                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        super.onVideoEnd();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public static void loadAdmobNativeAd(Context context, FrameLayout frameLayout) {
        try {


            AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.AdmobNative));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    // Assumes you have a placeholder FrameLayout in your View layout
                    // (with id fl_adplaceholder) where the ad is to be placed.

                    // Assumes that your ad layout is in a file call native_ad_layout.xml
                    // in the res/layout folder
                    NativeAdView adView = (NativeAdView) ((Activity)context).getLayoutInflater()
                            .inflate(R.layout.layout_native_ads, null);
                    // This method sets the text, images and the native ad, etc into the ad
                    // view.
                    populateNativeAdView(nativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });


            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e("adload", "adload faild $error");

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadBannerAdsAdapter(Context context, SliderView sliderView) {
        try {
            AdapterBannerAdsSliderRecyclerView adapterBannerAdsRecyclerView = new AdapterBannerAdsSliderRecyclerView(context);
            sliderView.setSliderAdapter(adapterBannerAdsRecyclerView);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.startAutoCycle();
        } catch (Exception e) {
            e.printStackTrace();
            sliderView.setVisibility(View.GONE);
        }

    }


}