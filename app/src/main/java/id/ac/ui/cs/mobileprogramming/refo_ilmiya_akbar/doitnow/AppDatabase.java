package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class, User.class, Category.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
}