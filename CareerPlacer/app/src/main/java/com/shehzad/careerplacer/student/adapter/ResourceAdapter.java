package com.shehzad.careerplacer.student.adapter;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.shehzad.careerplacer.R;
import com.shehzad.careerplacer.admin.model.ResourceModel;
import com.shehzad.careerplacer.databinding.ResourceItemLayoutBinding;
import com.shehzad.careerplacer.utils.MyResources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.MyViewHolder> {

    private final Context context;
    private final List<ResourceModel> list;

    public ResourceAdapter(Context context, List<ResourceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResourceItemLayoutBinding binding = ResourceItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResourceModel model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ResourceItemLayoutBinding binding;

        public MyViewHolder(ResourceItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ResourceModel model) {
            String pdfUrl = model.getPdf();

            binding.title.setText(model.getTitle());
            binding.description.setText(model.getDescription());


            if (pdfUrl != null && !pdfUrl.isEmpty()) {
                binding.pdfName.setText(getPdfName(pdfUrl));
            }

            binding.resourceCard.setOnClickListener(view -> {
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

            if (model.getUrl().isEmpty() || model.getUrl() == null) {
                binding.url.setVisibility(View.GONE);
            } else {
                binding.url.setText(model.getUrl());
            }


            binding.pdfName.setOnClickListener(v -> {
                if (pdfUrl != null && !pdfUrl.isEmpty()) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
                    String title = getPdfName(pdfUrl);
                    Log.d("download", "bind: " + title);
                    request.setTitle(title);
                    request.setDescription("Downloading Please wait....");
                    String cookie = CookieManager.getInstance().getCookie(pdfUrl);
                    request.addRequestHeader("cookie", cookie);
                    request.setAllowedOverMetered(true);
                    request.setVisibleInDownloadsUi(false);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                    MyResources.showToast(context, "Downloading started", "long");

                } else MyResources.showToast(context, "Sorry \nNo pdf found", "short");
            });

            binding.share.setOnClickListener(v -> {
                if (pdfUrl != null && !pdfUrl.isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, pdfUrl);
                    context.startActivity(shareIntent);
                } else MyResources.showToast(context, "Sorry \nNo pdf found", "short");

            });
        }

        private String getPdfName(String url) {
            Pattern pattern = Pattern.compile(".+(\\/|%2F)(.+)\\?.+");
            Matcher matcher = pattern.matcher(url);
            String decoded = null;
            if (matcher.find()) {
                try {
                    decoded = URLDecoder.decode(matcher.group(2), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return decoded;
        }
    }
}
