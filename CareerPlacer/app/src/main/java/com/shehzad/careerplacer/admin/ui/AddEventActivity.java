package com.shehzad.careerplacer.admin.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.databinding.ActivityAddEventBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.io.IOException;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1;
    ActivityAddEventBinding binding;
    Bitmap bitmap;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    String downloadImgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.selectImage.setOnClickListener(view -> openGallery());
        binding.eventUploadBtn.setOnClickListener(view -> onUploadBtnClicked());

        binding.eventTitle.addTextChangedListener(MyResources.createTextWatcher(binding.titleInputLayout));
        binding.eventDescription.addTextChangedListener(MyResources.createTextWatcher(binding.descriptionInputLayout));
    }


    private void onUploadBtnClicked() {
        String title = Objects.requireNonNull(binding.eventTitle.getText()).toString();
        String description = Objects.requireNonNull(binding.eventDescription.getText()).toString();
        if (title.isEmpty()) binding.titleInputLayout.setError("Must not be empty");
        else if (description.isEmpty())
            binding.descriptionInputLayout.setError("Must not be empty");
        else if (bitmap == null) uploadData();
        else uploadImage();

    }

    private void uploadImage() {
        MyResources.showProgressDialog(this, "Uploading...");
        byte[] img = MyResources.compressImage(bitmap);
        final StorageReference filePath = storageReference.child("Event").child(img + "jpg");
        final UploadTask uploadTask = filePath.putBytes(img);
        uploadTask.addOnCompleteListener(this, task -> {
            MyResources.dismissProgressDialog();
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadImgUrl = String.valueOf(uri);
                    uploadData();
                }));
            } else {
                MyResources.dismissProgressDialog();
                MyResources.showToast(this, "Unable to Upload", "short");
            }
        });
    }


    private void uploadData() {
        MyResources.showProgressDialog(this, "Uploading...");
        String title = Objects.requireNonNull(binding.eventTitle.getText()).toString();
        String description = Objects.requireNonNull(binding.eventDescription.getText()).toString();
        String url = Objects.requireNonNull(binding.eventUrl.getText()).toString();

        reference = databaseReference.child("Event");
        final String key = reference.push().getKey();

        String date = MyResources.getDateTime("date");
        String time = MyResources.getDateTime("time");

        EventModel eventModel = new EventModel(title, description, url, downloadImgUrl, date, time, key);
        reference.child(Objects.requireNonNull(key)).setValue(eventModel).addOnSuccessListener(unused -> {
            MyResources.dismissProgressDialog();
            binding.eventTitle.setText("");
            binding.eventDescription.setText("");
            binding.eventUrl.setText("");
            binding.eventImageView.setImageBitmap(null);
            bitmap = null;
            MyResources.showToast(this, "Event Uploaded Successfully", "short");
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(this, "Something went wrong", "short");
        });

    }

    private void openGallery() {
        Intent selectImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectImage, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data != null ? data.getData() : null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.eventImageView.setImageBitmap(bitmap);
        }
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