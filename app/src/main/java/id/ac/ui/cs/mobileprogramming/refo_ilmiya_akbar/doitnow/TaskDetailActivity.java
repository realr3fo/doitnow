package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
    private TextView textViewTaskName, textViewTaskCategory, textViewTaskDesc, textViewTaskDueDate, textViewTaskAttachments;
    private CheckBox checkBoxFinished;
    private ImageView imageView;
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
        imageView = findViewById(R.id.task_detail_image_view);

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
        Button deleteButton = findViewById(R.id.button_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
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
           }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void loadTask(Task task) {
        textViewTaskName.setText(task.getTask());
        textViewTaskCategory.setText(task.getCategory());
        textViewTaskDesc.setText(task.getDesc());
        textViewTaskDueDate.setText(task.getFinishBy());
        String taskAttachmentFilePath = task.getFilePath();
        if (!taskAttachmentFilePath.equals("")) {
            String[] fileNameArr = taskAttachmentFilePath.split("/");
            String fileName = fileNameArr[fileNameArr.length-1];
            Bitmap selectedImage = BitmapFactory.decodeFile(taskAttachmentFilePath);
            imageView.setImageBitmap(selectedImage);
            textViewTaskAttachments.setText(fileName);
        } else {
            textViewTaskAttachments.setText("None");
        }
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
