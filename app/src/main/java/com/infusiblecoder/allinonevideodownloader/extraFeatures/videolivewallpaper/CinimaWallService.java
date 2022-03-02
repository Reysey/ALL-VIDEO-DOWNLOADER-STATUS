package com.infusiblecoder.allinonevideodownloader.extraFeatures.videolivewallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CinimaWallService extends WallpaperService {
    private static final int VIDEO_MUSIC = 2;
    private static final int VIDEO_URL = 1;
    public static String ACTION = "action";
    public static String VIDEO_ACTION = "com.infusiblecoder.allinonevideodownloader";
    private static String is_battery_saver = "is_battery_saver";
    private static String is_plzay_begin = "is_plzay_begin";
    private static String videoAudio = "vAudi";
    private static String videoPath = "viPath";

    public Engine onCreateEngine() {
        return new VideoWallpaperEngine();
    }

    public void setVidSource(Context context, String str) {
        SharedPrefUtils.saveData(context, videoPath, str);
        Intent intent = new Intent(VIDEO_ACTION);
        intent.putExtra(ACTION, 1);
        context.sendBroadcast(intent);
    }

    public boolean isEnableVideoAudio(Context context) {
        return SharedPrefUtils.getBooleanData(context, videoAudio);
    }

    public String getVideoSource(Context context) {
        return SharedPrefUtils.getStringData(context, videoPath);
    }

    public boolean isPlayB(Context context) {
        return SharedPrefUtils.getBooleanData(context, is_plzay_begin);
    }

    public void setEnableVideoAudio(Context context, boolean z) {
        SharedPrefUtils.saveData(context, videoAudio, z);
        Intent intent = new Intent(VIDEO_ACTION);
        intent.putExtra(ACTION, 2);
        context.sendBroadcast(intent);
    }

    public void setPlayB(Context context, boolean z) {
        SharedPrefUtils.saveData(context, is_plzay_begin, !isPlayB(context));
    }

    public void setPlayBatterySaver(Context context, boolean z) {
        SharedPrefUtils.saveData(context, is_battery_saver, !isBatterySaver(context));
    }

    public boolean isBatterySaver(Context context) {
        return SharedPrefUtils.getBooleanData(context, is_battery_saver);
    }

    class VideoWallpaperEngine extends Engine {
        public MediaPlayer mMediaPlayer;
        private BroadcastReceiver mVideoReceiver;

        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(CinimaWallService.VIDEO_ACTION);
            this.mVideoReceiver = new VideoVoiceControlReceiver();
            CinimaWallService.this.registerReceiver(this.mVideoReceiver, intentFilter);
        }

        public void onDestroy() {
            CinimaWallService.this.unregisterReceiver(this.mVideoReceiver);
            super.onDestroy();
        }

        public void onVisibilityChanged(boolean z) {
            if (!z) {
                this.mMediaPlayer.pause();
                return;
            }
            CinimaWallService cinimaWallService = CinimaWallService.this;
            if (cinimaWallService.isPlayB(cinimaWallService.getApplicationContext())) {
                this.mMediaPlayer.seekTo(0);
                this.mMediaPlayer.start();
                return;
            }
            this.mMediaPlayer.start();
        }

        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            super.onSurfaceCreated(surfaceHolder);
            CinimaWallService cinimaWallService = CinimaWallService.this;
            if (!TextUtils.isEmpty(cinimaWallService.getVideoSource(cinimaWallService.getApplicationContext()))) {
                try {
                    this.mMediaPlayer = new MediaPlayer();
                    this.mMediaPlayer.setSurface(surfaceHolder.getSurface());
                    float f = CinimaWallService.this.isEnableVideoAudio(CinimaWallService.this.getApplicationContext()) ? 1.0f : 0.0f;
                    this.mMediaPlayer.setVolume(f, f);
                    this.mMediaPlayer.setDataSource(CinimaWallService.this.getVideoSource(CinimaWallService.this.getApplicationContext()));
                    this.mMediaPlayer.setLooping(true);
                    this.mMediaPlayer.prepare();
                    this.mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new NullPointerException("Nullpoint Exception");
            }
        }

        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            super.onSurfaceChanged(surfaceHolder, i, i2, i3);
        }

        public void onSurfaceDestroyed(SurfaceHolder surfaceHolder) {
            super.onSurfaceDestroyed(surfaceHolder);
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                this.mMediaPlayer = null;
            }
        }

        class VideoVoiceControlReceiver extends BroadcastReceiver {
            VideoVoiceControlReceiver() {
            }

            public void onReceive(Context context, Intent intent) {
                int intExtra = intent.getIntExtra(CinimaWallService.ACTION, 0);
                if (intExtra == 1) {
                    if (VideoWallpaperEngine.this.mMediaPlayer != null) {
                        try {
                            VideoWallpaperEngine.this.mMediaPlayer.reset();
                            VideoWallpaperEngine.this.mMediaPlayer.setDataSource(CinimaWallService.this.getVideoSource(CinimaWallService.this.getApplicationContext()));
                            VideoWallpaperEngine.this.mMediaPlayer.prepare();
                            return;
                        } catch (Exception e) {
                            Log.d("Log", e.getStackTrace().toString());
                        }
                    }
                } else if (intExtra != 2) {
                    return;
                }
                if (VideoWallpaperEngine.this.mMediaPlayer != null) {
                    float f = CinimaWallService.this.isEnableVideoAudio(CinimaWallService.this.getApplicationContext()) ? 1.0f : 0.0f;
                    VideoWallpaperEngine.this.mMediaPlayer.setVolume(f, f);
                }
            }
        }
    }
}
