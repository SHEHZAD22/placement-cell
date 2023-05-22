package com.shehzad.careerplacer.admin.ui.deletescreens;

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
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.adapter.DeleteAdapter;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.databinding.ActivityDeleteBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Objects;

public class DeleteActivity extends AppCompatActivity {
    ActivityDeleteBinding binding;

    private ArrayList<EventModel> list;
    private DeleteAdapter adapter;
    private DatabaseReference reference;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = getIntent().getStringExtra("title");

        Objects.requireNonNull(getSupportActionBar()).setTitle("Delete " + title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child(title);

        binding.deleteRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.deleteRecView.setHasFixedSize(true);

        getNotice();

    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        EventModel data = snapshot.getValue(EventModel.class);
//                        Log.d("snapshot", "onDataChange: " + data);
                        list.add(data);
                    }
                    adapter = new DeleteAdapter(getApplicationContext(), list, title);
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.deleteRecView.setAdapter(adapter);
                } else MyResources.showToast(getApplicationContext(), "No Data found", "short");
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