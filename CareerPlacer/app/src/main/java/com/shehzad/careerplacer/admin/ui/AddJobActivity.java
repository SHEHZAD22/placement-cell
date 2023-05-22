package com.shehzad.careerplacer.admin.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.ActivityAddJobBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddJobActivity extends AppCompatActivity {
    ActivityAddJobBinding binding;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.add_job);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.jobTitle.addTextChangedListener(MyResources.createTextWatcher(binding.titleInputLayout));
        binding.jobPosition.addTextChangedListener(MyResources.createTextWatcher(binding.positionInputLayout));
        binding.jobSalary.addTextChangedListener(MyResources.createTextWatcher(binding.salaryInputLayout));
        binding.jobType.addTextChangedListener(MyResources.createTextWatcher(binding.jobTypeInputLayout));
        binding.skills.addTextChangedListener(MyResources.createTextWatcher(binding.skillsInputLayout));
        binding.qualification.addTextChangedListener(MyResources.createTextWatcher(binding.qualificationInputLayout));
        binding.applyDate.addTextChangedListener(MyResources.createTextWatcher(binding.applyDateInputLayout));
        binding.jobDescription.addTextChangedListener(MyResources.createTextWatcher(binding.descriptionInputLayout));
        binding.location.addTextChangedListener(MyResources.createTextWatcher(binding.locationInputLayout));
        binding.aboutCompany.addTextChangedListener(MyResources.createTextWatcher(binding.companyInputLayout));
        binding.jobUrl.addTextChangedListener(MyResources.createTextWatcher(binding.urlInputLayout));

        binding.applyDate.setOnClickListener(v -> showDatePickerDialog());
        binding.jobUploadBtn.setOnClickListener(view -> onUploadBtnClicked());
    }


    private void onUploadBtnClicked() {
        String title = binding.jobTitle.getText().toString();
        String position = binding.jobPosition.getText().toString();
        String salary = binding.jobSalary.getText().toString();
        String jobType = binding.jobType.getText().toString();
        String skills = binding.skills.getText().toString();
        String qualification = binding.qualification.getText().toString();
        String lastApplyDate = binding.applyDate.getText().toString();
        String description = binding.jobDescription.getText().toString();
        String location = binding.location.getText().toString();
        String aboutCompany = binding.aboutCompany.getText().toString();
        String url = binding.jobUrl.getText().toString();

        if (title.isEmpty()) binding.titleInputLayout.setError("Must not be empty");
        else if (position.isEmpty()) binding.positionInputLayout.setError("Must not be empty");
        else if (salary.isEmpty()) binding.salaryInputLayout.setError("Must not be empty");
        else if (jobType.isEmpty()) binding.jobTypeInputLayout.setError("Must not be empty");
        else if (skills.isEmpty()) binding.skillsInputLayout.setError("Must not be empty");
        else if (qualification.isEmpty())
            binding.qualificationInputLayout.setError("Must not be empty");
        else if (lastApplyDate.isEmpty())
            binding.applyDateInputLayout.setError("Must not be empty");
        else if (description.isEmpty())
            binding.descriptionInputLayout.setError("Must not be empty");
        else if (location.isEmpty()) binding.locationInputLayout.setError("Must not be empty");
        else if (aboutCompany.isEmpty()) binding.companyInputLayout.setError("Must not be empty");
        else if (url.isEmpty()) binding.urlInputLayout.setError("Must not be empty");
        else
            uploadData(title, position, salary, jobType, skills, qualification, lastApplyDate, description, location, aboutCompany, url);

    }

    private void uploadData(String title, String position, String salary, String jobType, String skills, String qualification, String lastApplyDate, String description, String location, String aboutCompany, String url) {
        MyResources.showProgressDialog(this, "Uploading...");

        reference = databaseReference.child("Job");
        final String key = reference.push().getKey();

        String date = MyResources.getDateTime("date");
        String time = MyResources.getDateTime("time");

        JobModel jobModel = new JobModel(
                title, position, salary, jobType, skills, qualification,
                lastApplyDate, description, location, aboutCompany, url, date, time, key);

        reference.child(key).setValue(jobModel).addOnSuccessListener(unused -> {
            MyResources.dismissProgressDialog();
            clearTextFields();
            MyResources.showToast(this, "Job Uploaded Successfully", "short");
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(this, "Something went wrong", "short");
        });

    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            calendar.set(year, month, dayOfMonth);

            String date = dateFormat.format(calendar.getTime());
            binding.applyDate.setText(date);
        }, currentYear, currentMonth, currentDay);

        dialog.show();
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

    private void clearTextFields() {
        binding.jobTitle.setText("");
        binding.jobPosition.setText("");
        binding.jobSalary.setText("");
        binding.jobType.setText("");
        binding.skills.setText("");
        binding.qualification.setText("");
        binding.applyDate.setText("");
        binding.jobDescription.setText("");
        binding.location.setText("");
        binding.aboutCompany.setText("");
        binding.jobUrl.setText("");
    }
}