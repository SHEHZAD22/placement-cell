package com.shehzad.careerplacer.student.ui.job.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.databinding.AppliedJobLayoutBinding;
import com.shehzad.careerplacer.student.ui.job.db.AppliedJob;
import com.shehzad.careerplacer.utils.MyResources;

import java.util.List;
import java.util.Random;

public class AppliedJobAdapter extends ListAdapter<AppliedJob, AppliedJobAdapter.MyViewHolder> {

////    private Context context;
//    private List<AppliedJob> list = new ArrayList<>();
List<Integer> colorCode;

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
//
//    public AppliedJobAdapter(Context context, List<AppliedJob> list) {
//        this.context = context;
//        this.list = list;
//    }

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

//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public void submitList(List<AppliedJob> list){
//        this.list = list;
//        notifyDataSetChanged();
//    }

//private int getRandomColor(){
//        List<Integer> colorCode = new ArrayList<>();
//
//        colorCode.add(R.color.color1);
//        colorCode.add(R.color.color2);
//        colorCode.add(R.color.color3);
//        colorCode.add(R.color.color4);
//        colorCode.add(R.color.color5);
//
//        Random random = new Random();
//        int randomColor = random.nextInt(colorCode.size());
//        return randomColor;
//}


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final AppliedJobLayoutBinding binding;

        private final int[] colors = {R.color.blue_a700, R.color.pink_a400, R.color.purple_600, R.color.yellow_600, R.color.pink_a700, R.color.brown_a200};


        public MyViewHolder(AppliedJobLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppliedJob model, int position) {
            Log.d("bind", "bind: " + model.getTitle() + " " + model.getPosition());
            Random random = new Random();
            int randomNumber = random.nextInt(colors.length);
            binding.title.setText(model.getTitle());
            binding.position.setText(model.getPosition());

//            binding.image.setBackgroundColor(colors[randomNumber]);
//            binding.image.setBackgroundColor(MyResources.getRandomColor());
//            binding.image.setBackgroundColor(getRandomColor());
////            binding.image.setBackgroundColor(getRandomColor());
//            binding.image.setBackgroundColor(itemView.getResources().getColor(getRandomColor()));
//            binding.image.setBackgroundColor(getResources().getColor(getRandomColor()));

        }
    }


}

