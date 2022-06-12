package snx.easyscreenshot.Model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;

import snx.easyscreenshot.R;
import snx.easyscreenshot.Utilities.Utility;
import snx.easyscreenshot.View.Activities.MainActivity;

public class MyScreenshotService extends Service {
    MediaPlayer myPlayer;
    private boolean isDown=false;
    private boolean isUp=false;
//    ScreenCaptureImage SCI;
    Context mContext;

    HUDView mView;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        mContext= this;
          Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
          myPlayer.start();


//        SCI = new ScreenCaptureImage(MainActivity.mActivity);



        Sensey.getInstance().init(this);




        FlipDetector.FlipListener flipListener=new FlipDetector.FlipListener() {
            @Override public void onFaceUp() {
                // Device Facing up
//                    Utility.showToast(mContext,"Face Up");
                isDown =true;
                if(isUp && isDown)
                {

                    Utility.showToast(mContext,"Clicko");
                    Starto();
//                    btn.performClick();
                    isDown=false;
                    isUp=false;
                }
            }

            @Override public void onFaceDown() {
                // Device Facing down
                isUp= true;

//                Utility.showToast(mContext,"Face Down");
                if(isUp && isDown)
                {

                    Starto();
                    Utility.showToast(mContext,"Clicko");
//                    btn.performClick();
                    isDown=false;
                    isUp=false;
                }
            }
        };

        Sensey.getInstance().startFlipDetection(flipListener);


        ProximityDetector.ProximityListener mProxmityListener = new ProximityDetector.ProximityListener() {
            @Override
            public void onFar() {

//                Utility.showToast(mContext,"Far");
            }

            @Override
            public void onNear() {

//                btn.performClick();
                Starto();
                Utility.showToast(MainActivity.mActivity,"Clicko");
//                Utility.showToast(mContext,"Near");
            }
        };
        Sensey.getInstance().startProximityDetection(mProxmityListener);
//        int layout_parms;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//
//        {
//            layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//
//        }
//
//        else {
//
//            layout_parms = WindowManager.LayoutParams.TYPE_PHONE;
//
//        }
//
//        mView = new HUDView(this);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                layout_parms,
//                0,
////                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.RIGHT | Gravity.TOP;
//        params.setTitle("Load Average");
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.addView(mView, params);

        return Service.START_STICKY;
    }

    protected void Starto()
    {
        try {


            MainActivity.SSM.takeScreenshot(MainActivity.mActivity);

//            MainActivity.SCI.createVirtualDisplay();
//            MainActivity.SCI.startProjection();
        }catch (Exception e){e.printStackTrace();}
    }
//
//    private void CheckPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED )
//        {
//            if (isNeverAskAgainStorage) {
//                ShowPermissionWarning("Go to settings", "You selected 'Never ask again' but Phone permission is compulsory for Manawanui. You can still allow permission from settings. Go to settings and select permissions");
//            } else
//                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 6);
//
//        }
//        else
//        {
//
//            Capture();
////            Utility.takeScreenshot(mActivity);
//        }
//    }
    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation

        Toast.makeText(getApplicationContext(),"Easy Service Started",Toast.LENGTH_LONG).show();
        return null;
    }


    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

        myPlayer = MediaPlayer.create(this, R.raw.shutter);
        myPlayer.setLooping(false); // Set looping

//        mView = new HUDView(this);
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                0,
////              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
////                      | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.RIGHT | Gravity.TOP;
//        params.setTitle("Load Average");
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        wm.addView(mView, params);
    }
    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        myPlayer.start();
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        myPlayer.stop();
    }

    class HUDView extends ViewGroup {
        private Paint mLoadPaint;

        public HUDView(Context context) {
            super(context);
            Toast.makeText(getContext(),"HUDView painted", Toast.LENGTH_LONG).show();

            mLoadPaint = new Paint();
            mLoadPaint.setAntiAlias(true);
            mLoadPaint.setTextSize(20);
//            mLoadPaint.setARGB(128, 128, 128, 0);
            mLoadPaint.setARGB(40, 150, 150, 150);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawText("Hello World", 5, 15, mLoadPaint);
        }

        @Override
        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);
            Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
