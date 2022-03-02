package com.infusiblecoder.allinonevideodownloader.inappbilling;

import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.PurchasesUpdatedListener;

public class BillingClientSetup {

    private static BillingClient instance;

    public static BillingClient getInstance(Context context, PurchasesUpdatedListener listner) {
        return instance == null ? setupBillingClient(context, listner) : instance;
    }

    private static BillingClient setupBillingClient(Context context, PurchasesUpdatedListener listner) {
        BillingClient billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(listner)
                .build();


        return billingClient;
    }

}
