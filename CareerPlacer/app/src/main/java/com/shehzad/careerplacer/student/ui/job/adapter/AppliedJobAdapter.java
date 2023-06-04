package com.shehzad.careerplacer.student.ui.job.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.AppliedJobLayoutBinding;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;

import java.util.List;

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
        holder.bind(model, position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final AppliedJobLayoutBinding binding;

//        private int[] colors = {Color.BLACK, Color.argb(255, 236, 233, 46)};

        public MyViewHolder(AppliedJobLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppliedJob model, int position) {

//            Log.d("bind", "bind: " + model.getTitle() + " " + model.getPosition());
            binding.title.setText(model.getTitle());
            binding.position.setText(model.getPosition());
            List<AppliedJob> list = getCurrentList();
            int length = list.size();

            for (int i = 0; i < length; i++) {
                if (position % 2 != 0) {
                    binding.image.setBackgroundColor(Color.argb(255, 236, 233, 46));
                    binding.image.setImageResource(R.drawable.ic_done_black);
                }
            }
//            binding.image.setBackgroundColor(colors[position % colors.length]);

        }

    }

}

