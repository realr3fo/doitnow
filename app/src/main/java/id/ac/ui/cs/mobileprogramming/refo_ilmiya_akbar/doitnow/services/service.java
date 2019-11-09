package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class service extends Service {
    private static final String TAG = "service";
    private int SECONDS = 1000;
    private Timer timer = new Timer();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int pid = startId;
        final long interval = intent.getLongExtra("interval", -1);
        if (interval <= 0) {
            stopSelfResult(pid);
            return START_STICKY;
        }
        final String taskName = intent.getStringExtra("taskName");
        // Execute an action after period time
        timer.schedule(new TimerTask() {
            private Handler updateUI = new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    Toast.makeText(getApplicationContext(), taskName + " is due!", Toast.LENGTH_SHORT).show();
                }
            };
            @Override
            public void run() {
                // Print a log
                String str = "interval " + interval;
                // PUSH NOTIFICATION
                updateUI.sendEmptyMessage(0);
                stopSelfResult(pid);
            }
        }, interval*SECONDS);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();

    }
}
