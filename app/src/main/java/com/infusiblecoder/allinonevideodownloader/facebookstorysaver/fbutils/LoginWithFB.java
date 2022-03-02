package com.infusiblecoder.allinonevideodownloader.facebookstorysaver.fbutils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import java.util.Random;

public class LoginWithFB extends AppCompatActivity {
    public WebView mWebview;
    public ProgressBar progressBar;
    String TAG = "LoginWithFB";
    private WebCrome WebCromeClass = new WebCrome(this);

    private void initView() {
        this.mWebview = (WebView) findViewById(R.id.webView);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void startWebview() {
        this.mWebview.getSettings().setJavaScriptEnabled(true);
        this.mWebview.getSettings().setDomStorageEnabled(true);
        this.mWebview.getSettings().setBuiltInZoomControls(true);
        this.mWebview.getSettings().setDisplayZoomControls(true);
        this.mWebview.getSettings().setUseWideViewPort(true);
        this.mWebview.getSettings().setLoadWithOverviewMode(true);
        this.mWebview.addJavascriptInterface(this, "FB");
        this.mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.mWebview, true);
            this.mWebview.getSettings().setMixedContentMode(2);
        }
        if (Build.VERSION.SDK_INT < 18) {
            this.mWebview.getSettings().setSavePassword(true);
        }
        this.mWebview.setWebViewClient(new WebViewClass(this));
        this.mWebview.setWebChromeClient(this.WebCromeClass);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        this.mWebview.loadUrl("https://www.facebook.com/");
        try {
            Random random = new Random();
            int a = random.nextInt(iUtils.UserAgentsList.length);

            mWebview.getSettings().setUserAgentString(iUtils.UserAgentsList[a] + "");


        } catch (Exception e) {
            System.out.println("dsakdjasdjasd " + e.getMessage());

            mWebview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");

        }

    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login_with_fb);
        initView();
        startWebview();
    }

    public void onDestroy() {
        ((ViewGroup) this.mWebview.getParent()).removeView(this.mWebview);
        this.mWebview.destroy();
        super.onDestroy();
    }

    @JavascriptInterface
    public void keyFound(String str) {
        if (str.length() < 15) {
            Log.e("tag2", "key found but too small " + str);
            return;
        }
        Log.e("tag2", "key found " + str);
        if (FBhelper.valadateCooki(CookieManager.getInstance().getCookie("https://www.facebook.com"))) {
            Log.e("tag2", "cookie is not valid");
            Facebookprefloader sharedPrefsFor = new Facebookprefloader(LoginWithFB.this);

            sharedPrefsFor.SavePref(str, "true");
            Intent intent = new Intent();
            intent.putExtra("resultfb", "result");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class WebCrome extends WebChromeClient {
        LoginWithFB authWithFacebookActivity;

        private WebCrome(LoginWithFB loginWithFB) {
            this.authWithFacebookActivity = null;
            this.authWithFacebookActivity = loginWithFB;
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i < 100) {
                this.authWithFacebookActivity.progressBar.setVisibility(View.VISIBLE);
                this.authWithFacebookActivity.mWebview.setVisibility(View.GONE);
                return;
            }
            this.authWithFacebookActivity.progressBar.setVisibility(View.GONE);
            this.authWithFacebookActivity.mWebview.setVisibility(View.VISIBLE);
        }
    }

    private class WebViewClass extends WebViewClient {
        LoginWithFB authWithFacebookActivity;

        private WebViewClass(LoginWithFB loginWithFB) {
            this.authWithFacebookActivity = loginWithFB;
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            Log.e("tag3", "pageFinished");
            webView.loadUrl("javascript:FB.keyFound();");
            webView.loadUrl("javascript:var el = document.querySelectorAll('input[name=fb_dtsg]');FB.keyFound(el[0].value);");
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            Log.e("tag3", "webview error " + i + " / " + str + " / " + str2);
        }

        private boolean loginActivity(WebView webView, String str) {
            if (str.contains("play.google.com/store/apps/details?id=com.instagram.android")) {
                Log.e(LoginWithFB.this.TAG, "first_if");
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(Uri.parse("market://details?id=com.instagram.android"));
                LoginWithFB.this.startActivity(intent);
                Log.e(LoginWithFB.this.TAG, "play.google.com/store/apps/details?id=com.instagram.android");
                return true;
            } else if (str.contains("https://m.facebook.com/v2.2/dialog/oauth?channel")) {
                webView.loadUrl("https://www.instagram.com/accounts/login/");
                return false;
            } else {
                webView.loadUrl(str);
                String cookie = CookieManager.getInstance().getCookie(str);
                if (!FBhelper.valadateCooki(cookie)) {
                    return true;
                }
                String str2 = LoginWithFB.this.TAG;
                Log.e(str2, "login done cookie:" + cookie);
                Facebookprefloader sharedPrefsFor = new Facebookprefloader(LoginWithFB.this);
                sharedPrefsFor.SavePref(cookie, "true");
                return true;
            }
        }

        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            Log.e(LoginWithFB.this.TAG, "onReceivedError");
        }

        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            if (Build.VERSION.SDK_INT >= 21) {
                return loginActivity(webView, webResourceRequest.getUrl().toString());
            }
            return false;
        }

        @Deprecated
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return loginActivity(webView, str);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            String uri = webResourceRequest.getUrl().toString();
            WebResourceResponse shouldInterceptRequest = super.shouldInterceptRequest(webView, webResourceRequest);
            if (uri.contains("https://www.facebook.com/api/graphqlbatch")) {
                Log.e("tag1", "found graph url " + uri + "   " + webResourceRequest.getRequestHeaders().get("cookie"));
            }
            return shouldInterceptRequest;
        }
    }
}
