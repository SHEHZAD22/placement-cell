package com.shehzad.careerplacer.admin.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.EventModel;
import com.shehzad.careerplacer.databinding.DeleteNoticeItemLayoutBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.ArrayList;

public class DeleteNoticeAdapter extends RecyclerView.Adapter<DeleteNoticeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<EventModel> list;

    public DeleteNoticeAdapter(Context context, ArrayList<EventModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeleteNoticeItemLayoutBinding binding = DeleteNoticeItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        private final DeleteNoticeItemLayoutBinding binding;

        public MyViewHolder(DeleteNoticeItemLayoutBinding binding) {
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

            binding.deleteNotice.setOnClickListener(view -> {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notice");
                reference.child(model.getKey()).removeValue()
                        .addOnCompleteListener(task -> MyResources.showToast(context, "Deleted Successfully", "short"))
                        .addOnFailureListener(e -> MyResources.showToast(context,context.getString(R.string.fail),"short"));
                notifyItemRemoved(getAdapterPosition());

            });
        }
    }
}
