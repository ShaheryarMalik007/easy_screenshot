package snx.easyscreenshot.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import snx.easyscreenshot.View.Activities.DialogScrensshotTakenActivity;

public class AlertDialogReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ct, Intent intent) {
        Intent alarmIntent = new Intent("android.intent.action.MAIN");
        alarmIntent.setClass(ct, DialogScrensshotTakenActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start the popup activity

        ct.startActivity(alarmIntent);
    }
}
