package com.shehzad.careerplacer.student.ui.job;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shehzad.careerplacer.databinding.FragmentShowAppliedJobBinding;
import com.shehzad.careerplacer.student.ui.job.adapter.AppliedJobAdapter;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;


public class ShowAppliedJobFragment extends Fragment {

    private FragmentShowAppliedJobBinding binding;
    private AppliedJobViewModel viewModel;
    private AppliedJobAdapter appliedJobAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowAppliedJobBinding.inflate(inflater, container, false);

        binding.appliedJobRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.appliedJobRecView.setHasFixedSize(true);
        appliedJobAdapter = new AppliedJobAdapter();
        binding.appliedJobRecView.setAdapter(appliedJobAdapter);

        viewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);

        fetchData();
        return binding.getRoot();
    }

    private void fetchData() {
        viewModel.getAllJob().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                binding.text.setVisibility(View.GONE);
                appliedJobAdapter.submitList(list);
            }
        });
    }

}