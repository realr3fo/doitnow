package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.GlobalApplication;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.AddTaskActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.TaskListActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.adapters.TasksAdapter;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.view_models.TaskListViewModel;

public class TaskListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TaskListViewModel taskListViewModel;

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskListViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TaskListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View taskListView = inflater.inflate(R.layout.fragment_task_list, container, false);
        setAdapter(taskListView);
        taskListViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                // Update the cached copy of the words in the adapter.
                List<Task> filteredTasks = new ArrayList<>();
                assert tasks != null;
                for (Task t : tasks) {
                    String userEmail = SharedPrefManager.getInstance(getActivity()).getUserEmail();
                    if (t.getUserMail().equalsIgnoreCase(userEmail)) {
                        filteredTasks.add(t);
                    }
                }
                adapter.setWords(filteredTasks);
            }
        });
        return taskListView;
    }

    private TasksAdapter adapter = null;

    private void setAdapter(View view) {
        recyclerView = view.findViewById(R.id.recyclerview_tasks);
        adapter = new TasksAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            ((TaskListActivity) getActivity()).setUpNavBar();


            FloatingActionButton buttonAddTask = view.findViewById(R.id.floating_button_add_task);
            buttonAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                    startActivity(intent);
                }
            });


            getActivity().getSupportFragmentManager().beginTransaction().replace(view.findViewById(R.id.container_first).getId(), new CategoryListFragment()).commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(view.findViewById(R.id.container_second).getId(), new CategoryInfoFragment()).commit();


            final SharedPreferences prefs = getActivity().getSharedPreferences("doitnowsharedpref", Context.MODE_PRIVATE);
            Button buttonFilterCategory = view.findViewById(R.id.filter_by_category);
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
            FloatingActionButton buttonAddCategory = view.findViewById(R.id.floating_button_add_category);
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
    }

    private void adjustLayoutWeights() {
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
    }


}
