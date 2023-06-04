package com.shehzad.careerplacer.student.ui.job;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.ActivityAllJobBinding;
import com.shehzad.careerplacer.student.adapter.JobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class AllJobActivity extends AppCompatActivity {

    ActivityAllJobBinding binding;


    private AppliedJobViewModel viewModel;
    private ArrayList<JobModel> list;
    private JobAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAllJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        reference = FirebaseDatabase.getInstance().getReference().child("Job");

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        binding.jobRecView.setLayoutManager(new LinearLayoutManager(this));
        binding.jobRecView.setHasFixedSize(true);

        getData();
    }

    private void getData() {
        if (MyResources.isConnectedToInternet(getApplicationContext())) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        JobModel data = snapshot.getValue(JobModel.class);
                        list.add(data);
                    }

                    Collections.reverse(list);
                    adapter = new JobAdapter(getApplicationContext(), list, viewModel);
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.jobRecView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar.setVisibility(View.GONE);
                    MyResources.showToast(getApplicationContext(), error.getMessage(), "short");
                }
            });
        } else showErrorSnackBar();
    }

    //showing an error dialog box
    private void showErrorSnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Check your network connection..", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", view -> {
            getData();
            snackbar.dismiss();
        });
        snackbar.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query != null) {
                    adapter.filter(query, list);
                }
                return true;
            }
        });

        return true;
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