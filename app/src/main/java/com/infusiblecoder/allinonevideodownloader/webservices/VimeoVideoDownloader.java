package com.infusiblecoder.allinonevideodownloader.webservices;

import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.infusiblecoder.allinonevideodownloader.interfaces.VideoDownloader;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class VimeoVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public VimeoVideoDownloader(Context context, String videoURL) {
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

    private static int countOccurences(String str, String word) {
        // split the string by spaces in a
        String[] a = str.split("\"");

        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }


    @Override
    public String getVideoId(String link) {
        return link;
    }

    @Override
    public void DownloadVideo() {
        new Data().execute(getVideoId(VideoURL));
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

                String buffer = "No URL";
                String Line;
                while ((Line = reader.readLine()) != null) {
                    if (Line.contains("og:title")) {
                        VideoTitle = Line.substring(Line.indexOf("og:title"));
                        VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle, "\"", 1) + 1, ordinalIndexOf(VideoTitle, "\"", 2));
                    }
                    if (Line.contains("og:video:url")) {
                        Line = Line.substring(Line.indexOf("og:video:url"));
                        Line = Line.substring(ordinalIndexOf(Line, "\"", 1) + 1, ordinalIndexOf(Line, "\"", 2));
                        if (!Line.contains("https")) {
                            Line = Line.replace("http", "https");
                        }
                        boolean checkURL = URLUtil.isValidUrl(Line);
                        if (checkURL) {
                            HttpURLConnection connection11;
                            BufferedReader reader1;
                            URL url1 = new URL(Line);
                            try {
                                connection11 = (HttpURLConnection) url1.openConnection();
                                connection11.connect();

                                InputStream stream1 = connection11.getInputStream();
                                reader1 = new BufferedReader(new InputStreamReader(stream1));

                                StringBuilder builder = new StringBuilder();
                                String str;
                                while ((str = reader1.readLine()) != null) {
                                    builder.append(str);
                                }
                                String hello = builder.toString();
                                int Counter = countOccurences(hello, "video/mp4");
                                if (Counter > 0) {
                                    for (int i = 0; i < Counter; i++) {
                                        String FinalURL = hello.substring(ordinalIndexOf(hello, "video/mp4", i));
                                        FinalURL = FinalURL.substring(FinalURL.indexOf("url") + 1);
                                        FinalURL = FinalURL.substring(FinalURL.indexOf("\"") + 1, FinalURL.indexOf("}"));
                                        if (FinalURL.contains("360p")) {
                                            FinalURL = FinalURL.substring(FinalURL.indexOf("\"") + 1, ordinalIndexOf(FinalURL, "\"", 1));
                                            Line = FinalURL;
                                            break;
                                        } else {
                                            FinalURL = FinalURL.substring(FinalURL.indexOf("\"") + 1, ordinalIndexOf(FinalURL, "\"", 1));
                                            Line = FinalURL;
                                        }
                                    }
                                }

                            } catch (IOException e) {
                                Line = "Wrong Video URL";
                            }
                        }
                        buffer = Line;
                        break;
                    } else {
                        buffer = "Wrong Video URL";
                    }

                }
                return buffer;
            } catch (IOException e) {
                if (!fromService) {

                    pd.dismiss();
                }
                return "Invalid Video URL or Check Internet Connection";

            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (URLUtil.isValidUrl(s)) {
                if (VideoTitle == null || VideoTitle.equals("")) {
                    VideoTitle = "VimeoVideo" + new Date().toString() + ".mp4";
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

}
