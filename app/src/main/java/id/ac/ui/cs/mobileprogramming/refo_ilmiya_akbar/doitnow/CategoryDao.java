package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Query("SELECT * from category ORDER BY createdAt ASC")
    LiveData<List<Category>> getAllLiveCategories();

    @Insert
    void insert(Category category);

    @Delete
    void delete(Category category);

    @Update
    void update(Category category);
}
