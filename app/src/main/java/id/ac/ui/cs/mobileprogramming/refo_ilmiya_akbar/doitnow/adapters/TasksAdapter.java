package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.adapters;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.TaskDetailActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.activities.TaskListActivity;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Task;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments.TaskDetailFragment;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewDataBinding binding;

        TasksViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            itemView.setOnClickListener(this);
        }

        void bind(Object obj) {
            binding.setVariable(id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.BR.task, obj);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());
            View fragmentContainer = ((TaskListActivity) mCtx).findViewById(R.id.fragment_container);
            if (fragmentContainer != null) {
                TaskDetailFragment details = new TaskDetailFragment();
                FragmentTransaction ft = ((TaskListActivity) mCtx)
                        .getSupportFragmentManager().beginTransaction();
                details.setTask(task);
                ft.replace(R.id.fragment_container, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                Intent intent = new Intent(mCtx, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.EXTRA_TASK, (Parcelable) task);
                mCtx.startActivity(intent);
            }
        }
    }

    private Context mCtx;
    private List<Task> taskList;

    public TasksAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.bind(t);
    }

    public void setWords(List<Task> tasks) {
        taskList = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (taskList != null) {
            return taskList.size();
        } else {
            return 0;
        }
    }

}