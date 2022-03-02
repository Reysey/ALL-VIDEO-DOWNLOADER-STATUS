package com.infusiblecoder.allinonevideodownloader.facebookstorysaver;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.allinonevideodownloader.activities.MainActivity;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityFacebookPrivateWebviewBinding;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;

import java.util.Random;

public class FacebookPrivateWebview extends AppCompatActivity implements View.OnClickListener {


    private ActivityFacebookPrivateWebviewBinding binding;
    private Uri myvideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityFacebookPrivateWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.btnBackBrowse.setOnClickListener(this);
        binding.btnCloseBrowse.setOnClickListener(this);
        binding.btnHomeBrowse.setOnClickListener(this);
        binding.btnNextBrowse.setOnClickListener(this);
        binding.btnPasteUrl.setOnClickListener(this);
        binding.btnlogoutfb.setOnClickListener(this);


        webviewUtils();
    }


    void webviewUtils() {

        binding.webViewFacebook.setVerticalScrollBarEnabled(false);
        binding.webViewFacebook.setHorizontalScrollBarEnabled(false);
        binding.webViewFacebook.getSettings().setJavaScriptEnabled(true);
        binding.webViewFacebook.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webViewFacebook.getSettings().setBuiltInZoomControls(true);
        binding.webViewFacebook.getSettings().setDisplayZoomControls(true);
        binding.webViewFacebook.getSettings().setUseWideViewPort(true);
        binding.webViewFacebook.getSettings().setLoadWithOverviewMode(true);
        binding.webViewFacebook.getSettings().setAppCacheMaxSize(5242880);
        binding.webViewFacebook.getSettings().setAllowFileAccess(true);
        binding.webViewFacebook.getSettings().setAppCacheEnabled(true);
        binding.webViewFacebook.getSettings().setDefaultTextEncodingName("UTF-8");
        binding.webViewFacebook.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webViewFacebook.getSettings().setDatabaseEnabled(true);
        binding.webViewFacebook.getSettings().setBuiltInZoomControls(false);
        binding.webViewFacebook.getSettings().setSupportZoom(true);
        binding.webViewFacebook.getSettings().setUseWideViewPort(true);
        binding.webViewFacebook.getSettings().setDomStorageEnabled(true);
        binding.webViewFacebook.getSettings().setLoadsImagesAutomatically(true);
        binding.webViewFacebook.getSettings().setBlockNetworkImage(false);
        binding.webViewFacebook.getSettings().setBlockNetworkLoads(false);
        binding.webViewFacebook.getSettings().setLoadWithOverviewMode(true);

        Random rand = new Random();
        int rand_int1 = rand.nextInt(2);

        if (rand_int1 == 0) {
            binding.webViewFacebook.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.0.2; SAMSUNG SM-G925F Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/44.0.2403.133 Mobile Safari/537.36");
        } else {
            binding.webViewFacebook.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30");
        }
        // binding.webViewFacebook.getSettings().setUserAgentString(binding.webViewFacebook.getSettings().getUserAgentString());
        binding.webViewFacebook.addJavascriptInterface(this, "FBDownloader");
        binding.webViewFacebook.setWebViewClient(new WebViewClient() {
            public void onLoadResource(WebView webView, String str) {
                try {
                    binding.txtLinkWeb.setText(webView.getOriginalUrl());
                    if (webView.getOriginalUrl() != null) {
                        //  Hawk.put("ADDRESS_BROWSE", webView.getOriginalUrl());
                        if (binding.webViewFacebook.canGoBack()) {
                            binding.btnBackBrowse.setAlpha(1.0f);
                        } else {
                            binding.btnBackBrowse.setAlpha(0.5f);
                        }
                        if (binding.webViewFacebook.canGoForward()) {
                            binding.btnNextBrowse.setAlpha(1.0f);
                        } else {
                            binding.btnNextBrowse.setAlpha(0.5f);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.webViewFacebook.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);console.log(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
                // binding.webViewFacebook.loadUrl("javascript: (function prepareVideo() {    var cl = document.querySelectorAll('div[class='_53mw _4gbu']');    var el = document.querySelectorAll('div[data-sigil]');    for (var i = 0; i < el.length; i++) {        var sigil = el[i].dataset.sigil;        if (sigil.indexOf('inlineVideo') > -1) {            delete el[i].dataset.sigil;            var jsonData = JSON.parse(el[i].dataset.store);            console.log(el[i].dataset.store);            el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"' + jsonData['src'] + '\",\"' + jsonData['videoID'] + '\");');            var buttonEl = document.createElement('a');            buttonEl.href = jsonData['src'];            var buttonTextEl = document.createElement('span');            buttonTextEl.className = 'picon-p-add-news';            buttonTextEl.innerText = 'DOWNLOAD_Video';            buttonEl.appendChild(buttonTextEl);            cl[i].appendChild(buttonEl);        }    }})()");
                binding.webViewFacebook.loadUrl("javascript:( window.onload=prepareVideo;)()");


                //   binding.webViewFacebook.loadUrl("javascript:window.FBDownloader.processVideo('" + str + "',''+document.getElementsByTagName('video')[0].setAttribute('onClick', 'FBDownloader.processVideo(\"' + document.getElementsByTagName('video')[0].getAttribute(\"src\") + '\",\"' + document.getElementsByTagName('video')[0].getAttribute(\"src\") + '\");');");
                //binding.webViewFacebook.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('video');for(var i=0;i<el.length; i++){var sigil = el[i].href;el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"' + sigil + '\",\"' + sigil + '\"');}})()");

//                  binding.webViewFacebook.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('video');for(var i=0;i<el.length; i++){var sigil = el[i].src; javascript:window.FBDownloader.processVideo(\"'+sigil+'\",\"'+sigil+'\");');})()");
//                  binding.webViewFacebook.loadUrl("javascript:( window.onload=prepareVideo;)()");
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);

                System.out.println("srcissss = " + str);
                if (str.contains(".mp4?")) {
                    String substring = str.substring(str.lastIndexOf("/") + 1, str.lastIndexOf("."));
                    processVideo(str, substring);
                }

                if (binding.webViewFacebook.canGoBack()) {
                    binding.btnBackBrowse.setAlpha(1.0f);
                } else {
                    binding.btnBackBrowse.setAlpha(0.5f);
                }
                if (binding.webViewFacebook.canGoForward()) {
                    binding.btnNextBrowse.setAlpha(1.0f);
                } else {
                    binding.btnNextBrowse.setAlpha(0.5f);
                }
            }
        });
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        binding.webViewFacebook.loadUrl("https://www.facebook.com/");
    }


    @JavascriptInterface
    public void processVideo(String str, String str2) {

        myvideoUrl = Uri.parse(str);
        System.out.println("NAME_VIDEO=" + str2 + ".mp4");
        System.out.println("URL_VIDEO=" + str);


        new AlertDialog.Builder(FacebookPrivateWebview.this)
                .setTitle(getString(R.string.savedstt))
                .setMessage(R.string.Areyousureyou_download)

                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadFileMain.startDownloading(FacebookPrivateWebview.this, myvideoUrl.toString(), str2, ".mp4");
                    }
                })

                .setNegativeButton(R.string.negative_btn, null)
                .setIcon(R.drawable.ic_appicon)
                .show();

    }


    public void onBackPressed() {
        if (binding.webViewFacebook.canGoBack()) {
            binding.webViewFacebook.goBack();
            return;
        }
        super.onBackPressed();
        // startActivity(new Intent(FacebookPrivateWebview.this, MainActivity.class));
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackBrowse:
                binding.webViewFacebook.goBack();
                return;
            case R.id.btnCloseBrowse:
                startActivity(new Intent(FacebookPrivateWebview.this, MainActivity.class));
                finish();
                return;
            case R.id.btnHomeBrowse:
                binding.webViewFacebook.loadUrl("https://www.facebook.com/");
                return;
            case R.id.btnNextBrowse:
                binding.webViewFacebook.goForward();
                return;

            case R.id.btnlogoutfb:


                new AlertDialog.Builder(FacebookPrivateWebview.this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.areyousure_logout)

                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                binding.webViewFacebook.clearHistory();
                                binding.webViewFacebook.clearFormData();
                                binding.webViewFacebook.clearCache(true);
                                CookieSyncManager.createInstance(FacebookPrivateWebview.this);
                                CookieManager cookieManager = CookieManager.getInstance();
                                cookieManager.removeAllCookie();

                                binding.webViewFacebook.loadUrl("https://www.facebook.com/");

                            }
                        })

                        .setNegativeButton(R.string.negative_btn, null)
                        .setIcon(R.drawable.ic_appicon)
                        .show();


                return;
            case R.id.btnPasteUrl:
                binding.txtNumberClipboard.setVisibility(View.GONE);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String text = clipboard.getText().toString();
                binding.txtLinkWeb.setText(text);
                binding.webViewFacebook.loadUrl(text);
                return;
            default:
                return;
        }
    }

}