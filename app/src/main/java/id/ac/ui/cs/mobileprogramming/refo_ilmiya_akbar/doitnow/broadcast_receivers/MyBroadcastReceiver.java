package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.broadcast_receivers;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;

public class MyBroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case "android.intent.action.TIME_SET":
                Toast.makeText(context, R.string.time_changed_broadcast,
                        Toast.LENGTH_LONG).show();
                break;
            case "android.intent.action.TIMEZONE_CHANGED":
                Toast.makeText(context, R.string.timezone_changed_broadcast,
                        Toast.LENGTH_LONG).show();
                break;
            case "android.intent.action.DATE_CHANGED":
                Toast.makeText(context, R.string.date_changed_broadcast,
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}

