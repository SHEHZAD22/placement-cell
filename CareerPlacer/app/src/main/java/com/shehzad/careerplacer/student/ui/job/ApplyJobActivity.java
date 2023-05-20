package com.shehzad.careerplacer.student.ui.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.AddResourcesActivity;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.ActivityApplyJobBinding;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJobDao;
import com.shehzad.careerplacer.student.ui.job.db.MyDatabase;
import com.shehzad.careerplacer.student.ui.job.model.StudentResumeModel;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.HashMap;
import java.util.Objects;

public class ApplyJobActivity extends AppCompatActivity {

    ActivityApplyJobBinding binding;

    private final int REQ_CODE = 5;
    Uri pdfUri;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    private String pdfName;
    private String name, email, phoneNumber, qualification, branch, pincode, location;
    //    private JobModel jobModel;
//MyDatabase myDatabase;
//    AppliedJobDao jobDao;
    private AppliedJobViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Apply Job");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        binding.selectPdf.setOnClickListener(view -> openGallery());
        binding.submitBtn.setOnClickListener(view -> onSubmitBtnClicked());

        binding.name.addTextChangedListener(MyResources.createTextWatcher(binding.nameInputLayout));
        binding.email.addTextChangedListener(MyResources.createTextWatcher(binding.emailInputLayout));
        binding.phoneNumber.addTextChangedListener(MyResources.createTextWatcher(binding.phoneNumberInputLayout));
        binding.qualification.addTextChangedListener(MyResources.createTextWatcher(binding.qualificationInputLayout));
        binding.branch.addTextChangedListener(MyResources.createTextWatcher(binding.branchInputLayout));
        binding.location.addTextChangedListener(MyResources.createTextWatcher(binding.locationInputLayout));
        binding.pincode.addTextChangedListener(MyResources.createTextWatcher(binding.pincodeInputLayout));
    }


    private void onSubmitBtnClicked() {
        name = binding.name.getText().toString();
        email = binding.email.getText().toString();
        phoneNumber = binding.phoneNumber.getText().toString();
        qualification = binding.qualification.getText().toString();
        branch = binding.branch.getText().toString();
        pincode = binding.pincode.getText().toString();
        location = binding.location.getText().toString();

        if (name.isEmpty()) binding.nameInputLayout.setError("Must not be empty");
        else if (email.isEmpty()) binding.emailInputLayout.setError("Must not be empty");
        else if (phoneNumber.isEmpty())
            binding.phoneNumberInputLayout.setError("Must not be empty");
        else if (qualification.isEmpty())
            binding.qualificationInputLayout.setError("Must not be empty");
        else if (branch.isEmpty()) binding.branchInputLayout.setError("Must not be empty");
        else if (pincode.isEmpty()) binding.pincodeInputLayout.setError("Must not be empty");
        else if (location.isEmpty()) binding.locationInputLayout.setError("Must not be empty");
        else if (binding.viewPdf.getText().toString().equals("No file selected") || pdfUri == null)
            MyResources.showToast(this, "Please Select Resume", "long");
        else uploadPdf();

    }

    private void uploadPdf() {
        MyResources.showProgressDialog(ApplyJobActivity.this, "Submitting...");
        StorageReference reference = storageReference.child("Student/resume_details/" + pdfName);
        reference.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri uri = uriTask.getResult();
                    uploadData(String.valueOf(uri));
                }).addOnFailureListener(e -> {
                    MyResources.dismissProgressDialog();
                    MyResources.showToast(getApplicationContext(), "Something went wrong", "short");
                });
    }

    private void uploadData(String resume) {
//        String jobTitle = "N/A", jobPosition = "N/A";
        Bundle bundle = getIntent().getExtras();

//        if (bundle != null) {
        String jobTitle = bundle.getString("title");
        String jobPosition = bundle.getString("position");
//        }

        //inserting into database
        AppliedJob appliedJob = new AppliedJob(jobTitle, jobPosition);
        viewModel.insert(appliedJob);

        Log.d("title", "uploadData: " + jobTitle);
        Log.d("position", "uploadData: " + jobPosition);

        reference = databaseReference.child("Student/resume_details");
        String key = reference.push().getKey();

        StudentResumeModel model = new StudentResumeModel(name, email, phoneNumber, qualification, branch, pincode, location, resume, jobTitle, jobPosition);

        reference.child(key).setValue(model).addOnCompleteListener(task -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(this, "Submitted Successfully", "short");
            finish();
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(getApplicationContext(), "Unable to submit", "short");
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select resume "), REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            pdfUri = data != null ? data.getData() : null;

            pdfName = getFileNameFromUri(pdfUri);
            binding.viewPdf.setText(pdfName);
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String result = "default";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                result = cursor.getString(index);
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    private void clearTextFields() {

        binding.viewPdf.setText(R.string.no_file_selected);
        binding.name.setText("");
        binding.email.setText("");
        binding.pincode.setText("");
        binding.phoneNumber.setText("");
        binding.pincode.setText("");
        binding.qualification.setText("");
        binding.branch.setText("");
        binding.location.setText("");
        pdfUri = null;
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