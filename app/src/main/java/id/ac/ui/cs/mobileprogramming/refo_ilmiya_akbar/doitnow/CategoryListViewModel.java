package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CategoryListViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> mAllCategories;
    public CategoryListViewModel(Application application){
        super(application);
        categoryRepository = new CategoryRepository(application);
        mAllCategories = categoryRepository.getAllCategories();
    }
    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }
    public void insert(Category category) {
        categoryRepository.insert(category);
    }
}