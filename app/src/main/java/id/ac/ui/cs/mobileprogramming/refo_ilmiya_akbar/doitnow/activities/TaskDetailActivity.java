package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;

import android.os.Bundle;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments.TaskDetailFragment;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TASK = "task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_fragment);
        TaskDetailFragment frag = (TaskDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.task_detail_frag);
        Task task = (Task) Objects.requireNonNull(getIntent().getExtras()).get(EXTRA_TASK);
        assert frag != null;
        frag.setTask(task);
    }

    public void setUpBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}