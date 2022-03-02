package com.infusiblecoder.allinonevideodownloader.webservices;

import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.fromService;
import static com.infusiblecoder.allinonevideodownloader.webservices.DownloadVideosMain.pd;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.infusiblecoder.allinonevideodownloader.models.DlApismodels.VideoModel;
import com.infusiblecoder.allinonevideodownloader.utils.DownloadFileMain;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class DailyMotionDownloader {

    private Context context;
    private String Quality;
    private String FinalURL;
    private String VideoURL;
    private long DownLoadID;

    public DailyMotionDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    public void DownloadVideo() {
        new Data().execute(getVideoId(VideoURL));
    }

    private String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator);

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            boolean success1 = true;
            subFolder = new File(folder.getPath() + File.separator + "Dailymotion Videos");
            if (!subFolder.exists()) {
                success1 = subFolder.mkdirs();
            }
        }
        return subFolder.getPath();
    }

    public static String getVideoId(String link) {
        String videoId;
        if (link.contains("?")) {
            videoId = link.substring(link.indexOf("video/") + 1, link.indexOf("?"));
        } else {
            videoId = link.substring(link.indexOf("video/") + 1);
        }
        videoId = videoId.substring(videoId.lastIndexOf("/") + 1);
        return videoId;
    }


    private class Data extends AsyncTask<Object, Integer, ArrayList<VideoModel>> {
        ArrayList<VideoModel> videoModels = new ArrayList();
        private String videoModelsstr;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public ArrayList<VideoModel> doInBackground(Object... parm) {

            BufferedReader reader = null;
            try {
                URL url = new URL("https://www.dailymotion.com/embed/video/" + parm[0] + "?autoplay=1?__a=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String Line = "";
                while ((Line = reader.readLine()) != null) {
                    //   System.out.println("myfinalurlis 0= " + Line);

                    if (Line.contains("window.__PLAYER_CONFIG__ = {\"context\":")) {

                        String line2 = Line.substring(Line.indexOf("window.__PLAYER_CONFIG__ = {\"") + 27, Line.indexOf("};") + 1);


                        String manifest_M3u8url = new JSONObject(line2).getJSONObject("metadata").getJSONObject("qualities").getJSONArray("auto").getJSONObject(0).getString("url");

                        System.out.println("myfinalurlis 1=" + manifest_M3u8url);


                        try {
                            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(manifest_M3u8url).openConnection();
                            httpURLConnection.setConnectTimeout(60000);
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                            HashMap hashMap = null;
                            Pattern compile = Pattern.compile("\\d+");
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine == null) {
                                    bufferedReader.close();
                                    //  HomeActivity.this.customProgress.hideProgress();
                                    return this.videoModels;
                                } else if (readLine.equals("#EXTM3U")) {
                                    hashMap = new HashMap();
                                } else if (readLine.contains("#EXT-X-STREAM-INF")) {
                                    compile.matcher(readLine).find();
                                    if (hashMap != null) {
                                        VideoModel videoModel = new VideoModel();
                                        Log.e("TAG", "doInBackground: " + readLine);
                                        if (readLine.contains("PROGRESSIVE-URI")) {
                                            String[] split3 = readLine.split(",");
                                            String[] split4 = split3[3].split("=");
                                            split3[0].split("=");
                                            String[] split5 = split3[5].split("=");
                                            //   videoModel.setName(Uri.parse(split5[1].substring(1)).getLastPathSegment());
                                            videoModel.setQuality(split4[1].replace("\"", ""));
                                            videoModel.setUrl(split5[1].substring(1));
                                            this.videoModels.add(videoModel);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e("MyTag", e.toString());
                        }


                        return this.videoModels;


                    }
                }

            } catch (Exception e) {

                System.out.println("error is " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


        public ArrayList<VideoModel> removeDuplicates(ArrayList<VideoModel> arrayList) {
            TreeSet treeSet = new TreeSet(new Comparator<VideoModel>() {
                public int compare(VideoModel videoModel, VideoModel videoModel2) {
                    return videoModel.getQuality().equalsIgnoreCase(videoModel2.getQuality()) ? 0 : 1;
                }
            });
            treeSet.addAll(arrayList);
            return new ArrayList<>(treeSet);
        }


        protected void DownloadFiles(String decryptedurl) {
            try {
                URL u = new URL(decryptedurl);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/master.m3u8");

                FileOutputStream fos = new FileOutputStream(file);
                while ((length = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }


                List<String> lines = Collections.emptyList();

                lines =
                        Files.readAllLines(Paths.get(file.toString()), StandardCharsets.UTF_8);

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq master data = " + lines.get(i));
                    } else {
                        lines.remove(i);
                    }
                }


                URL u2 = new URL(lines.get(1));
                System.out.println("wojfdjhfdjh qqqq master data lone0=" + lines.get(0));

                InputStream is2 = u2.openStream();

                DataInputStream dis2 = new DataInputStream(is2);

                byte[] buffer2 = new byte[1024];
                int length2;
                File fileindex0 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a_1.m3u8");

                FileOutputStream fos2 = new FileOutputStream(fileindex0);
                while ((length2 = dis2.read(buffer2)) > 0) {
                    fos2.write(buffer, 0, length2);
                }


                List<String> lines2 = Collections.emptyList();

                lines2 = Files.readAllLines(Paths.get(fileindex0.toString()), StandardCharsets.UTF_8);


                for (int i = 0; i < lines2.size(); i++) {
                    if (lines2.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq datanew indexread= " + lines2.get(i));
                    } else {
                        lines2.remove(i);
                    }
                }


                URL u3 = new URL(lines.get(1));
                System.out.println("wojfdjhfdjh qqqq master data lone0=" + lines.get(0));

                InputStream is3 = u3.openStream();

                DataInputStream dis3 = new DataInputStream(is3);

                byte[] buffer3 = new byte[1024];
                int length3;

                File fileindex1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index_0_a_2.m3u8");

                FileOutputStream fos3 = new FileOutputStream(fileindex1);
                while ((length3 = dis3.read(buffer3)) > 0) {
                    fos3.write(buffer, 0, length3);
                }


                List<String> lines3 = Collections.emptyList();

                lines3 = Files.readAllLines(Paths.get(fileindex1.toString()), StandardCharsets.UTF_8);


                for (int i = 0; i < lines3.size(); i++) {
                    if (lines3.get(i).contains("http")) {
                        System.out.println("wojfdjhfdjh qqqq datanew indexread2= " + lines3.get(i));
                    } else {
                        lines3.remove(i);
                    }
                }


            } catch (Exception mue) {
                mue.printStackTrace();
                Log.e("SYNC getUpdate", mue.getMessage());
            }
        }


        public void onPostExecute(ArrayList<VideoModel> arrayList) {
            super.onPostExecute(arrayList);

            try {
                ArrayList<VideoModel> removeDuplicates = this.removeDuplicates(arrayList);
                this.videoModels = removeDuplicates;
                if (removeDuplicates != null && removeDuplicates.size() > 0) {


                    ArrayList<VideoModel> removeDuplicatesvalues = removeDuplicates(this.videoModels);


                    CharSequence[] charSequenceArr = new CharSequence[removeDuplicatesvalues.size()];

                    System.out.println("fjsdjkhfsdjfsd =" + removeDuplicatesvalues.size());

                    for (int i = 0; i < removeDuplicatesvalues.size(); i++) {
                        System.out.println("mybsdfghsdgfhjsd =" + removeDuplicatesvalues.get(i).getQuality());
                        charSequenceArr[i] = removeDuplicatesvalues.get(i).getQuality();
                    }

                    new AlertDialog.Builder(context).setTitle("Quality!").setItems(charSequenceArr, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DownloadFileMain.startDownloading(context, removeDuplicatesvalues.get(i).getUrl(), "Dailymotion_" + System.currentTimeMillis(), ".mp4");

                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!fromService) {

                                pd.dismiss();
                            }
                            dialogInterface.dismiss();
                        }
                    }).setCancelable(false).show();


                    if (!fromService) {

                        pd.dismiss();
                    }


                }


            } catch (Exception e) {

                System.out.println("sdfsfsfsdfsdfs =" + e.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
            }
        }
    }
}
