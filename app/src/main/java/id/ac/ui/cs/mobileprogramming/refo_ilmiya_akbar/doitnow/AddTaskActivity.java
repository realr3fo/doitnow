package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc, editTextFinishBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle("Create Your Task");

        getCategories();


        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getCategories() {
        class GetCategories extends AsyncTask<Void, Void, List<Category>> {

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .categoryDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<Category> categories) {
                super.onPostExecute(categories);
                for (int i= 0; i < categories.size(); i++) {
                    Log.d("categories", categories.get(i).getName());
                }
                Toast.makeText(AddTaskActivity.this, "Success Get Categories", Toast.LENGTH_SHORT).show();
            }
        }

        GetCategories gu = new GetCategories();
        gu.execute();
    }

    private void saveTask() {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();

        if (sTask.isEmpty()) {
            editTextTask.setError("Task required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        if (sFinishBy.isEmpty()) {
            editTextFinishBy.setError("Finish by required");
            editTextFinishBy.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);
                task.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), TodoListActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

}