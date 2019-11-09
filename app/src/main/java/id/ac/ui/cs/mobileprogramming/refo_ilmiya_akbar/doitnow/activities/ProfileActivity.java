package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.User;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender;
    private ActionBarDrawerToggle t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        textViewId = findViewById(R.id.textViewId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewGender = findViewById(R.id.textViewGender);

        Button spanishLanguage = findViewById(R.id.buttonSpanish);
        spanishLanguage.setOnClickListener(new View.OnClickListener() {
            private final String language_code = "es";

            @Override
            public void onClick(View v) {
                Resources res = ProfileActivity.this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(language_code.toLowerCase()));
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(getIntent());
            }
        });

        Button englishLanguage = findViewById(R.id.buttonEnglish);
        englishLanguage.setOnClickListener(new View.OnClickListener() {
            private final String language_code = "en";

            @Override
            public void onClick(View v) {
                Resources res = ProfileActivity.this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(language_code.toLowerCase()));
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(getIntent());
            }
        });

        String userEmail = SharedPrefManager.getInstance(this).getUserEmail();
        getUser(userEmail);

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });

        setUpNavbar();
    }

    private void getUser(final String userEmail) {
        class GetUsers extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .userDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                User user = new User(0, "", "", "", "");
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getEmail().equals(userEmail)) {
                        user = users.get(i);
                    }
                }
                if (user.getId() == 0) {
                    throw new Error("User not found");
                }
                textViewId.setText(String.valueOf(user.getId()));
                textViewUsername.setText(user.getUsername());
                textViewEmail.setText(user.getEmail());
                textViewGender.setText(user.getGender());
            }
        }

        GetUsers gu = new GetUsers();
        gu.execute();
    }

    public void setUpNavbar() {
        DrawerLayout dl = findViewById(R.id.activity_profile);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.profile:
                        Toast.makeText(ProfileActivity.this, R.string.already_in_profile,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.todolist:
                        finish();
                        startActivity(new Intent(getApplicationContext(), TaskListActivity.class));
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
