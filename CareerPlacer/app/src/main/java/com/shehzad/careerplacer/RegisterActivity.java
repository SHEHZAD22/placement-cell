package com.shehzad.careerplacer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shehzad.careerplacer.admin.model.RegisterModel;
import com.shehzad.careerplacer.databinding.ActivityRegisterBinding;
import com.shehzad.careerplacer.utils.MyConstants;
import com.shehzad.careerplacer.utils.MyResources;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    private FirebaseAuth auth;

    Bitmap bitmap;
    DatabaseReference databaseReference, reference;
    StorageReference storageReference;
    String downloadImgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.name.addTextChangedListener(MyResources.createTextWatcher(binding.nameInputLayout));
        binding.email.addTextChangedListener(MyResources.createTextWatcher(binding.emailInputLayout));
        binding.password.addTextChangedListener(MyResources.createTextWatcher(binding.passwordInputLayout));
        binding.confirmPassword.addTextChangedListener(MyResources.createTextWatcher(binding.confirmPasswordInputLayout));

        binding.selectImage.setOnClickListener(view -> openGallery());

        binding.registerBtn.setOnClickListener(view -> setRegistration());

        binding.login.setOnClickListener(view -> startLoginActivity());
    }

    //signup validation and error messages
    private void setRegistration() {
        String name = binding.name.getText().toString();
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();

        if (name.isEmpty()) binding.nameInputLayout.setError("Must not be empty");
        else if (email.isEmpty()) binding.emailInputLayout.setError("Must not be empty");
        else if (!email.matches(MyConstants.emailPattern))
            binding.emailInputLayout.setError("Enter Correct Email");
        else if (password.isEmpty()) binding.passwordInputLayout.setError("Must not be empty");
        else if (password.length() < 6)
            binding.passwordInputLayout.setError("Enter atleast 8 characters");
        else if (confirmPassword.isEmpty())
            binding.confirmPasswordInputLayout.setError("Must not be empty");
        else if (!password.equals(confirmPassword))
            binding.confirmPasswordInputLayout.setError("Password not matched");
        else if (bitmap == null) MyResources.showToast(this, "Select Image First", "long");
        else performRegistration(email, password);

    }

    private void performRegistration(String email, String password) {
        MyResources.showProgressDialog(RegisterActivity.this, "Please Wait...");

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) uploadImage();
            else {
                MyResources.dismissProgressDialog();
                MyResources.showToast(RegisterActivity.this, "Failed to Register: " + task.getException(), "short");
            }
        });
    }

    private void uploadImage() {
        byte[] img = MyResources.compressImage(bitmap);
        final StorageReference filePath = storageReference.child("Student/student_image/" + img + "jpg");
        final UploadTask uploadTask = filePath.putBytes(img);
        uploadTask.addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadImgUrl = String.valueOf(uri);
                    Log.d("check", "uploadImage: " + downloadImgUrl);
                    uploadData1();
                }));
            }
        });
    }


    private void uploadData1() {
        String name = binding.name.getText().toString();
        Log.d("check1", "true " + name);

        reference = databaseReference.child("Student/student_image");
        String key = auth.getCurrentUser().getUid();

        Log.d("key", "uploadData: " + key);
        RegisterModel model = new RegisterModel(key, name, downloadImgUrl);
        reference.child(key).setValue(model).addOnSuccessListener(unused -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(RegisterActivity.this, "Registered Successfully", "short");
            Log.d("check2", "uploadData: " + "World");
            binding.previewImage.setImageBitmap(null);
            bitmap = null;
            startLoginActivity();
        });

    }

    private void openGallery() {
        Intent selectImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectImage, MyConstants.REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data != null ? data.getData() : null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            binding.previewImage.setImageBitmap(bitmap);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}