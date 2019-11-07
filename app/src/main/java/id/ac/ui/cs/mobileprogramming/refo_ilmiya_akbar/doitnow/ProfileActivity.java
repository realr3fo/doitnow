package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }



        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewGender = (TextView) findViewById(R.id.textViewGender);

        //getting the current user
        String userEmail = SharedPrefManager.getInstance(this).getUserEmail();
        getUser(userEmail);

        //when the user presses logout button
        //calling the logout method
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
        final String userEmailFinal = userEmail;
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
                    if (users.get(i).getEmail().equals(userEmailFinal)) {
                        user = users.get(i);
                    }
                }
                if (user.getId() == 0) {
                    throw new Error("User not found");
                }
                //setting the values to the textviews
                textViewId.setText(String.valueOf(user.getId()));
                textViewUsername.setText(user.getUsername());
                textViewEmail.setText(user.getEmail());
                textViewGender.setText(user.getGender());
                Toast.makeText(ProfileActivity.this, "Success Get Users", Toast.LENGTH_SHORT).show();
            }
        }

        GetUsers gu = new GetUsers();
        gu.execute();
    }

    public void setUpNavbar() {
        dl = (DrawerLayout) findViewById(R.id.activity_profile);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.profile:
                        Toast.makeText(ProfileActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.todolist:
                        Toast.makeText(ProfileActivity.this, "Todo List", Toast.LENGTH_SHORT).show();
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), TaskActivityFragment.class));
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
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
