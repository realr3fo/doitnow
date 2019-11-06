package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameView;
        private final ViewDataBinding binding;

        private CategoryViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            categoryNameView = itemView.findViewById(R.id.category_name);
            this.binding = binding;
        }
        public void bind(Object obj) {
            binding.setVariable(BR.category,obj);
            binding.executePendingBindings();
        }
    }

    private final LayoutInflater mInflater;
    private List<Category> mCategories; // Cached copy of users


    CategoryListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_category_item, parent, false);
        return new CategoryViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (mCategories!= null) {
            Category current = mCategories.get(position);
            holder.bind(current);
        } else {
            holder.categoryNameView.setText("No Word");
        }
    }
    void setWords(List<Category> categories){
        mCategories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCategories != null) {
            return mCategories.size();
        }else{
            return  0;
        }
    }
}