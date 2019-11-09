package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;

public class service extends Service {
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
        int SECONDS = 1000;
        timer.schedule(new TimerTask() {
            @SuppressLint("HandlerLeak")
            private Handler updateUI = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    Toast.makeText(getApplicationContext(), taskName + " " +
                            getString(R.string.is_due), Toast.LENGTH_SHORT).show();
                }
            };

            @Override
            public void run() {
                updateUI.sendEmptyMessage(0);
                stopSelfResult(pid);
            }
        }, interval * SECONDS);
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
