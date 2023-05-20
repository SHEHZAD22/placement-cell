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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.shehzad.careerplacer.admin.model.JobModel;
import com.shehzad.careerplacer.databinding.JobItemLayoutBinding;
import com.shehzad.careerplacer.student.ui.job.ApplyJobActivity;
import com.shehzad.careerplacer.student.ui.job.ViewJobActivity;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJobDao;
import com.shehzad.careerplacer.student.ui.job.db.MyDatabase;
import com.shehzad.careerplacer.student.ui.job.viewmodel.AppliedJobViewModel;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<JobModel> list;
    private AppliedJobViewModel viewModel ;
//    MyDatabase myDatabase;
//    AppliedJobDao jobDao;

    public JobAdapter(Context context, ArrayList<JobModel> list) {
        this.context = context;
        this.list = list;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AppliedJobViewModel.class);
//        myDatabase = MyDatabase.getDbInstance(context);
//        jobDao = myDatabase.appliedJobDao();
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
//
//    public void submitList(List<AppliedJob> list) {
//        this.list = list;
//    }


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
                intent.putExtra("key", list.get(position));
                context.startActivity(intent);

            });


            binding.apply.setOnClickListener(v -> {

                checkIfItemAlreadyExist(model.getTitle(), model.getPosition());

            });
        }

        private void checkIfItemAlreadyExist(String title, String position){
            viewModel.isExist(title, position).observe((LifecycleOwner) context, count -> {
                if (count > 0){
                    MyResources.showToast(context,"Already Applied","long");
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("position", position);
                    Log.d("bundle", "bind: " + bundle.getString("title") + bundle.getString("position"));
                    Intent intent = new Intent(context, ApplyJobActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }


}
