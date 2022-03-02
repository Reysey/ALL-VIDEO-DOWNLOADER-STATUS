package com.infusiblecoder.allinonevideodownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.infusiblecoder.allinonevideodownloader.databinding.ActivityLoginInstagramBinding;
import com.infusiblecoder.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class InstagramLoginActivity extends AppCompatActivity {

    private String cookies;
    private ActivityLoginInstagramBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginInstagramBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        LoadPage();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage();
            }
        });

    }

    public void LoadPage() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.clearCache(true);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        binding.webView.setWebViewClient(new MyWebviewClient());
        CookieSyncManager.createInstance(InstagramLoginActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        binding.webView.loadUrl("https://www.instagram.com/accounts/login/");


        try {
            Random random = new Random();
            int a = random.nextInt(iUtils.UserAgentsList.length);

            binding.webView.getSettings().setUserAgentString(iUtils.UserAgentsList[a] + "");


        } catch (Exception e) {
            System.out.println("dsakdjasdjasd " + e.getMessage());

            binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");

        }


        binding.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                binding.swipeRefreshLayout.setRefreshing(progress != 100);
            }
        });
    }


    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null && !cookies.isEmpty()) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
        }
        return CookieValue;
    }

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView1, String url) {
            webView1.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

            cookies = CookieManager.getInstance().getCookie(str);

            try {
                String session_id = getCookie(str, "sessionid");
                String csrftoken = getCookie(str, "csrftoken");
                String userid = getCookie(str, "ds_user_id");
                if (session_id != null && csrftoken != null && userid != null) {

                    Map<String, String> map = new HashMap<>();

                    map.put(SharedPrefsForInstagram.PREFERENCE_COOKIES, cookies);
                    map.put(SharedPrefsForInstagram.PREFERENCE_CSRF, csrftoken);
                    map.put(SharedPrefsForInstagram.PREFERENCE_ISINSTAGRAMLOGEDIN, "true");
                    map.put(SharedPrefsForInstagram.PREFERENCE_SESSIONID, session_id);
                    map.put(SharedPrefsForInstagram.PREFERENCE_USERID, userid);


                    SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);

                    sharedPrefsForInstagram.setPreference(map);


                    webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    setResult(RESULT_OK, intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}