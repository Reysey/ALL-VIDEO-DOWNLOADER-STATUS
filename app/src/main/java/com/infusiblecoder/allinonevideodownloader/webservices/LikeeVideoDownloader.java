package com.infusiblecoder.allinonevideodownloader.webservices;

import static com.infusiblecoder.allinonevideodownloader.utils.Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE;
import static com.infusiblecoder.allinonevideodownloader.utils.iUtils.getFilenameFromURL;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.interfaces.VideoDownloader;
import com.infusiblecoder.allinonevideodownloader.utils.Constants;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LikeeVideoDownloader implements VideoDownloader {
    private static final Pattern a = Pattern.compile("window\\._INIT_PROPS_\\s*=\\s*(\\{.+\\})");
    String VideoUrl;
    Pattern pattern = Pattern.compile("window\\.data \\s*=\\s*(\\{.+?\\});");
    ProgressDialog progressDialog;
    AsyncTask downloadAsyncTask;
    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public LikeeVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    public static void down(String str) {
        String obj = "obj";
        String str2 = "";

        try {
            Matcher str1 = a.matcher(str);
            while (str1.find()) {
                JSONObject jSONObject = new JSONObject(str.replaceAll("window\\._INIT_PROPS_\\s*=\\s*", str2).replace("/v/:id", obj)).getJSONObject(obj).getJSONObject("videoData");
                JSONObject jSONObject2 = jSONObject.getJSONObject("itemInfos");
                jSONObject.getJSONObject("authorInfos");
                String obj2 = jSONObject2.getJSONObject("video").getJSONArray("urls").get(0).toString();
                String obj3 = jSONObject2.getJSONArray("covers").get(0).toString();

            }
        } catch (Exception e2) {
            e2.printStackTrace();

        }

    }

    public static String a() {
        return a(new int[]{103896, 115884, 115884, 111888, 114885, 57942, 46953, 46953, 118881, 118881, 118881, 45954, 106893, 96903, 115884, 96903, 113886, 108891, 96903, 107892, 45954, 104895, 109890, 46953, 117882, 48951, 46953, 111888, 113886, 110889, 98901, 100899, 114885, 114885, 84915, 113886, 107892});
    }

    public static String a(int[] iArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : iArr) {
            stringBuilder.append((char) (i / 999));
        }
        return stringBuilder.toString();
    }

    public static String b() {
        return a(new int[]{103896, 115884, 115884, 111888, 114885, 57942, 46953, 46953, 96903, 111888, 104895, 45954, 99900, 110889, 118881, 109890, 107892, 110889, 96903, 99900, 115884, 104895, 106893, 115884, 110889, 106893, 117882, 104895, 99900, 100899, 110889, 114885, 45954, 98901, 110889, 108891, 46953, 96903, 111888, 104895, 94905, 111888, 110889, 114885, 115884, 46953, 104895, 109890, 99900, 100899, 119880, 45954, 111888, 103896, 111888});
    }



    @Override
    public String getVideoId(String link) {
        if (!link.contains("https")) {
            link = link.replace("http", "https");
        }
        return link;
    }

    @Override
    public void DownloadVideo() {


        new callGetLikeeData().execute(getVideoId(VideoURL));
    }


    @SuppressLint("StaticFieldLeak")
    private class Data extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {

                if (!fromService) {

                    pd.dismiss();
                }
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String Line;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.e("Hello_myurl_is0 ", String.valueOf(reader.lines()));
                }
                while ((Line = reader.readLine()) != null) {
                    Log.e("Hello_myurl_is ", Line);
                    if (Line.contains("videoData")) {


                        Line = Line.substring(Line.indexOf("videoData"));
                        Log.e("Hello_myurl_is1 ", Line);
                        Line = Line.substring(Line.indexOf("urls"));
                        Log.e("Hello_myurl_is2 ", Line);
                        VideoTitle = Line.substring(Line.indexOf("text"));
                        if (VideoTitle.contains("#")) {
                            VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle, "\"", 1) + 1, ordinalIndexOf(VideoTitle, "#", 0));
                        } else {
                            VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle, "\"", 1) + 1, ordinalIndexOf(VideoTitle, "\"", 2));
                        }
                        System.out.println("myvideo titljklfjdfd " + VideoTitle);
                        Line = Line.substring(ordinalIndexOf(Line, "\"", 1) + 1, ordinalIndexOf(Line, "\"", 2));
                        Log.e("Hello_myurl_is3 ", Line);
                        if (!Line.contains("https")) {
                            Line = Line.replace("http", "https");
                        }
                        Log.e("Hello_myurl_is4 ", Line);

                        buffer.append(Line);
                        break;
                    }
                }
                return buffer.toString();
            } catch (IOException e) {
                if (!fromService) {

                    pd.dismiss();
                }
                return "Invalid Video URL or Check Internet Connection";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (URLUtil.isValidUrl(s)) {
                if (VideoTitle == null || VideoTitle.equals("")) {
                    VideoTitle = "TiktokVideo" + new Date().toString() + ".mp4";
                } else {
                    VideoTitle = VideoTitle + ".mp4";
                }

                DownloadFileMain.startDownloading(context, s, VideoTitle, ".mp4");

            } else {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }


    class callGetLikeeData extends AsyncTask<String, Void, Document> {
        Document likeeDoc;

        callGetLikeeData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.likeeDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return this.likeeDoc;
        }

        protected void onPostExecute(Document document) {

            try {

                String JSONData = "";
                Matcher matcher = pattern.matcher(document.toString());
                while (matcher.find()) {
                    JSONData = matcher.group().replaceFirst("window.data = ", "").replace(";", "");
                }
                JSONObject jsonObject = new JSONObject(JSONData);
                VideoUrl = jsonObject.getString("video_url").replace("_4", "");
                //    VideoUrl = VideoUrl.substring(0, VideoUrl.indexOf("{"));

                Log.e("onPostExecutenew14251: ", VideoUrl);

                if (!VideoUrl.equals("")) {
                    try {
                        pd.setMessage(context.getResources().getString(R.string.downloading_des));
                        downloadAsyncTask = new DownloadFileFromURL().execute(VideoUrl);

                        VideoUrl = "";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Log.e("onPostExecuteEonWrong", "Wrong url");
                }
            } catch (Exception document22) {
                document22.printStackTrace();
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output;

                Log.e("onPostExecuteOUTPUT0", "" + VideoUrl);

                String outputFileName = MY_ANDROID_10_IDENTIFIER_OF_FILE + getFilenameFromURL(VideoUrl) + ".mp4";


                if (outputFileName.length() > 100)
                    outputFileName = outputFileName.substring(0, 100);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    output = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constants.directoryInstaShoryDirectorydownload_videos + outputFileName));

                } else {
                    output = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Constants.directoryInstaShoryDirectorydownload_videos + outputFileName));


                }

                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error111: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {

            if (!fromService) {
                pd.dismiss();
            }
            iUtils.ShowToast(context, context.getResources().getString(R.string.completad));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (!fromService) {
                pd.dismiss();
            }

        }
    }

}
