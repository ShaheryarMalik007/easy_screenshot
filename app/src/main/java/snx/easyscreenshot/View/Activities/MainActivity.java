package snx.easyscreenshot.View.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nisrulz.sensey.ChopDetector;
import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.github.nisrulz.sensey.WaveDetector;
import com.github.nisrulz.sensey.WristTwistDetector;
import com.marcinorlowski.fonty.Fonty;
import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.io.File;

import snx.easyscreenshot.BuildConfig;
import snx.easyscreenshot.Model.ChatHeadService;
import snx.easyscreenshot.Model.HUD;
import snx.easyscreenshot.Model.MyScreenShotService2;
import snx.easyscreenshot.Model.MyScreenshotService;
import snx.easyscreenshot.Model.ScreenCaptureImage;
import snx.easyscreenshot.Model.ScreenshotManager;
import snx.easyscreenshot.R;
import snx.easyscreenshot.Utilities.Utility;

public class MainActivity extends Activity {

    private static final String TAG = "Easy Screen shot result";
    public  static Activity mActivity;
    private boolean isNeverAskAgainStorage;
    public static ScreenCaptureImage SCI;

    static ImageView imgScreenShot;

    boolean isDown, isUp;

    AlertDialog.Builder builder;
    AlertDialog mDialog;
    public static Intent intentSS = new Intent();


    public static ScreenshotManager SSM;
    private BubblesManager bubblesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mActivity =this;
        imgScreenShot =(ImageView)findViewById(R.id.img_screen_shot);
//
//        initBubble();

        SCI = new ScreenCaptureImage(mActivity);

        Intent i= new Intent(mActivity, MyScreenshotService.class);
// potentially add data to the intent
        i.putExtra("KEY1", "Value to be used by the service");
        mActivity.startService(i);

        SSM = new ScreenshotManager();

        SSM.requestScreenshotPermission(MainActivity.mActivity,100);

//        Intent ii= new Intent(mActivity, HUD.class);
//// potentially add data to the intent
//        ii.putExtra("KEY1", "Value to be used by the service");
//        mActivity.startService(ii);

//        intentSS.setClass(MainActivity.this, DialogScrensshotTakenActivity.class);
//        intentSS.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // click button and show the dialog after 10s

//        Intent ii= new Intent(mActivity, MyScreenShotService2.class);
//        ii.setAction(BuildConfig.APPLICATION_ID + ".RECORD");
//        mActivity.startService(ii);

//        Intent ii= new Intent(mActivity, ChatHeadService.class);
//        ii.setAction(BuildConfig.APPLICATION_ID + ".RECORD");
//        mActivity.startService(ii);
// potentially add data to the intent

        Sensey.getInstance().init(mActivity);


        final Button btn = (Button)findViewById(R.id.my_btn);
        final Button btn2 = (Button)findViewById(R.id.my_btn2);
        final Button btn3 = (Button)findViewById(R.id.my_btn3);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActBottom = new Intent(MainActivity.this,Main2ActivityBottom.class);
                startActivity(ActBottom);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActNav = new Intent(MainActivity.this,Main2ActivityNavigation.class);
                startActivity(ActNav);
            }
        });
//        SCI.startProjection();

//        FlipDetector.FlipListener flipListener=new FlipDetector.FlipListener() {
//            @Override public void onFaceUp() {
//                // Device Facing up
//                isDown =true;
//
////                Utility.showToast(mActivity,"Face Up", Toast.LENGTH_LONG);
//                if(isUp && isDown)
//                {
//                    btn.performClick();
//                    isDown=false;
//                    isUp=false;
//                }
//            }
//
//            @Override public void onFaceDown() {
//                // Device Facing down
//                isUp= true;
//
////                Utility.showToast(mActivity,"Face Down", Toast.LENGTH_LONG);
//                if(isUp && isDown)
//                {
//                    btn.performClick();
//                    isDown=false;
//                    isUp=false;
//                }
//            }
//        };
//
//        Sensey.getInstance().startFlipDetection(flipListener);



//        ChopDetector.ChopListener mChopListener = new ChopDetector.ChopListener() {
//            @Override
//            public void onChop() {
//
//                btn.performClick();
//                Utility.showToast(mActivity,"Chop it", Toast.LENGTH_LONG);
//            }
//        };
//        Sensey.getInstance().startChopDetection(mChopListener);

//
//

//        ProximityDetector.ProximityListener mProxmityListener = new ProximityDetector.ProximityListener() {
//            @Override
//            public void onFar() {
//
////                Utility.showToast(mActivity,"Far", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onNear() {
//
//                btn.performClick();
////                Utility.showToast(mActivity,"Near", Toast.LENGTH_LONG);
//            }
//        };
//        Sensey.getInstance().startProximityDetection(mProxmityListener);
//
//        ShakeDetector.ShakeListener mShakeListener = new ShakeDetector.ShakeListener(){
//
//            @Override
//            public void onShakeDetected() {
//                Utility.showToast(mActivity,"Shake Detected", Toast.LENGTH_LONG);
////                btn.performClick();
//            }
//
//            @Override
//            public void onShakeStopped() {
//
//            }
//        };
//
//        Sensey.getInstance().startShakeDetection(mShakeListener);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CheckPermission();
                } else {
                    //Utility.takeScreenshot(mActivity);
                   Capture();
                }
            }
        });


        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Fonty.setFonts(mActivity);
                }catch (Exception e){e.printStackTrace();}
            }
        });

    }

    private void initBubble() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.bubble_layout,null);
//        mBadge = (NotificationBadge)bubbleView.findViewById(R.id.count);
//        mBadge.setNumber(88);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView,60,20);
    }

    public static void setImage(File f){
        imgScreenShot.setImageURI(Uri.fromFile(f));
    }
    public static void setImageBitmap(Bitmap f){
        imgScreenShot.setImageBitmap(f);
    }
    void Capture()
    {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Utility.takeScreenshot(mActivity);
//                ScreenshotAdapter.getInstance().capture(mActivity, icl);
                try {
                    SSM.requestScreenshotPermission(MainActivity.mActivity,100);
//                    SCI.startProjection();
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED )
        {
            if (isNeverAskAgainStorage) {
                ShowPermissionWarning("Go to settings", "You selected 'Never ask again' but Phone permission is compulsory for Manawanui. You can still allow permission from settings. Go to settings and select permissions");
            } else
                ActivityCompat.requestPermissions(mActivity, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 6);

        }
        else
        {

            Capture();
//            Utility.takeScreenshot(mActivity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Capture();
//            Utility.takeScreenshot(mActivity);
        } else if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    isNeverAskAgainStorage = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        SCI.OnActivityResult(requestCode,resultCode,data);
        SSM.onActivityResult(resultCode, data);
    }

    public void ShowScreenShottaken()
    {
        startActivity(intentSS);
//        this.finish();
    }

    private void ShowPermissionWarning(final String retry, String desc) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_permission_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCancelable(true);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.START);
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView tvHeading = (TextView) dialog.findViewById(R.id.tv_heading);
        TextView tvDesc = (TextView) dialog.findViewById(R.id.tv_desc);
        TextView tvRetry = (TextView) dialog.findViewById(R.id.tv_retry);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        tvHeading.setText("Phone Permission Required");
        tvDesc.setText(desc);
        tvRetry.setText(retry);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (retry.equalsIgnoreCase("retry")) {
                    CheckPermission();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        SCI.stopProjection();
//        SCI = null;

        // Stop Sensey and release the context held by it
        Sensey.getInstance().stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //        @Override
//    public void onResult(int result, Bitmap bitmap) {
//        if(result == Result.FAILED)
//        {
//            Log.d(TAG, "Failed");
//            return;
//        }
//        Log.d(TAG, "Succeeded");
//
//        boolean res = ScreenshotAdapter.getInstance().saveToFolder(bitmap, "Screenshots", "MyScreenshot");
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
}
