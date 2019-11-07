package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}
