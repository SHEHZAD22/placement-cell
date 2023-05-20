package com.shehzad.careerplacer.admin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.ActivityAddResourcesBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.HashMap;
import java.util.Objects;

public class AddResourcesActivity extends AppCompatActivity {
    private final int REQ_CODE = 3;
    Uri pdfUri;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    private String pdfName;
    String title, description, url;
    ActivityAddResourcesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddResourcesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.resources);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.selectPdf.setOnClickListener(view -> openGallery());
        binding.resourceUploadBtn.setOnClickListener(view -> onUploadBtnClicked());

        binding.resourceTitle.addTextChangedListener(MyResources.createTextWatcher(binding.titleInputLayout));
        binding.resourceDescription.addTextChangedListener(MyResources.createTextWatcher(binding.descriptionInputLayout));
    }


    private void onUploadBtnClicked() {
        title = binding.resourceTitle.getText().toString();
        description = binding.resourceDescription.getText().toString();
        url = binding.resourceUrl.getText().toString();
        if (title.isEmpty()) binding.titleInputLayout.setError("Must not be empty");
        else if (description.isEmpty())
            binding.descriptionInputLayout.setError("Must not be empty");
        else if (binding.viewPdf.getText().toString().equals("No pdf Selected") || pdfUri == null)
            MyResources.showToast(this, "Please Select Pdf", "long");
        else uploadPdf();

    }

    private void uploadPdf() {
//        if (pdfUri != null) {
        MyResources.showProgressDialog(this, "Uploading...");
        StorageReference reference = storageReference.child("Resources/" + pdfName);
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
//        }
//        else MyResources.showToast(this,"Select Pdf first","short");
    }

    private void uploadData(String downloadUrl) {
        reference = databaseReference.child("Resources");
        String key = reference.push().getKey();
        HashMap data = new HashMap();
        data.put("title", title);
        data.put("description", description);
        data.put("url", url);
        data.put("pdf", downloadUrl);

        reference.child(key).setValue(data).addOnCompleteListener(task -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(AddResourcesActivity.this, "Uploaded Successfully", "short");
            binding.viewPdf.setText(R.string.no_file_selected);
            binding.resourceTitle.setText("");
            binding.resourceDescription.setText("");
            binding.resourceUrl.setText("");
            pdfUri = null;
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(getApplicationContext(), "Unable to upload", "short");
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf "), REQ_CODE);
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