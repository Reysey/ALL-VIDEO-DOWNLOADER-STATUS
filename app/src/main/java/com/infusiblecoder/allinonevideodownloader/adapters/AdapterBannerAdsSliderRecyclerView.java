package com.infusiblecoder.allinonevideodownloader.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.infusiblecoder.allinonevideodownloader.R;
import com.smarteist.autoimageslider.SliderViewAdapter;


public class AdapterBannerAdsSliderRecyclerView extends SliderViewAdapter<AdapterBannerAdsSliderRecyclerView.SliderAdapterVH> {

    private Context context;

    public AdapterBannerAdsSliderRecyclerView(Context context) {
        this.context = context;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        AdView adview;
        adview = new AdView(context);
        adview.setAdSize( AdSize.BANNER);

        adview.setAdUnitId(context.getString(R.string.AdmobBanner));

        float density = context.getResources().getDisplayMetrics().density;
        int height = Math.round(AdSize.BANNER.getHeight() * density);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
        adview.setLayoutParams(params);

        AdRequest request = new AdRequest.Builder().build();
        adview.loadAd(request);
        return new SliderAdapterVH(adview);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


    }

    @Override
    public int getCount() {
        return 3;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        public AdView adView;

        public SliderAdapterVH(View v) {
            super(v);
            if (!(itemView instanceof AdView)) {
                adView = (AdView) v.findViewById(R.id.adView);
            }
        }
    }

}