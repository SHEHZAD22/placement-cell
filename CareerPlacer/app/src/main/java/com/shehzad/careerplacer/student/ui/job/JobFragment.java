package com.shehzad.careerplacer.student.ui.job;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.FragmentJobBinding;
import com.shehzad.careerplacer.student.adapter.JobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JobFragment extends Fragment {


    private FragmentJobBinding binding;
    private ArrayList<JobModel> list;
    //    private ArrayList<JobModel> searchList;
    private JobAdapter adapter;
    private DatabaseReference reference;
    private AppliedJobViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobBinding.inflate(inflater, container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("Job");

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        binding.jobRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.jobRecView.setHasFixedSize(true);

        binding.topAppBar.setOnMenuItemClickListener(item -> {
//            int menuItem = item.getItemId();
            switch (item.getItemId()) {

                case R.id.appliedJob:
                    startActivity(new Intent(getActivity(), ShowAppliedJobActivity.class));
                    return true;
                default:
                    return false;
//                case R.id.search:
//                    SearchView searchView = (SearchView) item.getActionView();
//                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                        @Override
//                        public boolean onQueryTextSubmit(String query) {
//                            getData();
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onQueryTextChange(String newText) {
//                            return true;
//                        }
//                    });
//                    Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
//                    return true;

            }
        });

        getData();

        return binding.getRoot();
    }


    private void getData() {
        if (MyResources.isConnectedToInternet(getContext())) {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        JobModel data = snapshot.getValue(JobModel.class);
                        list.add(data);
                    }

                    Collections.reverse(list);
                    adapter = new JobAdapter(getContext(), list, viewModel);
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.jobRecView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar.setVisibility(View.GONE);
                    MyResources.showToast(getContext(), error.getMessage(), "short");
                }
            });
        } else showErrorSnackBar();
    }

    //showing an error dialog box
    private void showErrorSnackBar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Check your network connection..", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", view -> {
            getData();
            snackbar.dismiss();
        });
        snackbar.show();
    }

}