package com.shehzad.careerplacer.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.JobItemLayoutBinding;
import com.shehzad.careerplacer.student.ui.job.ApplyJobActivity;
import com.shehzad.careerplacer.student.ui.job.ViewJobActivity;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<JobModel> list;
    private final AppliedJobViewModel viewModel;

    public JobAdapter(Context context, ArrayList<JobModel> list, AppliedJobViewModel viewModel) {
        this.context = context;
        this.list = list;
        this.viewModel = viewModel;
    }

    public void filterList(ArrayList<JobModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JobItemLayoutBinding binding = JobItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JobModel model = list.get(position);
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //this method is for searchView
    public void filter(String query, ArrayList<JobModel> list) {
        ArrayList<JobModel> filteredList = new ArrayList<>();

        for (JobModel model : list)
            if (model.getTitle().toLowerCase().contains(query.toLowerCase()) || model.getSkills().toLowerCase().contains(query.toLowerCase()))
                filteredList.add(model);

        if (filteredList.isEmpty()) MyResources.showToast(context, "No Data Found...", "short");
        else filterList(filteredList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final JobItemLayoutBinding binding;

        public MyViewHolder(JobItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(JobModel model, int position) {
            binding.title.setText(model.getTitle());
            binding.salary.setText(model.getSalary());
            binding.postedDate.setText(model.getUploadDate());
            binding.jobType.setText(model.getJobType());

            binding.viewDetails.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key", list.get(position));
                context.startActivity(intent);

            });

            binding.apply.setOnClickListener(v -> checkIfItemAlreadyExist(model.getTitle(), model.getPosition()));
        }

        private void checkIfItemAlreadyExist(String title, String position) {

            viewModel.isExist(title, position).observeForever(integer -> {
                if (integer > 0) {
                    MyResources.showToast(context, "Already Applied", "long");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("position", position);
                    Log.d("bundle", "bind: " + bundle.getString("title") + bundle.getString("position"));
                    Intent intent = new Intent(context, ApplyJobActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            //not working: warning: Application cannot be cast to LifecycleOwner
//            LifecycleOwner lifecycleOwner = (LifecycleOwner) context;
//            viewModel.isExist(title, position).observe(lifecycleOwner, count -> {
//                if (count > 0) {
//                    MyResources.showToast(context, "Already Applied", "long");
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("title", title);
//                    bundle.putString("position", position);
//                    Log.d("bundle", "bind: " + bundle.getString("title") + bundle.getString("position"));
//                    Intent intent = new Intent(context, ApplyJobActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

}
