package com.infusiblecoder.allinonevideodownloader.webservices;

import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.Mcontext;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.content.Context;
import android.os.AsyncTask;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Twenty_min_ch_Downloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;

    public Twenty_min_ch_Downloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallTwenty_min_chData().execute(VideoURL);
    }


    public static class CallTwenty_min_chData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            boolean isSecon = false;

            try {

                if (!fromService) {

                    pd.dismiss();
                }
                System.out.println("myresponseis111 exp166 " + document);

                String data = "";

                Elements elements = document.select("script");
                for (Element element : elements) {
                    if (element.attr("id").equals("__NEXT_DATA__")) {
                        //Save As you want to


                        System.out.println("myresponseis111 list_of_qualities" + element.html());


                        JSONObject obj = new JSONObject(element.html());


                        JSONObject urlsdata = obj.getJSONObject("props")
                                .getJSONObject("data")
                                .getJSONObject("content")
                                .getJSONObject("video")
                                .getJSONObject("content")
                                .getJSONArray("elements")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("elements").getJSONObject(0);


                        System.out.println("myresponseis111 list_of_qualities8888=" + urlsdata.toString());


                        String jsonArray = "";
                        if (urlsdata.has("url_high")) {
                            jsonArray = urlsdata.getString("url_high");
                        } else {
                            jsonArray = urlsdata.getString("url_low");

                        }


                        System.out.println("myresponseis111 list_of_qualities" + jsonArray);
                        DownloadFileMain.startDownloading(Mcontext, jsonArray, "Twenty_min_ch_" + System.currentTimeMillis(), ".mp4");


                    }
                }


            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                iUtils.ShowToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }


}


