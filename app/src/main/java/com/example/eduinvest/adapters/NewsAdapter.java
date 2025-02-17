package com.example.eduinvest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.databinding.ItemNewsBinding;
import com.example.eduinvest.models.NewsModel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyDicoverViewHolder> {

    private final Context context;
    private final List<NewsModel> dataList;

    public NewsAdapter(Context context, List<NewsModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyDicoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyDicoverViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDicoverViewHolder holder, int position) {
        NewsModel news = dataList.get(position);

        // Load ảnh và thiết lập dữ liệu
        Glide.with(context).load(news.getImageNew()).into(holder.binding.imageNews);
        holder.binding.titleNews.setText(news.getTitleNews());
        holder.binding.timeNews.setText(news.getTimeNews());

        String linkWeb = news.getLinkNews();

        // Xử lý sự kiện click mở link trong trình duyệt
        holder.binding.itemNews.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkWeb));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Thông tin sẽ được cập nhật đến bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyDicoverViewHolder extends RecyclerView.ViewHolder {
        final ItemNewsBinding binding;

        public MyDicoverViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
