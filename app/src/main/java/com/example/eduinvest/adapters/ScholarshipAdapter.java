package com.example.eduinvest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.databinding.ItemScholarshipBinding;
import com.example.eduinvest.models.NewsModel;
import com.example.eduinvest.utils.Common;

import java.util.List;

public class ScholarshipAdapter extends RecyclerView.Adapter<ScholarshipAdapter.MyViewHolder> {

    private final List<NewsModel> messageModelList;
    private final Context context;

    public ScholarshipAdapter(List<NewsModel> messageModelList, Context context) {
        this.messageModelList = messageModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScholarshipBinding binding = ItemScholarshipBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewsModel messageModel = messageModelList.get(position);
        Glide.with(context).load(messageModel.getImageNew()).into(holder.binding.imageScholarship);
        holder.binding.titlesSholarship.setText(messageModel.getTitleNews());
        String linkWeb = messageModel.getLinkNews();
        holder.binding.btnRegister.setOnClickListener(v -> {
            Common.logEvent(context, "click_item_scholarship" + messageModel.getTitleNews());
            openLink(v, linkWeb);
        }); // Truyền context qua View
    }

    private void openLink(View view, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            view.getContext().startActivity(intent); // Sử dụng context từ view
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Thông tin sẽ được cập nhật đến bạn", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final ItemScholarshipBinding binding;

        public MyViewHolder(@NonNull ItemScholarshipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
