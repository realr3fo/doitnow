package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivityFragment extends AppCompatActivity {
    public static final String EXTRA_TASK = "task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_fragment);
        TaskDetailFragment frag = (TaskDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.task_detail_frag);
        Task task = (Task) getIntent().getExtras().get(EXTRA_TASK);
        frag.setTask(task);
    }

    public void setUpBackButton() {
        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}