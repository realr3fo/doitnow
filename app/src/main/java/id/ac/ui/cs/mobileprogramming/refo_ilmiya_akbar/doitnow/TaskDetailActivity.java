package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
    private TextView textViewTaskName, textViewTaskCategory, textViewTaskDesc, textViewTaskDueDate, textViewTaskAttachments;
    private CheckBox checkBoxFinished;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        setTitle("Task Detail");

        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewTaskName = findViewById(R.id.task_name);
        textViewTaskCategory = findViewById(R.id.task_category);
        textViewTaskDesc = findViewById(R.id.task_description);
        textViewTaskDueDate = findViewById(R.id.task_due_date);
        textViewTaskAttachments = findViewById(R.id.task_attachment);
        checkBoxFinished = findViewById(R.id.checkBoxFinished);

        this.task = (Task) getIntent().getSerializableExtra("task");

        assert this.task != null;
        loadTask(this.task);

        checkBoxFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskDetailActivity.this, "Check Box Clicked", Toast.LENGTH_SHORT).show();
                updateTask(task);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_task:
                // Update Task
                Toast.makeText(TaskDetailActivity.this, "Update Task", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpdateTaskActivity.class);
                intent.putExtra("task", this.task);
                startActivity(intent);
                return true;
            case R.id.delete_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetailActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(task);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadTask(Task task) {
        textViewTaskName.setText(task.getTask());
        textViewTaskCategory.setText("task's category");
        textViewTaskDesc.setText(task.getDesc());
        textViewTaskDueDate.setText(task.getFinishBy());
        textViewTaskAttachments.setText("task's attachments");
        checkBoxFinished.setChecked(task.isFinished());
    }

    private void updateTask(final Task task) {

        class UpdateTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                @SuppressLint("WrongThread") boolean isChecked = checkBoxFinished.isChecked();
                Log.d("ischecked", String.valueOf(isChecked));
                task.setFinished(isChecked);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(TaskDetailActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(TaskDetailActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}
