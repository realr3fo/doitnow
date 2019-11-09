package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos.CategoryDao;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos.TaskDao;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.daos.UserDao;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Category;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.User;

@Database(entities = {Task.class, User.class, Category.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
}