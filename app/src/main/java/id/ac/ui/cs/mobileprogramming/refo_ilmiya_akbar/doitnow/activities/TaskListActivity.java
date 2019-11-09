package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;

public class TaskListActivity extends AppCompatActivity {
    private ActionBarDrawerToggle t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.task_list_title);
        setContentView(R.layout.activity_task_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void setUpNavBar() {
        DrawerLayout dl = findViewById(R.id.activity_todo);
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
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.todolist:
                        Toast.makeText(TaskListActivity.this, R.string.already_in_task_list,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });
    }

}
