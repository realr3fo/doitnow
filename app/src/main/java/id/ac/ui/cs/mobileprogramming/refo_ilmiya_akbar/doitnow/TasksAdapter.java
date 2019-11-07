package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Task> taskList;

    public TasksAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewDataBinding binding;

        public TasksViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            itemView.setOnClickListener(this);
        }

        public void bind(Object obj) {
            binding.setVariable(BR.task,obj);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());
            View fragmentContainer = ((TaskActivityFragment) mCtx).findViewById(R.id.fragment_container);
            if (fragmentContainer != null) {
                TaskDetailFragment details = new TaskDetailFragment();
                FragmentTransaction ft = ((TaskActivityFragment) mCtx).getSupportFragmentManager().beginTransaction();
                details.setTask(task);
                ft.replace(R.id.fragment_container, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
            else {
                Intent intent = new Intent(mCtx, TaskDetailActivityFragment.class);
                intent.putExtra(TaskDetailActivityFragment.EXTRA_TASK, (Parcelable) task);
                mCtx.startActivity(intent);
            }
        }
    }
}