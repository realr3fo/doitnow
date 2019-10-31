package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTask, editTextDesc, editTextDate, editTextTime;
    Switch switchReminder;
    Spinner spinnerCategory;
    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;
    EditText text_date, text_hour;

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
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        switchReminder = findViewById(R.id.reminder);
        spinnerCategory = findViewById(R.id.spinner_categories);


        setUpDueTime();

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    public void setUpDueTime() {
        text_date = (EditText) findViewById(R.id.editTextDate);
        text_hour = (EditText) findViewById(R.id.editTextTime);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        text_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        text_hour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);
                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                            //it's after current
                            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                            text_hour.setText(time);
                        } else {
                            //it's before current'
                            Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

//        btn_get_datetime.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(AddTaskActivity.this,
//                        "Tanggal : " + txt_tgl.getText().toString() + "\n" +
//                                "Jam : " + txt_jam.getText().toString()
//                        , Toast.LENGTH_SHORT
//                ).show();
//            }
//        });
    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        text_date.setText(sdf.format(myCalendar.getTime()));
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
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("default");
                for (int i= 0; i < categories.size(); i++) {
                    listSpinner.add(categories.get(i).getName());
                }
                String[] arraySpinner = new String[ listSpinner.size() ];
                listSpinner.toArray( arraySpinner );
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTaskActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
                Toast.makeText(AddTaskActivity.this, "Success Get Categories", Toast.LENGTH_SHORT).show();
            }
        }

        GetCategories gu = new GetCategories();
        gu.execute();
    }

    private void saveTask() {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sDate = editTextDate.getText().toString().trim();
        final String sTime = editTextTime.getText().toString().trim();
        final String sDueDate = sDate + " @ " + sTime;
        final boolean reminderBool = switchReminder.isChecked();
        Log.d("tasks", String.valueOf(reminderBool));


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

        if (sDate.isEmpty()) {
            editTextDate.setError("Date required");
            editTextDate.requestFocus();
            return;
        }

        if (sTime.isEmpty()) {
            editTextTime.setError("Time required");
            editTextTime.requestFocus();
            return;
        }


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sDueDate);
                task.setFinished(false);
                task.setCategory(spinnerCategory.getSelectedItem().toString());
                task.setUserID(SharedPrefManager.getInstance(AddTaskActivity.this).getUserID());

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