package com.shehzad.careerplacer.student.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.shehzad.careerplacer.LoginActivity;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.FragmentHomeBinding;
import com.shehzad.careerplacer.databinding.FragmentProfileBinding;
import com.shehzad.careerplacer.student.ui.job.ShowAppliedJobActivity;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        binding.signOut.setOnClickListener(view -> signOut());

        return  binding.getRoot();
    }

    private void signOut() {
        auth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}