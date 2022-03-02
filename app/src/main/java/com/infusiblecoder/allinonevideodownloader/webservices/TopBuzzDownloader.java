package com.infusiblecoder.allinonevideodownloader.webservices;

import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.Mcontext;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.content.Context;
import android.os.AsyncTask;

import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;
import com.infusiblecoder.allinonevideodownloader.utils.iUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TopBuzzDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;

    public TopBuzzDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallTopBuzzDownloaderData().execute(VideoURL);
    }


    public static class CallTopBuzzDownloaderData extends AsyncTask<String, Void, Document> {
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
                    if (element.html().contains("__INITIAL_STATE__")) {
                        //Save As you want to

                        String list_of_qualities = element.html();


                        list_of_qualities = list_of_qualities.substring(list_of_qualities.indexOf("window.__INITIAL_STATE__ = JSON.parse(\"") + 39, list_of_qualities.indexOf("}}\");")) + "}}";



                        list_of_qualities = StringEscapeUtils.unescapeJava(list_of_qualities);

                        JSONObject obj = new JSONObject(list_of_qualities);


                        String replaceString = "https:"+obj.getJSONObject("story").getJSONObject("video").getString("videoUrl");

                        System.out.println("myresponseis111 exp ddd " + replaceString);


                       DownloadFileMain.startDownloading(Mcontext, replaceString, "TopBuzz_" + System.currentTimeMillis(), ".mp4");



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
