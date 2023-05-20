package com.shehzad.careerplacer.student.ui.job;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.shape.MaterialShapeDrawable;
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
import com.shehzad.careerplacer.student.ui.job.adapter.AppliedJobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class JobFragment extends Fragment {


    private FragmentJobBinding binding;
    private ArrayList<JobModel> list;
    private JobAdapter adapter;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJobBinding.inflate(inflater, container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("Job");

        binding.jobRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.jobRecView.setHasFixedSize(true);

//        requireActivity().getActionBar().show();
//        setHasOptionsMenu(true);


        binding.topAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.appliedJob:
                    startActivity(new Intent(getActivity(), ShowAppliedJobActivity.class));
                    return true;
                default:
                    return false;
            }
        });

        getData();

        return binding.getRoot();
    }




    private void getData() {
        if (isConnectedToInternet()) {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        JobModel data = snapshot.getValue(JobModel.class);
                        list.add(data);
                    }

                    Collections.reverse(list);
                    adapter = new JobAdapter(getContext(), list);
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

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.job_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.search:
//                MyResources.showToast(getContext(),"Search","long");
//                break;
//            case R.id.appliedJob:
//                startActivity(new Intent(getActivity(),ShowAppliedJobActivity.class));
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}