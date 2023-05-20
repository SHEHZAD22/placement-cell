package com.shehzad.careerplacer.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.databinding.EventItemLayoutBinding;
import com.shehzad.careerplacer.utils.FullImageActivity;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<EventModel> list;

    public EventAdapter(Context context, ArrayList<EventModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EventItemLayoutBinding binding = EventItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventModel model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final EventItemLayoutBinding binding;

        public MyViewHolder(EventItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EventModel model) {
            Glide.with(context)
                    .load(model.getImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.nophoto)
                    .error(R.drawable.nophoto)
                    .into(binding.image);

            binding.title.setText(model.getTitle());
            binding.description.setText(model.getDescription());

            if (!model.getUrl().isEmpty() && model.getUrl() != null)
                binding.url.setText(model.getUrl());
            else binding.url.setVisibility(View.GONE);

            binding.postedDate.setText(model.getDate());
            binding.postedTime.setText(model.getTime());

            binding.eventCard.setOnClickListener(view -> {
                if (binding.hiddenLayout.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(binding.baseLayout, new AutoTransition());
                    binding.hiddenLayout.setVisibility(View.GONE);
                    binding.expand.setImageResource(R.drawable.ic_expand_more);
                } else {
                    TransitionManager.beginDelayedTransition(binding.baseLayout, new AutoTransition());
                    binding.hiddenLayout.setVisibility(View.VISIBLE);
                    binding.expand.setImageResource(R.drawable.ic_expand_less);
                }
            });


            binding.image.setOnClickListener(view -> {

                if (model.getImage().isEmpty() || model.getImage() == null) {
                    MyResources.showToast(context, "No image found", "short");
                } else {
                    Intent intent = new Intent(context, FullImageActivity.class);
                    intent.putExtra("image", model.getImage());
                    context.startActivity(intent);
                }

            });
        }
    }
}
