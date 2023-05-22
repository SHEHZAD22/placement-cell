package com.shehzad.careerplacer.student.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shehzad.careerplacer.LoginActivity;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.RegisterModel;
import com.shehzad.careerplacer.databinding.DialogThemeBinding;
import com.shehzad.careerplacer.databinding.FragmentProfileBinding;
import com.shehzad.careerplacer.databinding.PopupDialogBinding;
import com.shehzad.careerplacer.utils.MyResources;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference studentReference;

    private SharedPreferences sharedPreferences;
    public static final String THEME_PREF = "themePref";
    public static final String SELECTED_THEME = "selectedTheme";
    private int selectedTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        sharedPreferences = getActivity().getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE);
        selectedTheme = sharedPreferences.getInt(SELECTED_THEME, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);

        auth = FirebaseAuth.getInstance();
        studentReference = FirebaseDatabase.getInstance().getReference().child("Student/student_image");

        binding.changePassword.setOnClickListener(v -> showPopupDialog());

        binding.mode.setOnClickListener(v -> showThemeDialog());

        binding.signOut.setOnClickListener(view -> signOut());

        binding.delete.setOnClickListener(v -> deleteDialog());

        setStudentDetails();

        return binding.getRoot();
    }


    private void showPopupDialog() {
        PopupDialogBinding popBinding = PopupDialogBinding.inflate(getLayoutInflater());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Enter new Password")
                .setView(popBinding.getRoot())
                .setPositiveButton("Submit", (dialog, which) -> {
                    String password = popBinding.password.getText().toString();
                    changePassword(password);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changePassword(String password) {
        if (password == null || password.isEmpty())
            MyResources.showToast(getContext(), "Password is empty", "l");
        else if (password.length() < 6)
            MyResources.showToast(getContext(), "Enter at least 8 characters", "l");
        else {
            FirebaseUser user = auth.getCurrentUser();
            user.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    MyResources.showToast(getContext(), "Password Changed.", "l");
                }
            }).addOnFailureListener(e -> MyResources.showToast(getContext(), "Failure: \n" + e.getMessage(), "l"));

        }
    }

    private void setStudentDetails() {
        FirebaseUser user = auth.getCurrentUser();
        binding.email.setText(user.getEmail());
        String key = user.getUid();

        studentReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RegisterModel model = snapshot.getValue(RegisterModel.class);
                    if (model != null) {
                        binding.name.setText(model.getName());
                        Glide.with(getContext()).load(model.getImage())
                                .placeholder(R.drawable.nophoto)
                                .error(R.drawable.nophoto)
                                .into(binding.profileImage.studentImage);
                    }
                } else MyResources.showToast(getContext(), "No Image found", "short");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MyResources.showToast(getContext(), error.getMessage(), "short");
            }
        });
    }


    private void showThemeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Theme");

        DialogThemeBinding themeBinding = DialogThemeBinding.inflate(getLayoutInflater());
        builder.setView(themeBinding.getRoot());

        switch (selectedTheme) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                themeBinding.darkMode.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                themeBinding.lightMode.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                themeBinding.systemMode.setChecked(true);
                break;
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            int checkedId = themeBinding.themeRadioGroup.getCheckedRadioButtonId();

            if (checkedId == R.id.darkMode) {
                selectedTheme = AppCompatDelegate.MODE_NIGHT_YES;
            } else if (checkedId == R.id.lightMode) {
                selectedTheme = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (checkedId == R.id.systemMode) {
                selectedTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }


            applySelectedTheme();
            saveSelectedTheme();
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void applySelectedTheme() {
        AppCompatDelegate.setDefaultNightMode(selectedTheme);
        getActivity().recreate();
    }

    private void saveSelectedTheme() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SELECTED_THEME, selectedTheme);
        editor.apply();
    }

    private void signOut() {
        auth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Delete Account!")
                .setMessage("Do you want to Delete your account permanently?")
                .setPositiveButton("YES", (dialog, which) -> {
                    deleteAccount();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteAccount() {
        auth.getCurrentUser().delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                MyResources.showToast(getContext(), "Account Deleted", "short");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }).addOnFailureListener(e -> {
            MyResources.showToast(getContext(), "Failure: \n" + e.getMessage(), "short");
        });
    }
}