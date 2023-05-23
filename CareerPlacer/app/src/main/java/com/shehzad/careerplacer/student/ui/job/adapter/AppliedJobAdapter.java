package com.shehzad.careerplacer.student.ui.job.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shehzad.careerplacer.databinding.AppliedJobLayoutBinding;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.utils.MyResources;

public class AppliedJobAdapter extends ListAdapter<AppliedJob, AppliedJobAdapter.MyViewHolder> {

    public AppliedJobAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<AppliedJob> DIFF_CALLBACK = new DiffUtil.ItemCallback<AppliedJob>() {
        @Override
        public boolean areItemsTheSame(@NonNull AppliedJob oldItem, @NonNull AppliedJob newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AppliedJob oldItem, @NonNull AppliedJob newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPosition().equals(newItem.getPosition());
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppliedJobLayoutBinding binding = AppliedJobLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppliedJob model = getItem(position);
        holder.bind(model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final AppliedJobLayoutBinding binding;

        public MyViewHolder(AppliedJobLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppliedJob model) {
            Log.d("bind", "bind: " + model.getTitle() + " " + model.getPosition());
            binding.title.setText(model.getTitle());
            binding.position.setText(model.getPosition());
            binding.image.setBackgroundColor(MyResources.getRandomColor());

        }
    }

}

