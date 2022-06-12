package snx.easyscreenshot.View.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import snx.easyscreenshot.R;

public  class DialogScrensshotTakenActivity extends Activity {
    AlertDialog.Builder builder;
    AlertDialog mDialog;
    private byte[] byteArray;
    private Bitmap bmp;
    ImageView imgScreenShotTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screenshot_taken_dialog);
        String dialogText = "dialog text";
        TextView txt = (TextView) findViewById(R.id.textView1);
        txt.setText(dialogText);

        imgScreenShotTaken =(ImageView)findViewById(R.id.img_screen_shot_taken);

        try {
             byteArray = getIntent().getByteArrayExtra("image");
             bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }catch (Exception e){e.printStackTrace();}

        try {
            imgScreenShotTaken.setImageBitmap(bmp);
        }catch (Exception d){d.printStackTrace();}


        Log.i("Dialog", "onCreate");

        Button dismissbutton = (Button) findViewById(R.id.button1);


        dismissbutton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Log.i("Dialog", "onClick");
                DialogScrensshotTakenActivity.this.finish();
            }
        });
    }
}

