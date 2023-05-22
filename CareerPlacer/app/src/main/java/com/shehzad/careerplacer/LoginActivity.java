package com.shehzad.careerplacer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shehzad.careerplacer.databinding.ActivityAdminLoginBinding;
import com.shehzad.careerplacer.databinding.ActivityLoginBinding;
import com.shehzad.careerplacer.student.StudentActivity;
import com.shehzad.careerplacer.utils.MyConstants;
import com.shehzad.careerplacer.utils.MyResources;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        //if already LoggedIn
        if (auth.getCurrentUser() != null) {
            startStudentActivity();
        }

        binding.email.addTextChangedListener(MyResources.createTextWatcher(binding.emailInputLayout));
        binding.password.addTextChangedListener(MyResources.createTextWatcher(binding.passwordInputLayout));

        binding.loginButton.setOnClickListener(view -> onLoginClicked());

        binding.forgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            finish();
        });

        binding.admin.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });

        binding.register.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

    }

    private void onLoginClicked() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        if (email.isEmpty()) binding.emailInputLayout.setError("Must not be empty");
        else if (!email.matches(MyConstants.emailPattern))
            binding.emailInputLayout.setError("Enter Correct Email");
        else if (password.isEmpty()) binding.passwordInputLayout.setError("Must not be empty");
        else if (password.length() < 6)
            binding.passwordInputLayout.setError("Enter at least 8 characters");
        else performLogin(email, password);
    }

    private void performLogin(String email, String password) {
        MyResources.showProgressDialog(this, "Please Wait...");
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                MyResources.dismissProgressDialog();
                startStudentActivity();
                MyResources.showToast(getApplicationContext(), "Logged In", "short");
            }
        }).addOnFailureListener(e -> {
            MyResources.dismissProgressDialog();
            MyResources.showToast(getApplicationContext(), "Failure : \n" + e.getMessage(), "long");
        });
    }


    //Validation & error dialog
    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Email or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void startStudentActivity() {
        startActivity(new Intent(this, StudentActivity.class));
        finish();
    }
}