package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Update;

public class UpdateTaskActivity extends AppCompatActivity {

    private final static int IMAGE_RESULT = 200;

    private EditText editTextTask, editTextDesc, editTextDate, editTextTime;
    TextView textFileName;
    String attachmentFilePath;
    ImageView imageView;

    Switch switchReminder;
    Spinner spinnerCategory;
    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;
    EditText text_date, text_hour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
        setTitle("Update Your Task");

        // Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        switchReminder = findViewById(R.id.reminder);
        spinnerCategory = findViewById(R.id.spinner_categories);
        textFileName = findViewById(R.id.task_image_file_name);
        imageView = findViewById(R.id.task_image_view);
        attachmentFilePath = "";



        final Task task = (Task) getIntent().getSerializableExtra("task");

        getCategories(task.getCategory());
        loadTask(task);
        setUpDueTime();
        setUpAttachment();

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                updateTask(task);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
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

    private void loadTask(Task task) {
        editTextTask.setText(task.getTask());

        editTextDesc.setText(task.getDesc());

        String dueDate = task.getFinishBy();
        String[] splitDueDate = dueDate.split("@");
        String date = splitDueDate[0].trim();
        String time = splitDueDate[1].trim();
        editTextDate.setText(date);
        editTextTime.setText(time);

        String taskAttachmentFilePath = task.getFilePath();
        if (!taskAttachmentFilePath.equals("")) {
            String[] fileNameArr = taskAttachmentFilePath.split("/");
            String fileName = fileNameArr[fileNameArr.length-1];
            attachmentFilePath = taskAttachmentFilePath;
            Bitmap selectedImage = BitmapFactory.decodeFile(taskAttachmentFilePath);
            imageView.setImageBitmap(selectedImage);
            textFileName.setText(fileName);
        }

        boolean reminder = task.isReminder();
        switchReminder.setChecked(reminder);
    }

    private void getCategories(String category) {
        final String selectedCategory = category;
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
                for (int i = 0; i < categories.size(); i++) {
                    listSpinner.add(categories.get(i).getName());
                }
                String[] arraySpinner = new String[listSpinner.size()];
                listSpinner.toArray(arraySpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateTaskActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
                selectCategory(listSpinner, selectedCategory);
                Toast.makeText(UpdateTaskActivity.this, "Success Get Categories", Toast.LENGTH_SHORT).show();
            }

            private void selectCategory(List<String> categoriesNames, String selectedCategory) {
                int index = 0;
                for (int i = 0; i < categoriesNames.size(); i++) {
                    if (categoriesNames.get(i).equalsIgnoreCase(selectedCategory)) {
                        index = i;
                        break;
                    }
                }
                spinnerCategory.setSelection(index);

            }
        }

        GetCategories gu = new GetCategories();
        gu.execute();
    }

    public void setUpAttachment() {
        FloatingActionButton fab = findViewById(R.id.floating_button_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.task_image_view);

            if (requestCode == IMAGE_RESULT) {

                String filePath = getImageFilePath(data);
                attachmentFilePath = filePath;
                String[] filenameSplit = filePath.split("/");
                String filename = filenameSplit[filenameSplit.length - 1];
                textFileName.setText(filename);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(selectedImage);
                }
            }
        }
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "capture.png"));
        }
        return outputFileUri;
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTaskActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        text_hour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String sDate = editTextDate.getText().toString().trim();
                if (sDate.isEmpty()) {
                    editTextDate.setError("Date required");
                    editTextDate.requestFocus();
                    return;
                }
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);
                        // check if its current date, the time is free
                        boolean dateAfterToday = false;
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date dueDate = formatter.parse(sDate);
                            Date today = datetime.getTime();
                            if (dueDate.after(today)) {
                                dateAfterToday = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (!dateAfterToday) {
                            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                //it's after current
                                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                                text_hour.setText(time);
                            } else {
                                //it's before current'
                                Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                            text_hour.setText(time);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        text_date.setText(sdf.format(myCalendar.getTime()));
    }


    private void updateTask(final Task task) {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sDate = editTextDate.getText().toString().trim();
        final String sTime = editTextTime.getText().toString().trim();
        final String sAttachmentFilePath = attachmentFilePath;
        final String sDueDate = sDate + " @ " + sTime;
        final boolean reminderBool = switchReminder.isChecked();


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
        if (reminderBool) {
            setReminder(sTask, sDueDate);
        }

        class UpdateTask extends AsyncTask<Void, Void, Void> {


            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sDueDate);
                task.setFinished(false);
                task.setFilePath(sAttachmentFilePath);
                task.setCategory(spinnerCategory.getSelectedItem().toString());
                task.setUserID(SharedPrefManager.getInstance(UpdateTaskActivity.this).getUserID());
                task.setReminder(reminderBool);

                //adding to database
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
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void setReminder(String sTaskName, String sDueDate) {
        Intent startServiceIntent = new Intent(UpdateTaskActivity.this, service.class);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd @ HH:mm");
        long seconds = 1;
        try {
            Date dueDate = formatter.parse(sDueDate);
            Date currentTime = Calendar.getInstance().getTime();
            seconds = (dueDate.getTime() - currentTime.getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startServiceIntent.putExtra("interval", seconds);
        startServiceIntent.putExtra("taskName", sTaskName);
        startService(startServiceIntent);
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
                startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}