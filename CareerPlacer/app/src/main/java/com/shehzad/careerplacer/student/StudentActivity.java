package com.shehzad.careerplacer.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.ActivityDeleteBinding;
import com.shehzad.careerplacer.databinding.ActivityStudentBinding;

import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    ActivityStudentBinding binding;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,R.id.frameLayout);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }
}