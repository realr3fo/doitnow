package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.repositories;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.GlobalApplication;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos.TaskDao;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> mAllTasks;

    public TaskRepository() {
        Context context = GlobalApplication.getAppContext();
        taskDao = DatabaseClient.getInstance(context).getAppDatabase().taskDao();
        mAllTasks = taskDao.getAllLiveTask();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        new TaskRepository.insertAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new TaskRepository.updateAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new TaskRepository.deleteAsyncTask(taskDao).execute(task);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        updateAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        deleteAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}