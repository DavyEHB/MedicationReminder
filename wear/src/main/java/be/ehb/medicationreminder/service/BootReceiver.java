package be.ehb.medicationreminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by davy.van.belle on 23/06/2015.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BOOT_COMPLETED Received");
        Intent i = new Intent(context, StartService.class);
        context.startService(i);
    }
}
