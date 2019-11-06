package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListFragment extends Fragment {
    private CategoryListViewModel categoryListViewModel;
    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryListViewModel = ViewModelProviders.of(getActivity()).get(CategoryListViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoryListView = inflater.inflate(R.layout.fragment_category_list, container, false);
        setAdapter(categoryListView);
        categoryListViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable final List<Category> categories) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(categories);
            }
        });
        return categoryListView;
    }
    CategoryListAdapter adapter = null;
    private void setAdapter(View view){
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.category_list);
        adapter = new CategoryListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}