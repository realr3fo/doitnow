package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.TaskDetailActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.database_configs.DatabaseClient;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;

public class TaskDetailFragment extends Fragment {
    private Task task;
    private TextView textViewTaskName, textViewTaskCategory, textViewTaskDesc, textViewTaskDueDate, textViewTaskAttachments;
    private CheckBox checkBoxFinished;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            task = savedInstanceState.getParcelable("task");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            getActivity().setTitle("Task Detail");

            // Back Button
            if (getActivity() != null && getActivity() instanceof TaskDetailActivity) {

                ((TaskDetailActivity) getActivity()).setUpBackButton();
            }
            textViewTaskName = view.findViewById(R.id.task_name);
            textViewTaskCategory = view.findViewById(R.id.task_category);
            textViewTaskDesc = view.findViewById(R.id.task_description);
            textViewTaskDueDate = view.findViewById(R.id.task_due_date);
            textViewTaskAttachments = view.findViewById(R.id.task_attachment);
            checkBoxFinished = view.findViewById(R.id.checkBoxFinished);
            imageView = view.findViewById(R.id.task_detail_image_view);

            // load task detail data
            final Task task = this.task;


            loadTask(task);

            checkBoxFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTask(task);
                }
            });
            Button deleteButton = getActivity().findViewById(R.id.button_delete);
            if (deleteButton != null) {
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        }
    }

    private void loadTask(Task task) {
        textViewTaskName.setText(task.getTask());
        textViewTaskCategory.setText(task.getCategory());
        textViewTaskDesc.setText(task.getDesc());
        textViewTaskDueDate.setText(task.getFinishBy());
        String taskAttachmentFilePath = task.getFilePath();
        if (!taskAttachmentFilePath.equals("")) {
            String[] fileNameArr = taskAttachmentFilePath.split("/");
            String fileName = fileNameArr[fileNameArr.length - 1];
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
                task.setFinished(isChecked);
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (getActivity() != null && getActivity() instanceof TaskDetailActivity) {
                    getActivity().onBackPressed();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(TaskDetailFragment.this).commit();
                }
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("task", this.task);
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
