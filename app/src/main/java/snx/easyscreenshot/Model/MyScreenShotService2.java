

package snx.easyscreenshot.Model;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.ToneGenerator;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;

import snx.easyscreenshot.BuildConfig;
import snx.easyscreenshot.R;
import snx.easyscreenshot.Utilities.Utility;
import snx.easyscreenshot.View.Activities.MainActivity;

//public class ScreenShotService {
//}
public class MyScreenShotService2 extends Service {
    private static final int NOTIFY_ID = 9906;
    static final String EXTRA_RESULT_CODE = "resultCode";
    static final String EXTRA_RESULT_INTENT = "resultIntent";
    static final String ACTION_RECORD = BuildConfig.APPLICATION_ID + ".RECORD";
    static final String ACTION_SHUTDOWN = BuildConfig.APPLICATION_ID + ".SHUTDOWN";
    static final int VIRT_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private MediaProjection projection;
    private VirtualDisplay vdisplay;
    final private HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_BACKGROUND);
    private Handler handler;
    private WindowManager windowManager;
    private MediaProjectionManager mediaProjectionManager;
    private int resultCode;
    private Intent resultData;
    final private ToneGenerator beeper = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext=this;

        Utility.showToast(mContext,"Service 2 started");
        if (intent.getAction() == null) {
            resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 1337);
            resultData = intent.getParcelableExtra(EXTRA_RESULT_INTENT);
            Utility.showToast(mContext,"Action null");
            foregroundify();
        } else if (intent.getAction().equals(ACTION_RECORD)) {
            if (resultData != null) {
                Utility.showToast(mContext,"Action Record Capture");

                startCapture();
            } else {
                Utility.showToast(mContext,"Action Record Else");
                Intent ui =
                        new Intent(this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(ui);
            }
        } else if (intent.getAction().equals(ACTION_SHUTDOWN)) {
            Utility.showToast(mContext,"Action Shut down");
            beeper.startTone(ToneGenerator.TONE_PROP_NACK);
            stopForeground(true);
            stopSelf();
        }

        return (START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stopCapture();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new IllegalStateException("Binding not supported. Go away.");
    }

    WindowManager getWindowManager() {
        return (windowManager);
    }

    Handler getHandler() {
        return (handler);
    }

    void processImage(final byte[] png) {
        new Thread() {
            @Override
            public void run() {
                File output = new File(getExternalFilesDir(null),
                        "screenshot.png");
                try {

                    FileOutputStream fos = new FileOutputStream(output);
                    fos.write(png);
                    fos.flush();
                    fos.getFD().sync();
                    fos.close();


                    MediaScannerConnection.scanFile(MyScreenShotService2.this,
                            new String[]{output.getAbsolutePath()},
                            new String[]{"image/png"},
                            null);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Exception writing out screenshot", e);
                }
            }
        }.start();

        beeper.startTone(ToneGenerator.TONE_PROP_ACK);
        stopCapture();
    }

    private void stopCapture() {
        if (projection != null) {
            projection.stop();
            vdisplay.release();
            projection = null;
        }
    }

    private void startCapture() {
        projection = mediaProjectionManager.getMediaProjection(resultCode, resultData);
        ImageTransmogrifier it = new ImageTransmogrifier(this);

        MediaProjection.Callback cb = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                vdisplay.release();
            }
        };

        vdisplay = projection.createVirtualDisplay("shooter",
                it.getWidth(), it.getHeight(),
                getResources().getDisplayMetrics().densityDpi,
                VIRT_DISPLAY_FLAGS, it.getSurface(), null, handler);
        projection.registerCallback(cb, handler);
    }

    private void foregroundify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        builder.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(getString(R.string.app_name));

        builder.addAction(R.mipmap.ic_launcher,
                getString(R.string.notify_record),
                buildPendingIntent(ACTION_RECORD));

        builder.addAction(R.mipmap.ic_launcher_round,
                getString(R.string.notify_shutdown),
                buildPendingIntent(ACTION_SHUTDOWN));

        startForeground(NOTIFY_ID, builder.build());
    }


    private PendingIntent buildPendingIntent(String action) {
        Intent i = new Intent(this, getClass());

        i.setAction(action);

        return (PendingIntent.getService(this, 0, i, 0));
    }
}