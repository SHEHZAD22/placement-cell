package com.shehzad.careerplacer.student.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.databinding.ActivityNoticeBinding;
import com.shehzad.careerplacer.student.adapter.NoticeAdapter;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class NoticeActivity extends AppCompatActivity {

    ActivityNoticeBinding binding;

    private ArrayList<EventModel> list;
    private NoticeAdapter adapter;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");
        binding.noticeRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.noticeRecView.setHasFixedSize(true);

        getData();

    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventModel data = snapshot.getValue(EventModel.class);
                    Log.d("snapshot", "onDataChange: " + data);
                    list.add(data);
                }
                Collections.reverse(list);
                adapter = new NoticeAdapter(NoticeActivity.this, list);
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.noticeRecView.setAdapter(adapter);
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