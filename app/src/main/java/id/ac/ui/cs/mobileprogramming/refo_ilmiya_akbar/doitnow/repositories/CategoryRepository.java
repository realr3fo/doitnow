package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos.CategoryDao;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Category;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.GlobalApplication;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> mAllCategories;
    public CategoryRepository(){
        Context context = GlobalApplication.getAppContext();
        categoryDao = DatabaseClient.getInstance(context).getAppDatabase().categoryDao();
        mAllCategories = categoryDao.getAllLiveCategories();
    }
    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }
    public void insert (Category category) {
        new insertAsyncTask(categoryDao).execute(category);
    }
    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao mAsyncTaskDao;
        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}