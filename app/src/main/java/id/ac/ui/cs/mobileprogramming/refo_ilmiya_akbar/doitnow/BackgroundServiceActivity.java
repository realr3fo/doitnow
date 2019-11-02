package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BackgroundServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_service);

        setTitle("dev2qa.com - Android Background Service Example.");

        Button startBackService = (Button)findViewById(R.id.start_background_service_button);
        startBackService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start android service.
                Intent startServiceIntent = new Intent(BackgroundServiceActivity.this, service.class);
                EditText editText = findViewById(R.id.editTextBackgroundTime);
                String datetime = editText.getText().toString();
                Log.d("datetime", datetime);
                int seconds = 1;
                startServiceIntent.putExtra("interval", seconds);
                startService(startServiceIntent);
            }
        });

        Button startBackService2 = (Button)findViewById(R.id.start_background_service_button_2);
        startBackService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start android service.
                Intent startServiceIntent = new Intent(BackgroundServiceActivity.this, service.class);
                EditText editText = findViewById(R.id.editTextBackgroundTime);
                String datetime = editText.getText().toString();
                Log.d("datetime", datetime);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd @ HH:mm");

                try {
                    Date date6 = formatter.parse(datetime);
                    Date currentTime = Calendar.getInstance().getTime();
                    Log.d("datetime", date6.toString());
                    Log.d("datetime", currentTime.toString());

                    long seconds = (date6.getTime()-currentTime.getTime())/1000;
                    Log.d("datetime", String.valueOf(seconds));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.d("datetime", datetime);
                int seconds = 10;
                startServiceIntent.putExtra("interval", seconds);
                startService(startServiceIntent);
            }
        });


        Button stopBackService = (Button)findViewById(R.id.stop_background_service_button);
        stopBackService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop android service.
                Intent stopServiceIntent = new Intent(BackgroundServiceActivity.this, service.class);
                stopService(stopServiceIntent);
            }
        });

        Button stopBackService2 = (Button)findViewById(R.id.stop_background_service_button_2);
        stopBackService2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop android service.
                Intent stopServiceIntent = new Intent(BackgroundServiceActivity.this, service.class);
                stopService(stopServiceIntent);
            }
        });
    }
}