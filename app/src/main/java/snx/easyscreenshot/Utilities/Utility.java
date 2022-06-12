package snx.easyscreenshot.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import snx.easyscreenshot.R;

public class Utility {

    private static Toast univToast;
//
//    public static void takeScreenshot(Activity mActivity) {
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
//
//            // create bitmap screen capture
//            View v1 = mActivity.getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            openScreenshot(imageFile, mActivity);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
//    }
    public static void takeScreenshot(Activity mActivity) {

    }
    public static void openScreenshot(File imageFile, Activity mActivity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        mActivity.startActivity(intent);
    }

    public static void showToast(final Activity activity, final String text, final int duration) {
        activity.runOnUiThread(new Runnable() {
            public void run() {

                try {
                    univToast.cancel();
                }catch (Exception e){e.printStackTrace();}

                LayoutInflater layoutInflater= activity.getLayoutInflater();
                View viewLayout= layoutInflater.inflate(R.layout.custom_toast_layout, (ViewGroup)activity.findViewById(R.id.custom_toast_layout));
                TextView tv=(TextView)viewLayout.findViewById(R.id.text_view_toast) ;
                univToast= Toast.makeText(activity, text, duration);
                tv.setText(text);

//                t1.setGravity(Gravity.CENTER,0,0);
                univToast.setView(viewLayout);
                univToast.show();
            }
        });
    }
    public static void showToast( Context c,final String text) {


                try {
                    if(univToast!=null)
                    univToast.cancel();
                }catch (Exception e){e.printStackTrace();}

                univToast= Toast.makeText(c, text, Toast.LENGTH_LONG);

//                t1.setGravity(Gravity.CENTER,0,0);
                univToast.show();
            }
}
