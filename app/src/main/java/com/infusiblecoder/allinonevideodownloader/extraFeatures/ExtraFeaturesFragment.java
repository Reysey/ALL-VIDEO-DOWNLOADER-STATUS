package com.infusiblecoder.allinonevideodownloader.extraFeatures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.activities.InstagramBulkDownloader;
import com.infusiblecoder.allinonevideodownloader.extraFeatures.videolivewallpaper.MainActivityLivewallpaper;
import com.infusiblecoder.allinonevideodownloader.facebookstorysaver.FacebookPrivateWebview;
import com.infusiblecoder.allinonevideodownloader.utils.AdsManager;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;


public class ExtraFeaturesFragment extends Fragment {
    private String nn = "nnn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_extra_features, container, false);


        view.findViewById(R.id.btn_one)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), MainActivityLivewallpaper.class));
                         showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        //showAdmobAds();

                    }
                });
        view.findViewById(R.id.bankcardtiktokId)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), TikTokWebview.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

                    }
                });

        view.findViewById(R.id.facebookprivate)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), FacebookPrivateWebview.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        //showAdmobAds();

                    }
                });


        if (Constants.show_earning_card_in_extrafragment) {
            view.findViewById(R.id.card_extra).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.card_extra).setVisibility(View.GONE);

        }

        view.findViewById(R.id.earnmoneycard)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), EarningAppWebviewActivity.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                            Intent i = new Intent(getActivity(), FloatingWidgetDownload.class);
//
//
//                            getActivity().startService(i);
//                        } else {
//                            Toast.makeText(getActivity(),
//                                    "avaliable for android LOLLIPOP or above", Toast.LENGTH_SHORT).show();
//
//                        }
                    }
                });

        view.findViewById(R.id.instabulkcard)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), InstagramBulkDownloader.class));
                        showstarappasds();
                        //TODO to show admobb ads here instead of startapp ads
                        // showAdmobAds();

                    }
                });


        SharedPreferences prefs = getActivity().getSharedPreferences("whatsapp_pref",
                Context.MODE_PRIVATE);
        nn = prefs.getString("inappads", "nnn");//"No name defined" is the default value.


        return view;
    }

    private void showstarappasds() {
        if (Constants.show_startappads) {
            if (nn.equals("nnn")) {
                IronSource.setInterstitialListener(new InterstitialListener() {
                    /**
                     * Invoked when Interstitial Ad is ready to be shown after load function was called.
                     */
                    @Override
                    public void onInterstitialAdReady() {
                        IronSource.loadInterstitial();


                    }

                    /**
                     * invoked when there is no Interstitial Ad available after calling load function.
                     */
                    @Override
                    public void onInterstitialAdLoadFailed(IronSourceError error) {
                    }

                    /**
                     * Invoked when the Interstitial Ad Unit is opened
                     */
                    @Override
                    public void onInterstitialAdOpened() {
                    }

                    /*
                     * Invoked when the ad is closed and the user is about to return to the application.
                     */
                    @Override
                    public void onInterstitialAdClosed() {
                    }

                    /**
                     * Invoked when Interstitial ad failed to show.
                     *
                     * @param error - An object which represents the reason of showInterstitial failure.
                     */
                    @Override
                    public void onInterstitialAdShowFailed(IronSourceError error) {
                    }

                    /*
                     * Invoked when the end user clicked on the interstitial ad, for supported networks only.
                     */
                    @Override
                    public void onInterstitialAdClicked() {
                    }

                    /**
                     * Invoked right before the Interstitial screen is about to open.
                     * NOTE - This event is available only for some of the networks.
                     * You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent
                     */
                    @Override
                    public void onInterstitialAdShowSucceeded() {
                    }
                });
            }
        }
    }

    private void showAdmobAds() {
        if (Constants.show_Ads) {
            if (nn.equals("nnn")) {
                AdsManager.loadInterstitialAd(getActivity());

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(getActivity());
    }
    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(getActivity());
    }
}
