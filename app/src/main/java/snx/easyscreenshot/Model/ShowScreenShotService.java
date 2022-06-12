package snx.easyscreenshot.Model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.ProximityDetector;
import com.github.nisrulz.sensey.Sensey;

import snx.easyscreenshot.R;
import snx.easyscreenshot.Utilities.Utility;
import snx.easyscreenshot.View.Activities.DialogScrensshotTakenActivity;
import snx.easyscreenshot.View.Activities.MainActivity;

public class ShowScreenShotService extends Service {
    MediaPlayer myPlayer;
    private boolean isDown=false;
    private boolean isUp=false;
//    ScreenCaptureImage SCI;
    Context mContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        mContext= this;
          Toast.makeText(this, "Show Screenshot", Toast.LENGTH_LONG).show();
//          myPlayer.start();


//        SCI = new ScreenCaptureImage(MainActivity.mActivity);
        Intent popUpIntent = new Intent(this, DialogScrensshotTakenActivity.class);
        popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(popUpIntent);


        return Service.START_STICKY;
    }

    protected void Starto()
    {
        try {
            MainActivity.SCI.startProjection();
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation

       // Toast.makeText(getApplicationContext(),"Easy Service Started",Toast.LENGTH_LONG).show();
        return null;
    }


    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
//
//        myPlayer = MediaPlayer.create(this, R.raw.shutter);
//        myPlayer.setLooping(false); // Set looping
        Intent popUpIntent = new Intent(this, DialogScrensshotTakenActivity.class);
        popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(popUpIntent);
    }
    @Override
    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        myPlayer.start();
        Intent popUpIntent = new Intent(this, DialogScrensshotTakenActivity.class);
        popUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(popUpIntent);
    }
    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
//        myPlayer.stop();
    }
}