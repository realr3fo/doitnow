package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User task);

    @Delete
    void delete(User task);

    @Update
    void update(User task);
}
