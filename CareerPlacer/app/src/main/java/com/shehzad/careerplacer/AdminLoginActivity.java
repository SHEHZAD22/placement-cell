package com.shehzad.careerplacer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shehzad.careerplacer.admin.MainActivity;
import com.shehzad.careerplacer.databinding.ActivityAdminLoginBinding;
import com.shehzad.careerplacer.utils.MyResources;

public class AdminLoginActivity extends AppCompatActivity {

    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> onLoginClicked());

        binding.username.addTextChangedListener(MyResources.createTextWatcher(binding.usernameInputLayout));
        binding.password.addTextChangedListener(MyResources.createTextWatcher(binding.passwordInputLayout));

    }

    //Validation and error messages
    private void onLoginClicked() {
        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();

        if (username.isEmpty()) binding.usernameInputLayout.setError("Username must not be empty");
        else if (password.isEmpty())
            binding.passwordInputLayout.setError("Password must not be empty");
        else if (!username.equals("admin007")) showErrorDialog("Username");
        else if ( !password.equals("iamAdmin007@")) showErrorDialog("Password");
        else startMainactivity();
    }

    //Validation & error dialog
    private void showErrorDialog(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage(s + " is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void startMainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}