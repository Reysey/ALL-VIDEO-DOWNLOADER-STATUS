package com.infusiblecoder.allinonevideodownloader.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.infusiblecoder.allinonevideodownloader.BuildConfig;
import com.infusiblecoder.allinonevideodownloader.activities.MainActivity;
import com.infusiblecoder.allinonevideodownloader.R;
import com.infusiblecoder.allinonevideodownloader.fragments.DownloadMainFragment;
import com.infusiblecoder.allinonevideodownloader.utils.MyClipboardManager;

import org.jetbrains.annotations.Nullable;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FloatingWidgetDownload extends Service {
    public static final String EXTRA_RESULT_CODE = "resultCode";
    public static final String EXTRA_RESULT_INTENT = "resultIntent";
    public static final String ACTION_RECORD =
            BuildConfig.APPLICATION_ID + ".RECORD";
    public static final String ACTION_SHUTDOWN =
            BuildConfig.APPLICATION_ID + ".SHUTDOWN";
    public static final int VIRT_DISPLAY_FLAGS =
            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    public static final String EXTRA_RESULT_screenrecordCODE = "resultCodescreenrecord";
    public static final String EXTRA_RESULT_screenrecordINTENT = "resultIntentscreenrecord";
    public static final String ACTION_screenRECORD =
            BuildConfig.APPLICATION_ID + ".RECORD";
    public static final String ACTION_STOP =
            BuildConfig.APPLICATION_ID + ".STOP";
    private static final String CHANNEL_WHATEVER = "channel_whatever";
    private static final int NOTIFY_ID = 9906;
    private static final String TAG = "RecordingManager";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int videoTime = 5000;
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1000;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    private static DisplayMetrics displayMetrics;
    private static boolean recording;
    private static String outputPath;
    private static int DISPLAY_WIDTH = 720;
    private static int DISPLAY_HEIGHT = 1280;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    final private HandlerThread handlerThread =
            new HandlerThread(getClass().getSimpleName(),
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
    final private ToneGenerator beeper =
            new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    private MediaProjection projection;
    private VirtualDisplay vdisplay;
    private Handler handler;
    private MediaProjectionManager mgr;
    private WindowManager wmgr;
    private int resultCode;
    private Intent resultData;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private boolean isForeground = false;
    private boolean recordOnNextStart = false;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private int mScreenDensity;
    private String screenShotUri = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("worki111n 1");
        mgr = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        wmgr = (WindowManager) getSystemService(WINDOW_SERVICE);


        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        RelativeLayout collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        //    final View expandedView = mFloatingView.findViewById(R.id.expanded_container);


        //Set the close button
        mFloatingView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window

                beeper.startTone(ToneGenerator.TONE_PROP_NACK);
                stopForeground(true);
                stopSelf();
            }
        });


        mFloatingView.findViewById(R.id.collapsed_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.GONE);
//TODO

                startActivity(new Intent(FloatingWidgetDownload.this.getApplicationContext(), MainActivity.class)

                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        String mytextis = MyClipboardManager.readFromClipboard(FloatingWidgetDownload.this);
                        MainActivity mainActivity = new MainActivity();

                        Bundle bundle = new Bundle();
                        bundle.putString("myinstaurl", mytextis);
                        Fragment fragobj = new DownloadMainFragment();
                        fragobj.setArguments(bundle);

                        mainActivity.setmydata(mytextis);

                        Toast.makeText(FloatingWidgetDownload.this.getApplicationContext(), "Starting Downlaod From Coppied text  " + mytextis, Toast.LENGTH_LONG).show();

                        //  downloadVideo.Start(FloatingWidgetDownload.this.getApplicationContext(), mytextis, true);


                    }
                }, 1000);
                collapsedView.setVisibility(View.VISIBLE);

            }
        });

        collapsedView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                //  collapsedView.setVisibility(View.GONE);
                                // expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });


        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        //   init();


    }


    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
//        if (i.getAction() == null) {
//            resultCode = i.getIntExtra(EXTRA_RESULT_CODE, 1337);
//            resultData = i.getParcelableExtra(EXTRA_RESULT_INTENT);
//            foregroundify();
//
//
//            if (recordOnNextStart) {
//                //               startRecorder();
//            }
//
//            foregroundify(!recordOnNextStart);
//            recordOnNextStart = false;
//
//        } else if (ACTION_RECORD.equals(i.getAction())) {
//
//            System.out.println("worki111n 2");
//
//            if (resultData != null) {
//                System.out.println("worki111n 3");
//
//
//            } else {
//                System.out.println("worki111n 4");
//
////                Intent ui=
////                        new Intent(this, HomeActivity.class)
////                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////
////                startActivity(ui);
//            }
//        } else if (ACTION_SHUTDOWN.equals(i.getAction())) {
//            beeper.startTone(ToneGenerator.TONE_PROP_NACK);
//            stopForeground(true);
//            stopSelf();
//        } else if (ACTION_screenRECORD.equals(i.getAction())) {
//
//
//        } else if (ACTION_STOP.equals(i.getAction())) {
//            foregroundify(true);
//
//        }


        return (START_NOT_STICKY);
    }


    @Override
    public void onDestroy() {


        stopForeground(true);
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        super.onDestroy();
    }


    private void foregroundify(boolean showRecord) {
        NotificationManager mgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                mgr.getNotificationChannel(this.getPackageName() + "floatservice3") == null) {
            mgr.createNotificationChannel(new NotificationChannel(this.getPackageName() + "floatservice3",
                    "Whatever", NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder b =
                new NotificationCompat.Builder(this, this.getPackageName() + "floatservice3");

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        b.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_appicon)
                .setTicker(getString(R.string.app_name));

        b.addAction(R.drawable.ic_download_color_24dp,
                "Take Screenshot",
                buildPendingIntent(ACTION_RECORD));

        b.addAction(R.drawable.ic_clear_black_24dp,
                "Close", buildPendingIntent(ACTION_SHUTDOWN));

        if (isForeground) {
            mgr.notify(NOTIFY_ID, b.build());
        } else {
            startForeground(NOTIFY_ID, b.build());
            isForeground = true;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new IllegalStateException("Binding not supported. Go away.");
    }


    private void foregroundify() {
        System.out.println("worki111n 7");

        NotificationManager mgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                mgr.getNotificationChannel(this.getPackageName() + "floatservice1") == null) {
            mgr.createNotificationChannel(new NotificationChannel(this.getPackageName() + "floatservice1",
                    "Whatever", NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder b =
                new NotificationCompat.Builder(this, this.getPackageName() + "floatservice1");

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        b.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_appicon)
                .setTicker(getString(R.string.app_name));

        b.addAction(R.drawable.ic_download_color_24dp,
                "Take Screenshot",
                buildPendingIntent(ACTION_RECORD));

        b.addAction(R.drawable.ic_clear_black_24dp,
                "Close",
                buildPendingIntent(ACTION_SHUTDOWN));

        startForeground(NOTIFY_ID, b.build());
    }

    private PendingIntent buildPendingIntent(String action) {
        Intent i = new Intent(this, getClass());

        i.setAction(action);

        return (PendingIntent.getService(this, 0, i, 0));
    }


}