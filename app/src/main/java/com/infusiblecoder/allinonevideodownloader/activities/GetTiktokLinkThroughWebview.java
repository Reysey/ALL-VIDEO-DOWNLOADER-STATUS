

package com.infusiblecoder.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.databinding.ActivityGetLinkThroughWebviewBinding;
import com.infusiblecoder.allinonevideodownloader.models.DlApismodels.VideoModel;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import java.util.ArrayList;

import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

public class GetTiktokLinkThroughWebview extends AppCompatActivity {
    String url = "";
    ProgressDialog progressDialog;
    boolean isOnetime = false;
    private ArrayList<VideoModel> videoModelArrayList = new ArrayList<>();
    private ActivityGetLinkThroughWebviewBinding binding;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetLinkThroughWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(GetTiktokLinkThroughWebview.this);
        progressDialog.setMessage("Loading.... Note if it takes longer then 1 min close the app and retry");
        progressDialog.show();

        url = "https://audiomack.com/lightskinkeisha/song/fdh";


        // url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";

        if (getIntent().hasExtra("myurlis")) {
            url = getIntent().getStringExtra("myurlis");

        } else {
            url = "https://audiomack.com/embed/song/lightskinkeisha/fdh?background=1";
        }


        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(binding.browser, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        binding.browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        binding.browser.getSettings().setJavaScriptEnabled(true);
//        binding.browser.clearFormData();
//        binding.browser.getSettings().setSaveFormData(true);
//        binding.browser.getSettings().setAppCacheMaxSize(5242880);
//        binding.browser.getSettings().setAllowFileAccess(true);
//        binding.browser.getSettings().setAppCacheEnabled(true);
//        binding.browser.getSettings().getAllowFileAccess();
//        binding.browser.getSettings().setDefaultTextEncodingName("UTF-8");
//        binding.browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        binding.browser.getSettings().setDatabaseEnabled(true);
//        binding.browser.getSettings().setBuiltInZoomControls(false);
//        binding.browser.getSettings().setSupportZoom(false);
//        binding.browser.getSettings().setUseWideViewPort(true);
//        binding.browser.getSettings().setDomStorageEnabled(true);
//        binding.browser.getSettings().setAllowFileAccess(true);
//        binding.browser.getSettings().setLoadWithOverviewMode(true);
//        binding.browser.getSettings().setLoadsImagesAutomatically(true);
//        binding.browser.getSettings().setBlockNetworkImage(false);
//        binding.browser.getSettings().setBlockNetworkLoads(false);
//        binding.browser.getSettings().setLoadWithOverviewMode(true);


//        this.config = {
//                baseURL: 'https://api2.musical.ly/',
//                host: 'api2.musical.ly',
//                userAgent: `com.zhiliaoapp.musically/${reqParams.manifest_version_code}`
//        + ` (Linux; U; Android ${reqParams.os_version}; ${reqParams.language}_${reqParams.region};`
//        + ` ${reqParams.device_type}; Build/NHG47Q; Cronet/58.0.2991.0)`,
//      ...apiConfig,
//    } as API.TikTokAPIConfig;


        // binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10;TXY567) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/8399.0.9993.96 Mobile Safari/599.36");
        //  binding.browser.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.96 Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

        binding.browser.getSettings().setUserAgentString("com.zhiliaoapp.musically/2018052132 (Linux; U; Android 8.0.0; en_US; Pixel 2; Build/ABCXYZ; Cronet/58.0.2991.0)");
        // binding.browser.getSettings().setUserAgentString("com.zhiliaoapp.musically/2018052132 (Linux; Android 10;TXY567; en_US; SM-J700F; Build/MMB29K; Cronet/58.0.2991.0)");

        binding.browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (url.contains("tiktok")) {

                            // binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementById('RENDER_DATA').textContent);");
                            binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "','<html>'+document.getElementsByTagName('html')[0].textContent+'</html>');");
                            //binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.getElementsByTagName('video')[0].getAttribute(\"src\"));");
                            // binding.browser.loadUrl("javascript:window.HTMLOUT.showHTML('" + url + "',''+document.querySelectorAll(\"script[id='__NEXT_DATA__']\")[0].innerHTML));");

                        } else {
                            progressDialog.dismiss();

                            if (!fromService) {
                                pd.dismiss();
                                iUtils.ShowToast(GetTiktokLinkThroughWebview.this, getResources().getString(R.string.somthing));
                            }

                            Intent intent = new Intent();
                            setResult(2, intent);
                            finish();
                        }

                    }
                }, 1000);

            }
        });

        binding.browser.loadUrl(url);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onPausing() {


        if (binding.browser != null) {
            binding.browser.evaluateJavascript("javascript:document.querySelector('video').pause();", new ValueCallback() {
                public void onReceiveValue(Object obj) {
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResuming() {
        if (binding.browser != null) {
            binding.browser.evaluateJavascript("javascript:document.querySelector('video').play();", new ValueCallback() {
                public void onReceiveValue(Object obj) {
                }
            });
        }
    }


    class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void showHTML(final String myurl, final String html) {
            progressDialog.dismiss();

            System.out.println("myhtml res uu =" + myurl);
            System.out.println("myhtml res =" + html);


            if (!isOnetime) {
                isOnetime = true;


                if (myurl.contains("tiktok")) {

                    try {
//                        String afterDecode = URLDecoder.decode(html, "UTF-8");
//
//
//                        JSONObject jsonObject = new JSONObject(afterDecode);
//
//                        String video_id = jsonObject
//                                .getJSONObject("rpcRes")
//                                .getJSONObject("res")
//                                .getJSONObject("ItemInfo")
//                                .getJSONObject("ItemStruct")
//                                .getJSONObject("Video")
//                                .getJSONObject("PlayAddr")
//                                .getString("Uri");
//
//
//                        String ddurl = "https://api2-16-h2.musical.ly/aweme/v1/play/?video_id=" + video_id + "&vr_type=0&is_play_url=1&source=PackSourceEnum_PUBLISH&media_type=4&ratio=default&improve_bitrate=1";
//
//                        System.out.println("wojfdjhfdjh 111 " + ddurl);


                        //    new downloadFile().Downloading(GetTiktokLinkThroughWebview.this, html, "Tiktok_" + System.currentTimeMillis(), ".mp4");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // new downloadFile().Downloading(GetTiktokLinkThroughWebview.this, html, "Audiomack_" + System.currentTimeMillis(), ".mp3");

                } else {

                    progressDialog.dismiss();

                    if (!fromService) {
                        pd.dismiss();
                        iUtils.ShowToast(GetTiktokLinkThroughWebview.this, getResources().getString(R.string.somthing));
                    }

//                    Intent intent = new Intent();
//                    setResult(2, intent);
//                    finish();

                }

                System.out.println("htmlissss vid_url=" + html + " url=" + myurl);


//                Intent intent = new Intent();
//                intent.putExtra("MESSAGE", html);
//                setResult(2, intent);
//                finish();
            }

        }
    }


//    public class HttpHelper {
//        public static final OkHttpClient a = new Builder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).retryOnConnectionFailure(true).proxy(Proxy.NO_PROXY).build();
//        public static final Request.Builder b = new Request.Builder().addHeader("Connection", "close").addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
//
//        static {
//            System.loadLibrary("native-lib");
//        }
//
//        public static void a(String str, Context context, Callback callback) {
//            StringBuilder stringBuilder;
//            String stringBuilder2;
//            Object a;
//            Exception e;
//            MediaType parse;
//            String str2;
//            TreeMap treeMap;
//            Excluder excluder;
//            ww0 com_video_downloader_no_watermark_tiktok_ui_view_ww0;
//            bw0 com_video_downloader_no_watermark_tiktok_ui_view_bw0;
//            HashMap hashMap;
//            HashMap hashMap2;
//            Collection collection;
//            Collection arrayList;
//            Collection collection2;
//            Collection arrayList2;
//            boolean z;
//            boolean z2;
//            boolean z3;
//            boolean z4;
//            int i;
//            List list;
//            List arrayList3;
//            iw0 com_video_downloader_no_watermark_tiktok_ui_view_iw0;
//            String str3 = "params";
//            String str4 = "videoUrl";
//            String str5 = "headStr";
//            String str6 = "utf-8";
//            StringBuilder stringBuilder3 = new StringBuilder();
//            stringBuilder3.append("app_language=AS");
//            stringBuilder3.append("&language=AS");
//            stringBuilder3.append("&region=AS");
//            stringBuilder3.append("&sys_region=AS");
//            stringBuilder3.append("&carrier_region=&carrier_region_v2=");
//            int dSTSavings = (TimeZone.getDefault().getDSTSavings() / 1000) + (TimeZone.getDefault().getRawOffset() / 1000);
//            stringBuilder3.append("&timezone_offset=");
//            stringBuilder3.append(dSTSavings);
//            stringBuilder3.append("&timezone_name=");
//            stringBuilder3.append(Uri.encode(TimeZone.getDefault().getID()));
//            stringBuilder3.append("&mcc_mnc=&retry_type=no_retry&is_my_cn=1&fp=&pass-region=1&pass-route=1&ac=wifi&channel=googleplay&aid=1180&iid=6800628374143469314&app_name=trill&version_code=494&device_platform=android&ab_version=4.9.3&aid=1180&ssmix=a&manifest_version_code=494&update_version_code=4940");
//            int i2 = Resources.getSystem().getDisplayMetrics().heightPixels;
//            dSTSavings = Resources.getSystem().getDisplayMetrics().widthPixels;
//            stringBuilder3.append("&resolution=");
//            stringBuilder3.append(dSTSavings);
//            stringBuilder3.append("*");
//            stringBuilder3.append(i2);
//            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//            stringBuilder3.append("&dpi=");
//            stringBuilder3.append(displayMetrics.densityDpi);
//            stringBuilder3.append("&openudid=");
//            stringBuilder3.append(Secure.getString(context.getContentResolver(), "android_id"));
//            stringBuilder3.append("&device_type=");
//            stringBuilder3.append(Uri.encode(Build.MODEL));
//            stringBuilder3.append("&os_version=");
//            stringBuilder3.append(VERSION.RELEASE);
//            stringBuilder3.append("&os_api=");
//            stringBuilder3.append(VERSION.SDK_INT);
//            stringBuilder3.append("&device_brand=");
//            stringBuilder3.append(Build.BRAND);
//            try {
//                stringBuilder = new StringBuilder();
//                stringBuilder.append("com.ss.android.ugc.trill/494 ");
//                stringBuilder.append(WebSettings.getDefaultUserAgent(context));
//                stringBuilder2 = stringBuilder.toString();
//            } catch (Throwable th) {
//                th.printStackTrace();
//                stringBuilder2 = "com.ss.android.ugc.trill/494 Mozilla/5.0 (Linux; Android 10; HRY-AL00a Build/HONORHRY-AL00a; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/80.0.3987.99 Mobile Safari/537.36";
//            }
//            HashMap hashMap3 = new HashMap();
//            try {
//                hashMap3.put(str5, URLEncoder.encode(stringBuilder2, str6));
//                hashMap3.put(str3, URLEncoder.encode(stringBuilder3.toString(), str6));
//                hashMap3.put(str4, URLEncoder.encode(str, str6));
//            } catch (UnsupportedEncodingException e2) {
//                e2.printStackTrace();
//            }
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(System.currentTimeMillis());
//            str6 = "";
//            stringBuilder.append(str6);
//            hashMap3.put("userId", stringBuilder.toString());
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(System.currentTimeMillis());
//            stringBuilder.append(str6);
//            hashMap3.put("timestamp", stringBuilder.toString());
//            stringBuilder = new StringBuilder();
//            TreeMap treeMap2 = new TreeMap(hashMap3);
//            Excluder excluder2 = Excluder.f;
//            ww0 com_video_downloader_no_watermark_tiktok_ui_view_ww02 = ww0.a;
//            bw0 com_video_downloader_no_watermark_tiktok_ui_view_bw02 = bw0.a;
//            HashMap hashMap4 = r7;
//            HashMap hashMap5 = new HashMap();
//            Collection collection3 = r7;
//            Collection arrayList4 = new ArrayList();
//            Collection collection4 = r11;
//            Collection arrayList5 = new ArrayList();
//            boolean z5 = false;
//            boolean z6 = false;
//            boolean z7 = false;
//            int i3 = 2;
//            List list2 = r12;
//            List arrayList6 = new ArrayList((arrayList5.size() + arrayList4.size()) + 3);
//            arrayList6.addAll(arrayList4);
//            Collections.reverse(arrayList6);
//            arrayList4 = new ArrayList(arrayList5);
//            Collections.reverse(arrayList4);
//            arrayList6.addAll(arrayList4);
//            String str7 = str6;
//            iw0 com_video_downloader_no_watermark_tiktok_ui_view_iw02 = r7;
//            iw0 com_video_downloader_no_watermark_tiktok_ui_view_iw03 = new iw0(excluder2, com_video_downloader_no_watermark_tiktok_ui_view_bw02, hashMap4, false, false, false, false, z7, z5, z6, com_video_downloader_no_watermark_tiktok_ui_view_ww02, null, 2, i3, collection3, collection4, list2);
//            stringBuilder.append(com_video_downloader_no_watermark_tiktok_ui_view_iw02.f(treeMap2));
//            stringBuilder.append(getSafeKey(context));
//            try {
//                a = qu0.a(MessageDigest.getInstance("SHA-256").digest(stringBuilder.toString().getBytes("UTF-8")));
//            } catch (NoSuchAlgorithmException e3) {
//                e = e3;
//                e.printStackTrace();
//                a = str7;
//                hashMap3.put("sign", a);
//                parse = MediaType.parse("application/json");
//                getURL(context);
//                for (Entry entry : hashMap3.entrySet()) {
//                    if (str4.equals(entry.getKey())) {
//                        str2 = (String) entry.getKey();
//                        str2 = (String) entry.getValue();
//                        b.header((String) entry.getKey(), String.valueOf(entry.getValue()));
//                    }
//                }
//                treeMap = new TreeMap();
//                treeMap.put(str5, hashMap3.get(str5));
//                treeMap.put(str3, hashMap3.get(str3));
//                treeMap.put(str4, hashMap3.get(str4));
//                excluder = Excluder.f;
//                com_video_downloader_no_watermark_tiktok_ui_view_ww0 = ww0.a;
//                com_video_downloader_no_watermark_tiktok_ui_view_bw0 = bw0.a;
//                hashMap = hashMap2;
//                hashMap2 = new HashMap();
//                collection = arrayList;
//                arrayList = new ArrayList();
//                collection2 = arrayList2;
//                arrayList2 = new ArrayList();
//                z = false;
//                z2 = false;
//                z7 = false;
//                z3 = false;
//                z4 = false;
//                i = 2;
//                list = arrayList3;
//                arrayList3 = new ArrayList((arrayList2.size() + arrayList.size()) + 3);
//                arrayList3.addAll(arrayList);
//                Collections.reverse(arrayList3);
//                arrayList = new ArrayList(arrayList2);
//                Collections.reverse(arrayList);
//                arrayList3.addAll(arrayList);
//                com_video_downloader_no_watermark_tiktok_ui_view_iw0 = new iw0(excluder, com_video_downloader_no_watermark_tiktok_ui_view_bw0, hashMap, z, z4, false, false, z3, z2, z7, com_video_downloader_no_watermark_tiktok_ui_view_ww0, null, 2, i, collection, collection2, list);
//                com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap);
//                a.newCall(b.post(RequestBody.create(parse, com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap))).url(getURL(context)).build()).enqueue(callback);
//            } catch (UnsupportedEncodingException e4) {
//                e = e4;
//                e.printStackTrace();
//                a = str7;
//                hashMap3.put("sign", a);
//                parse = MediaType.parse("application/json");
//                getURL(context);
//                for (Entry entry2 : hashMap3.entrySet()) {
//                    if (str4.equals(entry2.getKey())) {
//                        str2 = (String) entry2.getKey();
//                        str2 = (String) entry2.getValue();
//                        b.header((String) entry2.getKey(), String.valueOf(entry2.getValue()));
//                    }
//                }
//                treeMap = new TreeMap();
//                treeMap.put(str5, hashMap3.get(str5));
//                treeMap.put(str3, hashMap3.get(str3));
//                treeMap.put(str4, hashMap3.get(str4));
//                excluder = Excluder.f;
//                com_video_downloader_no_watermark_tiktok_ui_view_ww0 = ww0.a;
//                com_video_downloader_no_watermark_tiktok_ui_view_bw0 = bw0.a;
//                hashMap = hashMap2;
//                hashMap2 = new HashMap();
//                collection = arrayList;
//                arrayList = new ArrayList();
//                collection2 = arrayList2;
//                arrayList2 = new ArrayList();
//                z = false;
//                z2 = false;
//                z7 = false;
//                z3 = false;
//                z4 = false;
//                i = 2;
//                list = arrayList3;
//                arrayList3 = new ArrayList((arrayList2.size() + arrayList.size()) + 3);
//                arrayList3.addAll(arrayList);
//                Collections.reverse(arrayList3);
//                arrayList = new ArrayList(arrayList2);
//                Collections.reverse(arrayList);
//                arrayList3.addAll(arrayList);
//                com_video_downloader_no_watermark_tiktok_ui_view_iw0 = new iw0(excluder, com_video_downloader_no_watermark_tiktok_ui_view_bw0, hashMap, z, z4, false, false, z3, z2, z7, com_video_downloader_no_watermark_tiktok_ui_view_ww0, null, 2, i, collection, collection2, list);
//                com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap);
//                a.newCall(b.post(RequestBody.create(parse, com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap))).url(getURL(context)).build()).enqueue(callback);
//            }
//            hashMap3.put("sign", a);
//            parse = MediaType.parse("application/json");
//            getURL(context);
//            for (Entry entry22 : hashMap3.entrySet()) {
//                if (!(str5.equals(entry22.getKey()) || str3.equals(entry22.getKey()))) {
//                    if (str4.equals(entry22.getKey())) {
//                        str2 = (String) entry22.getKey();
//                        str2 = (String) entry22.getValue();
//                        b.header((String) entry22.getKey(), String.valueOf(entry22.getValue()));
//                    }
//                }
//            }
//            treeMap = new TreeMap();
//            treeMap.put(str5, hashMap3.get(str5));
//            treeMap.put(str3, hashMap3.get(str3));
//            treeMap.put(str4, hashMap3.get(str4));
//            excluder = Excluder.f;
//            com_video_downloader_no_watermark_tiktok_ui_view_ww0 = ww0.a;
//            com_video_downloader_no_watermark_tiktok_ui_view_bw0 = bw0.a;
//            hashMap = hashMap2;
//            hashMap2 = new HashMap();
//            collection = arrayList;
//            arrayList = new ArrayList();
//            collection2 = arrayList2;
//            arrayList2 = new ArrayList();
//            z = false;
//            z2 = false;
//            z7 = false;
//            z3 = false;
//            z4 = false;
//            i = 2;
//            list = arrayList3;
//            arrayList3 = new ArrayList((arrayList2.size() + arrayList.size()) + 3);
//            arrayList3.addAll(arrayList);
//            Collections.reverse(arrayList3);
//            arrayList = new ArrayList(arrayList2);
//            Collections.reverse(arrayList);
//            arrayList3.addAll(arrayList);
//            com_video_downloader_no_watermark_tiktok_ui_view_iw0 = new iw0(excluder, com_video_downloader_no_watermark_tiktok_ui_view_bw0, hashMap, z, z4, false, false, z3, z2, z7, com_video_downloader_no_watermark_tiktok_ui_view_ww0, null, 2, i, collection, collection2, list);
//            com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap);
//            a.newCall(b.post(RequestBody.create(parse, com_video_downloader_no_watermark_tiktok_ui_view_iw0.f(treeMap))).url(getURL(context)).build()).enqueue(callback);
//        }
//
//        public static native String getSafeKey(Object obj);
//
//        public static native String getURL(Object obj);
//    }
//

}