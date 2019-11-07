package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Category;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameView;
        private final ViewDataBinding binding;

        private CategoryViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            categoryNameView = itemView.findViewById(R.id.category_name);
            this.binding = binding;
        }

        void bind(Object obj) {
            binding.setVariable(id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.BR.category, obj);
            binding.executePendingBindings();
        }
    }

    private List<Category> mCategories;


    public CategoryListAdapter(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.adapter_category_item, parent, false);
        return new CategoryViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (mCategories != null) {
            Category current = mCategories.get(position);
            holder.bind(current);
        } else {
            holder.categoryNameView.setText("No Word");
        }
    }

    public void setWords(List<Category> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCategories != null) {
            return mCategories.size();
        } else {
            return 0;
        }
    }
}