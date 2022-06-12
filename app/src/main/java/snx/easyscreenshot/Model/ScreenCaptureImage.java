package snx.easyscreenshot.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import snx.easyscreenshot.R;
import snx.easyscreenshot.View.Activities.DialogScrensshotTakenActivity;
import snx.easyscreenshot.View.Activities.MainActivity;


public class ScreenCaptureImage implements OnActivityResult {

    Activity mActivity;

    private static final String TAG = ScreenCaptureImage.class.getName();
    private static final int REQUEST_CODE = 100;
    private static String STORE_DIRECTORY;
    private static int IMAGES_PRODUCED;
    private static final String SCREENCAP_NAME = "screencap";
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static MediaProjection sMediaProjection;

    private MediaProjectionManager mProjectionManager;
    private ImageReader mImageReader;
    private Handler mHandler;
    private Display mDisplay;
    private VirtualDisplay mVirtualDisplay;
    private int mDensity;
    private int mWidth;
    private int mHeight;
    private int mRotation;
    private OrientationChangeCallback mOrientationChangeCallback;


    boolean takeImage=false;
    FileOutputStream fos;
    File f;

    Intent intentDialog ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScreenCaptureImage(Activity mActivity) {
        this.mActivity = mActivity;


        // call for the projection manager
        mProjectionManager = (MediaProjectionManager) mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        try {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    mHandler = new Handler();
                    Looper.loop();
                }
            }.start();
        }catch (Exception e){e.printStackTrace();}

    }



    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            fos = null;
            Bitmap bitmap = null;

            if(takeImage)
            {

                takeImage =false;
                stopProjection();

                try {
//                    image = reader.acquireLatestImage();
//                    reader.acquireLatestImage();
                    image = reader.acquireNextImage();
                    if (image != null) {
                        Image.Plane[] planes = image.getPlanes();
                        ByteBuffer buffer = planes[0].getBuffer();
                        int pixelStride = planes[0].getPixelStride();
                        int rowStride = planes[0].getRowStride();
                        int rowPadding = rowStride - pixelStride * mWidth;

                        // create bitmap
                        bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(buffer);


                        final Bitmap bmp = Bitmap.createBitmap(bitmap,0,0,mWidth,mHeight);


                        // write bitmap to a file

                        AsyncTask ass = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {

                                 f = new File(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");
                                try {
                                    fos = new FileOutputStream(f);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    bmp.compress(CompressFormat.JPEG, 100, fos);
                                }catch (Exception e){e.printStackTrace();}
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);

//                                Intent iss= new Intent(mActivity, ShowScreenShotService.class);
//// potentially add data to the intent
//                                iss.putExtra("KEY1", "Value to be used by the service");
//                                mActivity.startService(iss);

//                                intentDialog =  new Intent(mActivity.getApplicationContext(), AlertDialogReceiver.class);
//
//                                mActivity.sendBroadcast(intentDialog);
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mActivity, "Screenshot Taken", Toast.LENGTH_SHORT).show();
//                                        MainActivity.setImage(f);
//                                        ((MainActivity)mActivity).ShowScreenShottaken();
//                                        Intent intentSS = new Intent();
//                                        intentSS.setClass(mActivity.getApplicationContext(), DialogScrensshotTakenActivity.class);
//                                        intentSS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        mActivity.startActivity(intentSS);


                                    }
                                });

                            }
                        };
                        ass.execute();


                        IMAGES_PRODUCED++;
                        Log.e(TAG, "captured image: " + IMAGES_PRODUCED);
//

                    }

                } catch (Exception e) {
                    e.printStackTrace();


                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }

                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    if (image != null) {
                        image.close();
                    }
                    takeImage =false;
                    stopProjection();
                }
            }
        }
    }

    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });
        }
    }

    /****************************************** UI Widget Callbacks *******************************/
    public void startProjection() {
        mActivity.startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);

                // start capture handling thread
//        takeImage=true;
    }

    public void stopProjection() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                    mVirtualDisplay.release();
//                    sMediaProjection=null;
                }
            }
        });
    }

    public void OnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {
                File externalFilesDir = mActivity.getExternalFilesDir(null);
                if (externalFilesDir != null) {
                    STORE_DIRECTORY = externalFilesDir.getAbsolutePath() + "/easyscreenshots/";
                    File storeDirectory = new File(STORE_DIRECTORY);
                    if (!storeDirectory.exists()) {
                        boolean success = storeDirectory.mkdirs();
                        if (!success) {
                            Log.e(TAG, "failed to create file storage directory.");
                            return;
                        }
                    }
                } else {
                    Log.e(TAG, "failed to create file storage directory, getExternalFilesDir is null.");
                    return;
                }

                // display metrics
                DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
                mDensity = metrics.densityDpi;
                mDisplay = mActivity.getWindowManager().getDefaultDisplay();

                // create virtual display depending on device width / height


                // register orientation change callback
                mOrientationChangeCallback = new OrientationChangeCallback(mActivity);
                if (mOrientationChangeCallback.canDetectOrientation()) {
                    mOrientationChangeCallback.enable();
                }

                // register media projection stop callback
                sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
            }
        }
    }




    /****************************************** Factoring Virtual Display creation ****************/
    public void createVirtualDisplay() {
        // get width and height
        Point size = new Point();
        mDisplay.getRealSize(size);
//        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 1);
        mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
        mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
    }
}