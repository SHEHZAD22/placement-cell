package com.shehzad.careerplacer.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.shehzad.careerplacer.databinding.ActivityFullImageBinding;

import java.util.Objects;

public class FullImageActivity extends AppCompatActivity {
    ActivityFullImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        //make activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String imageUrl = getIntent().getStringExtra("image");

        Glide.with(this).load(imageUrl).into(binding.fullImage);
    }
}