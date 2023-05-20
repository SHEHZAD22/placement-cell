package com.shehzad.careerplacer.admin;

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
import com.shehzad.careerplacer.databinding.ActivityAddNoticeBinding;
import com.shehzad.careerplacer.utils.MyConstants;
import com.shehzad.careerplacer.utils.MyResources;

import java.io.IOException;
import java.util.Objects;

public class AddNoticeActivity extends AppCompatActivity {

    ActivityAddNoticeBinding binding;
    Bitmap bitmap;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    String downloadImgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.add_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Notice");


        binding.selectImage.setOnClickListener(view -> openGallery());
        binding.noticeUploadBtn.setOnClickListener(view -> onUploadBtnClicked());

        binding.noticeTitle.addTextChangedListener(MyResources.createTextWatcher(binding.titleInputLayout));
    }

    private void onUploadBtnClicked() {
        String title = binding.noticeTitle.getText().toString();
        if (title.isEmpty()) binding.titleInputLayout.setError("Must not be empty");
        else if (bitmap == null) MyResources.showToast(this, "Select Image First", "long");
        else uploadImage();

    }


    private void uploadImage() {
        MyResources.showProgressDialog(this, "Uploading...");
        byte[] img = MyResources.compressImage(bitmap);
        final StorageReference filePath = storageReference.child(img + "jpg");
        final UploadTask uploadTask = filePath.putBytes(img);
        uploadTask.addOnCompleteListener(this, task -> {
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
        String title = binding.noticeTitle.getText().toString();

        reference = databaseReference.child("Notice");
        final String key = reference.push().getKey();

        String date = MyResources.getDateTime("date");
        String time = MyResources.getDateTime("time");

        EventModel eventModel = new EventModel(title, downloadImgUrl, date, time, key);
        reference.child(key).setValue(eventModel).addOnSuccessListener(unused -> {
            MyResources.dismissProgressDialog();
            binding.noticeTitle.setText("");
            binding.noticeImageView.setImageBitmap(null);
            bitmap = null;
            MyResources.showToast(this, "Notice Uploaded Successfully", "short");
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(this, "Something went wrong", "short");
        });

    }

    private void openGallery() {
        Intent selectImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectImage, MyConstants.Notice_Gallery_Req_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.Notice_Gallery_Req_code && resultCode == RESULT_OK) {
            Uri uri = data != null ? data.getData() : null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.noticeImageView.setImageBitmap(bitmap);
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