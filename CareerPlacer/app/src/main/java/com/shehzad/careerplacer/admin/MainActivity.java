package com.shehzad.careerplacer.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.ui.AddEventActivity;
import com.shehzad.careerplacer.admin.ui.AddJobActivity;
import com.shehzad.careerplacer.admin.ui.AddNoticeActivity;
import com.shehzad.careerplacer.admin.ui.AddResourcesActivity;
import com.shehzad.careerplacer.admin.ui.deletescreens.DeleteActivity;
import com.shehzad.careerplacer.databinding.ActivityMainBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.admin_text);

        if (MyResources.isConnectedToInternet(this)) {
            binding.addEvent.setOnClickListener(v -> startActivity(new Intent(this, AddEventActivity.class)));
            binding.addJob.setOnClickListener(v -> startActivity(new Intent(this, AddJobActivity.class)));
            binding.addNotice.setOnClickListener(v -> startActivity(new Intent(this, AddNoticeActivity.class)));
            binding.addResources.setOnClickListener(v -> startActivity(new Intent(this, AddResourcesActivity.class)));
            binding.delete.setOnClickListener(v -> showDeleteDialog());
        } else showErrorSnackBar();
    }

    private void showDeleteDialog() {
        final String[] items = {"Notice", "Event", "Job"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Click Item to Delete")
                .setItems(items, (dialog, which) -> {
                    switch (which) {
                        case 0:
                        case 1:
                        case 2:
                            startDeleteActivity(items[which]);
                            dialog.dismiss();
                            break;
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void startDeleteActivity(String title) {
        Intent intent = new Intent(this, DeleteActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void showErrorSnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Check your network connection..", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", view -> {
            snackbar.dismiss();
            recreate();
        });
        snackbar.show();
    }

}