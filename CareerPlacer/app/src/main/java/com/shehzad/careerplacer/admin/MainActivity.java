package com.shehzad.careerplacer.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.ActivityMainBinding;
import com.shehzad.careerplacer.student.StudentActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.admin_text);

        binding.addEvent.setOnClickListener(this);
        binding.addJob.setOnClickListener(this);
        binding.addNotice.setOnClickListener(this);
        binding.addResources.setOnClickListener(this);
        binding.delete.setOnClickListener(this);
        binding.addImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addEvent:
                startActivity(new Intent(this, AddEventActivity.class));
                break;
            case R.id.addJob:
                startActivity(new Intent(this, AddJobActivity.class));
                break;
            case R.id.addNotice:
                startActivity(new Intent(this, AddNoticeActivity.class));
                break;
            case R.id.addResources:
                startActivity(new Intent(this, AddResourcesActivity.class));
                break;
            case R.id.delete:
                startActivity(new Intent(this, DeleteActivity.class));
                break;
            case R.id.addImage:
                startActivity(new Intent(this, StudentActivity.class));
                break;
        }
    }
}