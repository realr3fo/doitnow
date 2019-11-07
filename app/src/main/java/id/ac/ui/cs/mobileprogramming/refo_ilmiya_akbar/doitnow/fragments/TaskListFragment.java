package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.AddTaskActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.TaskListActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.GlobalApplication;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.adapters.TasksAdapter;

public class TaskListFragment extends Fragment {
    public static interface Listener {
        void itemClicked(long id);
    }

    ;

    private Listener listener;
    private FloatingActionButton buttonAddTask, buttonAddCategory;
    private Button buttonFilterCategory;
    private RecyclerView recyclerView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_task_list, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ((TaskListActivity) getActivity()).setUpNavbar();

            recyclerView = view.findViewById(R.id.recyclerview_tasks);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            buttonAddTask = view.findViewById(R.id.floating_button_add_task);
            buttonAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                    startActivity(intent);
                }
            });

            getTasks();


            getActivity().getSupportFragmentManager().beginTransaction().replace(view.findViewById(R.id.container_first).getId(), new CategoryListFragment()).commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(view.findViewById(R.id.container_second).getId(), new CategoryInfoFragment()).commit();


            final SharedPreferences prefs = getActivity().getSharedPreferences("doitnowsharedpref", Context.MODE_PRIVATE);
            buttonFilterCategory = view.findViewById(R.id.filter_by_category);
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
            buttonAddCategory = view.findViewById(R.id.floating_button_add_category);
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
            Log.d("taskListFragment", "hello world");
            // load task list data
        }
    }

    public void adjustLayoutWeights() {
        View view = getView();
        SharedPreferences prefs = getActivity().getSharedPreferences("doitnowsharedpref", Context.MODE_PRIVATE);
        boolean btnFilterCatState = prefs.getBoolean("btnFilterCat", false);
        if (btnFilterCatState) {
            (view.findViewById(R.id.filter_by_category)).setBackgroundColor(getResources().getColor(R.color.colorRed));
        } else {
            (view.findViewById(R.id.filter_by_category)).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        boolean btnAddCatState = prefs.getBoolean("btnAddCat", false);
        if (btnAddCatState) {
            (view.findViewById(R.id.floating_button_add_category))
                    .setBackgroundTintList(ContextCompat.getColorStateList(GlobalApplication.getAppContext(), R.color.colorRed));
        } else {
            (view.findViewById(R.id.floating_button_add_category))
                    .setBackgroundTintList(ContextCompat.getColorStateList(GlobalApplication.getAppContext(), R.color.colorPrimaryDark));
            ;
        }
        RecyclerView taskList = view.findViewById(R.id.recyclerview_tasks);
        FrameLayout containerSecond = view.findViewById(R.id.container_second);
        FrameLayout containerFirst = view.findViewById(R.id.container_first);
        int taskListWeight = 100;
        int containerSecondWeight = 0;
        int containerFirstWeight = 0;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (btnFilterCatState && btnAddCatState) {
                taskListWeight = 60;
                containerSecondWeight = 25;
                containerFirstWeight = 15;
            } else if (btnFilterCatState) {
                taskListWeight = 75;
                containerFirstWeight = 25;
            } else if (btnAddCatState) {
                taskListWeight = 85;
                containerSecondWeight = 15;
            }
        } else {
            if (btnFilterCatState && btnAddCatState) {
                taskListWeight = 45;
                containerFirstWeight = 25;
                containerSecondWeight = 30;
            } else if (btnFilterCatState) {
                taskListWeight = 75;
                containerFirstWeight = 25;
            } else if (btnAddCatState) {
                taskListWeight = 70;
                containerSecondWeight = 30;
            }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(GlobalApplication.getAppContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(getActivity(), tasks);
                recyclerView.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


//    @Override
//    public void onListItemClick(ListView listView, View itemView, int position, long id) {
//        if (listener != null) {
//            listener.itemClicked(id);
//        }
//    }
}
