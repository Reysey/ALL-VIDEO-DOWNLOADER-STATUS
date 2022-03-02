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

public class SolidfilesDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;

    public SolidfilesDownloader(Context context, String vid) {
        this.context = context;
        VideoURL = vid;
    }

    public void DownloadVideo() {
        new CallSolidfilesData().execute(VideoURL);
    }


    public static class CallSolidfilesData extends AsyncTask<String, Void, Document> {
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
                    if (element.html().contains("viewerOptions")) {
                        //Save As you want to

                        String list_of_qualities = element.html();


                        list_of_qualities = list_of_qualities.substring(list_of_qualities.indexOf("viewerOptions',") + 15, list_of_qualities.indexOf("});")) + "}";

                        JSONObject obj = new JSONObject(list_of_qualities);


                        String replaceString = obj.getString("downloadUrl");

                        String ext = replaceString.substring(replaceString.length() - 4);


                        System.out.println("myresponseis111 list_of_qualities" + replaceString);
                        if (ext.equals(".gif") || ext.equals("png")) {
                            DownloadFileMain.startDownloading(Mcontext, replaceString, "Solidfiles_" + System.currentTimeMillis(), ext);
                        } else {
                            DownloadFileMain.startDownloading(Mcontext, replaceString, "Solidfiles_" + System.currentTimeMillis(), ext);
                        }


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
