package snx.easyscreenshot.Model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import snx.easyscreenshot.BuildConfig;
import snx.easyscreenshot.Global.Globals;
import snx.easyscreenshot.View.Activities.DialogScrensshotTakenActivity;
import snx.easyscreenshot.View.Activities.MainActivity;

import static android.content.ContentValues.TAG;
import static android.hardware.display.DisplayManager.*;

public class ScreenshotManager {
    private static final String SCREENCAP_NAME = "screencap";

    private static String STORE_DIRECTORY;
    private static int IMAGES_PRODUCED;

    public static Intent ShowScreenShotInten;
    public static PendingIntent pendingIntent;
    public static AlarmManager alarmManager;

    FileOutputStream fos;
    File f;
    String ScreenShotURI;

    private static final int VIRTUAL_DISPLAY_FLAGS = VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | VIRTUAL_DISPLAY_FLAG_PUBLIC;
    public static final ScreenshotManager INSTANCE = new ScreenshotManager();
    private static Intent mIntent;
    private ByteArrayOutputStream stream;
    private byte[] byteArray;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private int density;
    private Display display;
    private Point size;
    private int width;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;
    private int height;
    private Intent ii;
    private ChatHeadService mService;
    private boolean mBound;

    public ScreenshotManager() {
    }

    public void requestScreenshotPermission(@NonNull Activity activity, int requestId) {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), requestId);
    }


    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null)
            mIntent = data;
//        else mIntent = null;
    }

    @UiThread
    public boolean takeScreenshot(@NonNull Context context) {
        if (mIntent == null)
        {
            requestScreenshotPermission(MainActivity.mActivity, 100);
            return false;
        }

         mediaProjectionManager = (MediaProjectionManager) MainActivity.mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mIntent);
        if (mediaProjection == null)
        {
            requestScreenshotPermission(MainActivity.mActivity, 100);
//            return false;
            return false;
        }

        File externalFilesDir = MainActivity.mActivity.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            STORE_DIRECTORY = externalFilesDir.getAbsolutePath() + "/easyscreenshots/";
            File storeDirectory = new File(STORE_DIRECTORY);
            if (!storeDirectory.exists()) {
                boolean success = storeDirectory.mkdirs();
                if (!success) {
                    Log.e(TAG, "failed to create file storage directory.");
                    return false;
                }
            }
        } else {
            Log.e(TAG, "failed to create file storage directory, getExternalFilesDir is null.");
            return false;
        }



//        if(imageReader!=null)
//        {
//            try {
//                imageReader.close();
//            }catch (Exception e){e.printStackTrace();}
//        }

        density = context.getResources().getDisplayMetrics().densityDpi;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        virtualDisplay = mediaProjection.createVirtualDisplay(SCREENCAP_NAME, width, height, density, VIRTUAL_DISPLAY_FLAGS, imageReader.getSurface(), null, null);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(final ImageReader reader) {
                Log.d("AppLog", "onImageAvailable");
                mediaProjection.stop();
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(final Void... params) {
                        Image image = null;
                        Bitmap bitmap = null;
                        try {
//                            image = reader.acquireLatestImage();
                            image = reader.acquireNextImage();
                            if (image != null) {
                                Image.Plane[] planes = image.getPlanes();
                                ByteBuffer buffer = planes[0].getBuffer();
                                int pixelStride = planes[0].getPixelStride(), rowStride = planes[0].getRowStride(), rowPadding = rowStride - pixelStride * width;
                                bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);

                                final Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,width,height);

                                ScreenShotURI =null;

                                f = new File(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");
                                try {
                                    fos = new FileOutputStream(f);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                }catch (Exception e){e.printStackTrace();}
//                                return null;
                                ScreenShotURI = Uri.fromFile(f).getPath();

                                image.close();
                                return bitmap;
                            }
                        } catch (Exception e) {
                            if (bitmap != null)
                                bitmap.recycle();
                            e.printStackTrace();
                        }
                        if (image != null)
                            image.close();
//                        if (bitmap != null)
//                            bitmap=null;
                        reader.close();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        Log.d("AppLog", "Got bitmap?" + (bitmap != null));
//                        Toast.makeText(MainActivity.mActivity, "Taken SS", Toast.LENGTH_SHORT).show();
                        IMAGES_PRODUCED++;
//                        ShowScreenShotInten = new Intent(MainActivity.mActivity, DialogScrensshotTakenActivity.class);
//
//                        try {
//                            stream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                            byteArray = stream.toByteArray();
//                        }catch (Exception e){e.printStackTrace();}
//
//                        try {
//                            ShowScreenShotInten.putExtra("image",byteArray);
//                        }catch (Exception e){e.printStackTrace();}

//                        ShowScreenShotInten.putExtra("SSURI", )



                       //                        try {
//                            ii.putExtra("image",byteArray);
//                        }catch (Exception e){e.printStackTrace();}
                        if(ScreenShotURI!=null)
                        {
                            ((Globals)MainActivity.mActivity.getApplicationContext()).SaveFile(ScreenShotURI);

//        ii.setAction(BuildConfig.APPLICATION_ID + ".RECORD");
                            if(ii==null)
                            {
                                ii= new Intent(MainActivity.mActivity, ChatHeadService.class);
                                MainActivity.mActivity.bindService(ii,mConnection , Context.BIND_AUTO_CREATE);
                            }

                            if(mBound)
                            mService.UpdateUI();
//                            MainActivity.mActivity.startService(ii);

                        }


//                        pendingIntent = PendingIntent.getActivity(MainActivity.mActivity, (int)System.currentTimeMillis(), ShowScreenShotInten, PendingIntent.FLAG_UPDATE_CURRENT);
//                        long futureInMillis = SystemClock.elapsedRealtime() + 1000;
//                        alarmManager = (AlarmManager)MainActivity.mActivity.getSystemService(Context.ALARM_SERVICE);
//                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
                    }
                }.execute();
            }
        }, null);
        mediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (virtualDisplay != null)
                    virtualDisplay.release();
                imageReader.setOnImageAvailableListener(null, null);
                mediaProjection.unregisterCallback(this);
            }
        }, null);
        return true;
    }zz
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ChatHeadService.LocalBinder binder = (ChatHeadService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}