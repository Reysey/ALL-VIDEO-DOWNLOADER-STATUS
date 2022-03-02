package com.infusiblecoder.allinonevideodownloader.extraFeatures;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityEarningAppWebviewBinding;

public class EarningAppWebviewActivity extends AppCompatActivity {
    public static Handler handler;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = "Mytagis";
    boolean doubleBackToExitPressedOnce = false;
    private ActivityEarningAppWebviewBinding binding;


    @SuppressLint("HandlerLeak")
    private class btnInitHandlerListner extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    private class webChromeClients extends WebChromeClient {
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }


    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "progressbar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "progressbar GONE");
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEarningAppWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        InitHandler();
        if (Build.VERSION.SDK_INT >= 24) {
            onstart();
            binding.webViewscan.clearFormData();
            binding.webViewscan.getSettings().setSaveFormData(true);
            binding.webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            binding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            binding.webViewscan.setWebChromeClient(new webChromeClients());
            binding.webViewscan.setWebViewClient(new MyBrowser());
            binding.webViewscan.getSettings().setAppCacheMaxSize(5242880);
            binding.webViewscan.getSettings().setAllowFileAccess(true);
            binding.webViewscan.getSettings().setAppCacheEnabled(true);
            binding.webViewscan.getSettings().setJavaScriptEnabled(true);
            binding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
            binding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            binding.webViewscan.getSettings().setDatabaseEnabled(true);
            binding.webViewscan.getSettings().setBuiltInZoomControls(false);
            binding.webViewscan.getSettings().setSupportZoom(true);
            binding.webViewscan.getSettings().setUseWideViewPort(true);
            binding.webViewscan.getSettings().setDomStorageEnabled(true);
            binding.webViewscan.getSettings().setAllowFileAccess(true);
            binding.webViewscan.getSettings().setLoadWithOverviewMode(true);
            binding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
            binding.webViewscan.getSettings().setBlockNetworkImage(false);
            binding.webViewscan.getSettings().setBlockNetworkLoads(false);
            binding.webViewscan.getSettings().setLoadWithOverviewMode(true);


            binding.webViewscan.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && binding.progressBar.getVisibility() == ProgressBar.GONE) {
                        binding.progressBar.setVisibility(ProgressBar.VISIBLE);

                    }

                    binding.progressBar.setProgress(progress);
                    if (progress == 100) {
                        binding.progressBar.setVisibility(ProgressBar.GONE);

                    }
                }
            });


            binding.webViewscan.loadUrl("http://play.qureka.com/");
        } else {
            onstart();
            binding.webViewscan.clearFormData();
            binding.webViewscan.getSettings().setSaveFormData(true);
            binding.webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            binding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            binding.webViewscan.setWebChromeClient(new webChromeClients());
            binding.webViewscan.setWebViewClient(new MyBrowser());
            binding.webViewscan.getSettings().setAppCacheMaxSize(5242880);
            binding.webViewscan.getSettings().setAllowFileAccess(true);
            binding.webViewscan.getSettings().setAppCacheEnabled(true);
            binding.webViewscan.getSettings().setJavaScriptEnabled(true);
            binding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
            binding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            binding.webViewscan.getSettings().setDatabaseEnabled(true);
            binding.webViewscan.getSettings().setBuiltInZoomControls(false);
            binding.webViewscan.getSettings().setSupportZoom(true);
            binding.webViewscan.getSettings().setUseWideViewPort(true);
            binding.webViewscan.getSettings().setDomStorageEnabled(true);
            binding.webViewscan.getSettings().setAllowFileAccess(true);
            binding.webViewscan.getSettings().setLoadWithOverviewMode(true);
            binding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
            binding.webViewscan.getSettings().setBlockNetworkImage(false);
            binding.webViewscan.getSettings().setBlockNetworkLoads(false);
            binding.webViewscan.getSettings().setLoadWithOverviewMode(true);


            binding.webViewscan.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    if (progress < 100 && binding.progressBar.getVisibility() == ProgressBar.GONE) {
                        binding.progressBar.setVisibility(ProgressBar.VISIBLE);

                    }

                    binding.progressBar.setProgress(progress);
                    if (progress == 100) {
                        binding.progressBar.setVisibility(ProgressBar.GONE);

                    }
                }
            });

            binding.webViewscan.loadUrl("http://play.qureka.com/");
        }
    }


    public void onstart() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"}, 123);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && Build.VERSION.SDK_INT >= 21) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (binding.webViewscan.canGoBack()) {
                    binding.webViewscan.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
        z = super.onKeyDown(keyCode, event);
        return z;
    }


    @SuppressLint({"WrongConstant"})
    @RequiresApi(api = 21)
    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.pressagain), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        binding.webViewscan.clearCache(true);
    }

    public void onDestroy() {
        super.onDestroy();
        binding.webViewscan.clearCache(true);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        binding.webViewscan.clearCache(true);
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }

}