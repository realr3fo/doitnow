package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.os.Build;
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
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Category;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.services.service;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class AddTaskActivity extends AppCompatActivity {
    private final static int IMAGE_RESULT = 200;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private String fileName = "";

    private EditText editTextTask, editTextDesc, editTextDate, editTextTime;
    TextView textFileName;
    String attachmentFilePath;

    Switch switchReminder;
    Spinner spinnerCategory;
    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;
    EditText text_date, text_hour;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle(R.string.task_title);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        switchReminder = findViewById(R.id.reminder);
        spinnerCategory = findViewById(R.id.spinner_categories);
        textFileName = findViewById(R.id.task_image_file_name);
        attachmentFilePath = "";

        getCategories();
        setUpDueTime();
        setUpAttachment();

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest
                        .toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
            }
        }
        return false;
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.permission_mandatory))
                .setPositiveButton(getString(R.string.okay), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }


    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perms : permissionsToRequest) {
                if (hasPermission(perms)) {
                    permissionsRejected.add(perms);
                }
            }
            if (permissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsRejected
                                                .toArray(new String[permissionsRejected.size()]),
                                        ALL_PERMISSIONS_RESULT);
                            }
                        });
                    }
                }

            }
        }

    }


    public void setUpAttachment() {
        FloatingActionButton fab = findViewById(R.id.floating_button_add_task);
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
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
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
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (Objects.requireNonNull(intent.getComponent()).getClassName()
                    .equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, getString(R.string.select_source));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents
                .toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.task_image_view);

            if (requestCode == IMAGE_RESULT) {

                String filePath = getImageFilePath(data);
                textFileName.setText(String.format("%s.png", this.fileName));
                attachmentFilePath = filePath;
                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                imageView.setImageBitmap(selectedImage);
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
        if (this.fileName.equals("")) {
            long tsLong = System.currentTimeMillis() / 1000;
            this.fileName = String.valueOf(tsLong);
        }
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), this.fileName + ".png"));

        }
        return outputFileUri;
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = Objects.requireNonNull(cursor).
                getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void setUpDueTime() {
        text_date = findViewById(R.id.editTextDate);
        text_hour = findViewById(R.id.editTextTime);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (AddTaskActivity.this, date, myCalendar
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
                    editTextDate.setError(getString(R.string.date_required));
                    editTextDate.requestFocus();
                    return;
                }
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                                  int selectedMinute) {
                                Calendar datetime = Calendar.getInstance();
                                Calendar c = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                                datetime.set(Calendar.MINUTE, selectedMinute);
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
                                    if (datetime.getTimeInMillis() > c.getTimeInMillis()) {
                                        //it's after current
                                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                                        text_hour.setText(time);
                                    } else {
                                        //it's before current'
                                        Toast.makeText(getApplicationContext(), "Invalid Time",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                                    text_hour.setText(time);
                                }
                            }
                        }, hour, minute, true);

                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();
            }
        });
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
                List<String> listSpinner = new ArrayList<>();
                listSpinner.add(getString(R.string.default_cat));
                for (int i = 0; i < categories.size(); i++) {
                    listSpinner.add(categories.get(i).getName());
                }
                String[] arraySpinner = new String[listSpinner.size()];
                listSpinner.toArray(arraySpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTaskActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
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
        final String sAttachmentFilePath = attachmentFilePath;
        final String sDueDate = sDate + " @ " + sTime;
        final boolean reminderBool = switchReminder.isChecked();


        if (sTask.isEmpty()) {
            editTextTask.setError(getString(R.string.task_required));
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError(getString(R.string.desc_required));
            editTextDesc.requestFocus();
            return;
        }

        if (sDate.isEmpty()) {
            editTextDate.setError(getString(R.string.date_required));
            editTextDate.requestFocus();
            return;
        }

        if (sTime.isEmpty()) {
            editTextTime.setError(getString(R.string.time_required));
            editTextTime.requestFocus();
            return;
        }

        if (reminderBool) {
            setReminder(sTask, sDueDate);
        }


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sDueDate);
                task.setFinished(false);
                task.setFilePath(sAttachmentFilePath);
                task.setCategory(spinnerCategory.getSelectedItem().toString());
                task.setUserMail(SharedPrefManager.getInstance(AddTaskActivity.this).getUserEmail());
                task.setReminder(reminderBool);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void setReminder(String sTaskName, String sDueDate) {
        Intent startServiceIntent = new Intent(AddTaskActivity.this, service.class);
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

}