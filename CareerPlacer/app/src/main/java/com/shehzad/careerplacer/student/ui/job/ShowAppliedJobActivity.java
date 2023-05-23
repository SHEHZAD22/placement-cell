package com.shehzad.careerplacer.student.ui.job;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shehzad.careerplacer.databinding.ActivityShowAppliedJobBinding;
import com.shehzad.careerplacer.student.ui.job.adapter.AppliedJobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;

import java.util.Objects;

public class ShowAppliedJobActivity extends AppCompatActivity {

    ActivityShowAppliedJobBinding binding;
    private AppliedJobAdapter appliedJobAdapter;
    private AppliedJobViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityShowAppliedJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Applied Jobs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.appliedJobRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.appliedJobRecView.setHasFixedSize(true);
        appliedJobAdapter = new AppliedJobAdapter();
        binding.appliedJobRecView.setAdapter(appliedJobAdapter);

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        fetchData();
    }

    private void fetchData() {
        viewModel.getAllJob().observe(this, list1 -> appliedJobAdapter.submitList(list1));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}