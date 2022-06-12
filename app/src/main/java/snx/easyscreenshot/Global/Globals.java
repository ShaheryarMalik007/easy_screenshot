package snx.easyscreenshot.Global;


import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.marcinorlowski.fonty.Fonty;

import java.util.TimeZone;

//import com.crashlytics.android.Crashlytics;
//import com.google.gson.Gson;
//import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//
//import java.io.File;
//import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;
//
//import ifisol.attendo.Model.Organisations;
//import ifisol.attendo.Model.User;
//import ifisol.attendo.R;
//import io.fabric.sdk.android.Fabric;
//import okhttp3.OkHttpClient;

/**
 * Created by Shaheryar Malik
 */
public class Globals extends Application
{

    public static SharedPreferences mPreference;
    public static SharedPreferences.Editor mEditor;
//    public static OkHttpClient okHttpClient ;
//    public User mActiveUser;
//    public Organisations mTempOrganisation;
    public static Dialog mLoadingDia;
    public static Dialog mConnectionDia;
    public static String timezone;
    public boolean isCreatEvent;

    @Override
    public void onCreate() {
        super.onCreate();


        mPreference = getSharedPreferences("SWIPESHOT_PREFERENCES", Context.MODE_PRIVATE);
        mEditor = mPreference.edit();


        timezone = TimeZone.getDefault().getID();
    }

//

    public void SaveFile(String file) {
        mEditor.putString("FILE",file);
        mEditor.commit();
    }


    public String GetFile() {
        String loc = mPreference.getString("FILE",null);
        return loc;
    }



    /*public Events GetEvent()
    {
        String ObjString=mPreference.getString("EVENT", null);
        Gson gson=new Gson();
        Events event=gson.fromJson(ObjString, Events.class);
        return event;
    }*/


    public void saveDemoState(boolean isBool) {
        mEditor.putBoolean("BOOL", isBool);
        mEditor.commit();
    }


    public boolean getDemoState() {
        boolean bool =mPreference.getBoolean("BOOL", false);
        return bool;
    }
}
