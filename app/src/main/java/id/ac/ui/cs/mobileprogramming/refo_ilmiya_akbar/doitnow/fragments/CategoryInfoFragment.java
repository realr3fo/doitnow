package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.view_models.CategoryListViewModel;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities.Category;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.R;
import id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.SharedPrefManager;

public class CategoryInfoFragment extends Fragment implements View.OnClickListener {
    private CategoryListViewModel categoryListViewModel;

    public CategoryInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryListViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity())).get(CategoryListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoryInfoView = inflater.inflate(R.layout.fragment_category_info, container, false);
        initViews(categoryInfoView);
        return categoryInfoView;
    }

    private EditText categoryName;

    private void initViews(View view) {
        categoryName = view.findViewById(R.id.category_name);
        Button saveCategory = view.findViewById(R.id.save_info);
        saveCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_info) {
            saveInfo();
        }
    }

    private void saveInfo() {
        if (!TextUtils.isEmpty(categoryName.getText().toString())) {
            String name = categoryName.getText().toString();
            String userEmail = SharedPrefManager.getInstance(getContext()).getUserEmail();
            Category category = new Category();
            category.setName(name);
            category.setUserMail(userEmail);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String sTimestamp = String.valueOf(timestamp);
            category.setCreatedAt(sTimestamp);
            categoryListViewModel.insert(category);
            categoryName.setText("");
        } else {
            Toast.makeText(getActivity(), "Please fill in the required info", Toast.LENGTH_LONG).show();
        }
    }
}