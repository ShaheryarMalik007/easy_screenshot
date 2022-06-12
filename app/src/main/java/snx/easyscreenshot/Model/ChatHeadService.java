package snx.easyscreenshot.Model;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

import snx.easyscreenshot.Global.Globals;
import snx.easyscreenshot.R;
import snx.easyscreenshot.View.Activities.DialogScrensshotTakenActivity;
import snx.easyscreenshot.View.Activities.MainActivity;

import static android.content.Context.WINDOW_SERVICE;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;

    WindowManager.LayoutParams params;

    Bitmap bmp;
    private byte[] byteArray;
    ImageView imgScreenShotTaken;

    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ChatHeadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatHeadService.this;
        }
    }

    public ChatHeadService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        try {
//            byteArray = intent.getByteArrayExtra("image");
//            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        }catch (Exception e){e.printStackTrace();}


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void UpdateUI()
    {
        String ff= ((Globals)this.getApplicationContext()).GetFile();
        Uri uri=null;
        if(ff!=null)
            uri = Uri.parse(ff);

        if(ff!=null)
            Log.d("Screenshot URI", ff );

        Toast.makeText(MainActivity.mActivity, "URI:"+ff, Toast.LENGTH_SHORT).show();
//        Language language = new Language();
        //Inflate the chat head layout we created
        if(mChatHeadView==null)
        {
            mChatHeadView = LayoutInflater.from(this).inflate(R.layout.activity_screenshot_taken_dialog, null);
//        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.dialog_incoming_call, null);
            int layOutType= WindowManager.LayoutParams.TYPE_PHONE;




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                layOutType  = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;




            {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        layOutType,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.LEFT | Gravity.BOTTOM;
                params.x = 0;
                params.y = 100;
                mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                mWindowManager.addView(mChatHeadView, params);

            }
//            else {
//                params = new WindowManager.LayoutParams(
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        PixelFormat.TRANSLUCENT);
//
//
//                params.gravity = Gravity.LEFT | Gravity.BOTTOM;
//                params.x = 0;
//                params.y = 100;
//                mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                mWindowManager.addView(mChatHeadView, params);
//
//            }
            Log.i("Dialog", "onCreate");

            Button dismissbutton = (Button)mChatHeadView. findViewById(R.id.button1);

            imgScreenShotTaken =(ImageView)mChatHeadView.findViewById(R.id.img_screen_shot_taken);
            dismissbutton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Log.i("Dialog", "onClick");
                    try {
                        mChatHeadView.setVisibility(View.GONE);
                    }catch (Exception e){e.printStackTrace();}
                }
            });
        }

        mChatHeadView.setVisibility(View.VISIBLE);
       //            try {
//                imgScreenShotTaken.setImageBitmap(bmp);
//            }catch (Exception d){d.printStackTrace();}

        imgScreenShotTaken.setImageURI(uri);


    }

//        TextView tvTitle=mChatHeadView.findViewById(R.id.tvTitle);
//        tvTitle.setText("Incoming Call");
//
//        //Set the close button.
//        Button btnReject = (Button) mChatHeadView.findViewById(R.id.btnReject);
//        btnReject.setText(language.getText(R.string.reject));
//        btnReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //close the service and remove the chat head from the window
//                stopSelf();
//            }
//        });
//
//        //Drag and move chat head using user's touch action.
//        final Button btnAccept = (Button) mChatHeadView.findViewById(R.id.btnAccept);
//        btnAccept.setText(language.getText(R.string.accept));
//
//
//        LinearLayout linearLayoutMain=mChatHeadView.findViewById(R.id.linearLayoutMain);


//        linearLayoutMain.setOnTouchListener(new View.OnTouchListener() {
//            private int lastAction;
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//
//                        //remember the initial position.
//                        initialX = params.x;
//                        initialY = params.y;
//
//                        //get the touch location
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//
//                        lastAction = event.getAction();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        //As we implemented on touch listener with ACTION_MOVE,
//                        //we have to check if the previous action was ACTION_DOWN
//                        //to identify if the user clicked the view or not.
//                        if (lastAction == MotionEvent.ACTION_DOWN) {
//                            //Open the chat conversation click.
//                            Intent intent = new Intent(ChatHeadService.this, HomeActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            //close the service and remove the chat heads
//                            stopSelf();
//                        }
//                        lastAction = event.getAction();
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        //Calculate the X and Y coordinates of the view.
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//
//                        //Update the layout with new X & Y coordinate
//                        mWindowManager.updateViewLayout(mChatHeadView, params);
//                        lastAction = event.getAction();
//                        return true;
//                }
//                return false;
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
        }catch (Exception e){e.printStackTrace();}

    }
}