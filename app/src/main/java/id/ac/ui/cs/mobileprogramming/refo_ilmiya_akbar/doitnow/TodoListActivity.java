package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


//import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private FloatingActionButton buttonAddTask, buttonAddCategory;
    private Button buttonFilterCategory;
    private RecyclerView recyclerView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        setTitle("Your Tasks");

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTask = findViewById(R.id.floating_button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoListActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        setUpNavbar();
        getTasks();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_first, new CategoryListFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_second, new CategoryInfoFragment()).commit();
        final SharedPreferences prefs = getSharedPreferences("doitnowsharedpref", Context.MODE_PRIVATE);
        buttonFilterCategory = findViewById(R.id.filter_by_category);
        buttonFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean btnFilterCatState = prefs.getBoolean("btnFilterCat", false);
                if (!btnFilterCatState) {
                    prefs.edit().putBoolean("btnFilterCat", true).apply();
                } else {
                    prefs.edit().putBoolean("btnFilterCat", false).apply();
                }
                adjustLayoutWeights();
            }
        });
        buttonAddCategory = findViewById(R.id.floating_button_add_category);
        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean btnAddCatState = prefs.getBoolean("btnAddCat", false);
                if (!btnAddCatState) {
                    prefs.edit().putBoolean("btnAddCat", true).apply();
                } else {
                    prefs.edit().putBoolean("btnAddCat", false).apply();
                }
                adjustLayoutWeights();
            }
        });
        adjustLayoutWeights();
    }

    public void adjustLayoutWeights() {
        SharedPreferences prefs = getSharedPreferences("doitnowsharedpref", Context.MODE_PRIVATE);
        boolean btnFilterCatState = prefs.getBoolean("btnFilterCat", false);
        if (btnFilterCatState) {
            (findViewById(R.id.filter_by_category)).setBackgroundColor(getResources().getColor(R.color.colorRed));
        } else {
            (findViewById(R.id.filter_by_category)).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        boolean btnAddCatState = prefs.getBoolean("btnAddCat", false);
        if (btnAddCatState) {
            (findViewById(R.id.floating_button_add_category))
                    .setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorRed));
        } else {
            (findViewById(R.id.floating_button_add_category))
                    .setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));;
        }
        RecyclerView taskList = findViewById(R.id.recyclerview_tasks);
        FrameLayout containerSecond = findViewById(R.id.container_second);
        FrameLayout containerFirst= findViewById(R.id.container_first);
        int taskListWeight = 100;
        int containerSecondWeight = 0;
        int containerFirstWeight = 0;
        if (btnFilterCatState && btnAddCatState) {
            taskListWeight = 60;
            containerSecondWeight = 15;
            containerFirstWeight = 25;
        } else if (btnFilterCatState) {
            taskListWeight = 75;
            containerFirstWeight = 25;
        } else if (btnAddCatState) {
            taskListWeight = 85;
            containerSecondWeight = 15;
        }

        LinearLayout.LayoutParams taskListParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                taskListWeight
        );
        LinearLayout.LayoutParams containerSecondParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                containerSecondWeight
        );
        LinearLayout.LayoutParams containerFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                containerFirstWeight
        );
        taskList.setLayoutParams(taskListParams);
        containerSecond.setLayoutParams(containerSecondParams);
        containerFirst.setLayoutParams(containerFirstParams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void setUpNavbar() {
        dl = (DrawerLayout) findViewById(R.id.activity_todo);
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
                        Toast.makeText(TodoListActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.todolist:
                        Toast.makeText(TodoListActivity.this, "To Do List", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
    }


    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(TodoListActivity.this, tasks);
                recyclerView.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


}