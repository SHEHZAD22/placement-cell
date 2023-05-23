package com.shehzad.careerplacer.student.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.admin.model.RegisterModel;
import com.shehzad.careerplacer.databinding.FragmentHomeBinding;
import com.shehzad.careerplacer.student.adapter.JobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private ArrayList<JobModel> list;
    private JobAdapter adapter;
    private DatabaseReference reference;
    private DatabaseReference studentReference;
    private FirebaseAuth auth;
    private AppliedJobViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("Job");
        studentReference = FirebaseDatabase.getInstance().getReference().child("Student/student_image");

        auth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        binding.jobRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.jobRecView.setHasFixedSize(true);

        binding.events.setOnClickListener(this);
        binding.notices.setOnClickListener(this);
        binding.resources.setOnClickListener(this);

        binding.seeAll.setOnClickListener(v -> {
            MyResources.showToast(getContext(), "Press back button to go back", "l");
            Navigation.findNavController(v).navigate(R.id.navigation_job);
        });

        setStudentDetails();

        getData();

        return binding.getRoot();
    }

    private void getData() {
        if (MyResources.isConnectedToInternet(getContext())) {
            reference.limitToLast(10).addValueEventListener(new ValueEventListener() {
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

    private void setStudentDetails() {
        String key = auth.getCurrentUser().getUid();
        Log.d("called", "setStudentDetails: " + "true");

        studentReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("inside", "true");
                if (snapshot.exists()) {
                    Log.d("snapshot", "true");
                    RegisterModel model = snapshot.getValue(RegisterModel.class);
                    if (model != null) {
                        Log.d("register", "true");
                        Log.d("student", "onDataChange: " + model.getName() + model.getImage() + model.getKey() + key);
                        binding.studentName.setText(model.getName());
                        Glide.with(getContext()).load(model.getImage())
                                .placeholder(R.drawable.nophoto)
                                .error(R.drawable.nophoto)
                                .into(binding.studentImage.studentImage);
                    }
                } else MyResources.showToast(getContext(), "No Image found", "short");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyResources.showToast(getContext(), error.getMessage(), "short");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.events:
                startActivity(new Intent(getActivity(), EventActivity.class));
                break;
            case R.id.notices:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.resources:
                startActivity(new Intent(getActivity(), ResourceActivity.class));
                break;
        }
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