package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.view_models;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.repositories.TaskRepository;

public class TaskListViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> mAllTasks;

    public TaskListViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository();
        mAllTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }
}
