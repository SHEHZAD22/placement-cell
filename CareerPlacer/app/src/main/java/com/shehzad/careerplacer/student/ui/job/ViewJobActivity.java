package com.shehzad.careerplacer.student.ui.job;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.ActivityApplyJobBinding;
import com.shehzad.careerplacer.databinding.ActivityViewJobBinding;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.Objects;

public class ViewJobActivity extends AppCompatActivity {
    ActivityViewJobBinding binding;
    private JobModel model;
    private AppliedJobViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Job Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        model = getIntent().getParcelableExtra("key");

        setData();

        binding.applyBtn.setOnClickListener(view -> {

            onApplyBtnClicked();

        });
    }

    private void onApplyBtnClicked() {
        viewModel.isExist(model.getTitle(), model.getPosition()).observe(this, count -> {
            if (count > 0) MyResources.showToast(getApplicationContext(), "Already Applied", "long");
            else startActivity(new Intent(this, ApplyJobActivity.class));
        });
    }

    private void setData() {
        binding.title.setText(model.getTitle());
        binding.position.setText(model.getPosition());
        binding.jobType.setText(model.getJobType());
        binding.postedDate.setText(model.getUploadDate());
        binding.salary.setText(model.getSalary());
        binding.applyDate.setText(model.getLastApplyDate());
        binding.location.setText(model.getLocation());
        binding.url.setText(model.getUrl());
        binding.aboutCompany.setText(model.getAboutCompany());
        binding.description.setText(model.getDescription());
        binding.skills.setText(model.getSkills());
        binding.qualification.setText(model.getQualification());
        binding.progressBar.setVisibility(View.GONE);
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