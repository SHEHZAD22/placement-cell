package com.shehzad.careerplacer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shehzad.careerplacer.databinding.ActivityForgotPasswordBinding;
import com.shehzad.careerplacer.utils.MyConstants;
import com.shehzad.careerplacer.utils.MyResources;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.email.addTextChangedListener(MyResources.createTextWatcher(binding.emailInputLayout));

        binding.resetPassword.setOnClickListener(view -> validate());
    }

    private void validate() {
        String email = binding.email.getText().toString();
        if (email.isEmpty()) binding.emailInputLayout.setError("Must not be empty");
        else if (!email.matches(MyConstants.emailPattern))
            binding.emailInputLayout.setError("Enter Correct Email");
        else resetPassword(email);
    }

    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                MyResources.showToast(getApplicationContext(), "A link has been sent to your mail.", "short");
                startLoginActivity();
            }
        }).addOnFailureListener(e -> MyResources.showToast(getApplicationContext(), "Failure : \n" + e.getMessage(), "long"));
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}