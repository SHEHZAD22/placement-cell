package com.shehzad.careerplacer.student.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.admin.model.ResourceModel;
import com.shehzad.careerplacer.databinding.ActivityResourceBinding;
import com.shehzad.careerplacer.databinding.ActivityStudentBinding;
import com.shehzad.careerplacer.student.adapter.NoticeAdapter;
import com.shehzad.careerplacer.student.adapter.ResourceAdapter;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResourceActivity extends AppCompatActivity {

    ActivityResourceBinding binding;

    private List<ResourceModel> list;
    private ResourceAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResourceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Resources");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Resources");

        binding.resourceRecView.setLayoutManager(new LinearLayoutManager(this));
//        binding.resourceRecView.setHasFixedSize(true);

        getData();

    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ResourceModel data = snapshot.getValue(ResourceModel.class);
                    list.add(data);
                }
                Collections.reverse(list);
                adapter = new ResourceAdapter(ResourceActivity.this, list);
//                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.resourceRecView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                MyResources.showToast(getApplicationContext(), error.getMessage(), "short");
            }
        });
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