package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
//        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_tasks, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.bind(t);

//        holder.textViewTask.setText(t.getTask());
//        holder.textViewFinishBy.setText(t.getFinishBy());
//
//        if (t.isFinished())
//            holder.textViewStatus.setText("Completed");
//        else
//            holder.textViewStatus.setText("Not Completed");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewDataBinding binding;
        TextView textViewStatus, textViewTask, textViewDesc, textViewFinishBy;

        public TasksViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewFinishBy = itemView.findViewById(R.id.textViewFinishBy);
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

            Intent intent = new Intent(mCtx, TaskDetailActivity.class);
            intent.putExtra("task", task);

            mCtx.startActivity(intent);
        }
    }
}