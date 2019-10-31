package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class MyBroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case "android.intent.action.TIME_SET":
                Toast.makeText(context, "You have changed your time, please check your tasks due time",
                        Toast.LENGTH_LONG).show();
                break;
            case "android.intent.action.TIMEZONE_CHANGED":
                Toast.makeText(context, "You have changed your timezone, please check your tasks due time",
                        Toast.LENGTH_LONG).show();
                break;
            case "android.intent.action.DATE_CHANGED":
                Toast.makeText(context, "You have changed your date, please check your tasks due time",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}

