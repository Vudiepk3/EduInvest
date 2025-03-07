package com.example.eduinvest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.databinding.ItemNewsBinding;
import com.example.eduinvest.databinding.ItemNewsTrendingBinding;
import com.example.eduinvest.models.NewsModel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_NEWS_TRENDING = 0;
    private static final int ITEM_NEWS = 1;

    private final Context context;
    private final List<NewsModel> dataList;

    public NewsAdapter(Context context, List<NewsModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getTypeNews().equals("Trending") ? ITEM_NEWS_TRENDING : ITEM_NEWS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_NEWS_TRENDING) {
            ItemNewsTrendingBinding binding = ItemNewsTrendingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TrendingViewHolder(binding);
        } else {
            ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new NewsViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsModel news = dataList.get(position);
        String linkWeb = news.getLinkNews();

        if (holder instanceof TrendingViewHolder) {
            TrendingViewHolder trendingHolder = (TrendingViewHolder) holder;
            Glide.with(context).load(news.getImageNew()).into(trendingHolder.binding.imageNews);
            trendingHolder.binding.titleNews.setText(news.getTitleNews());
            trendingHolder.binding.timeNews.setText(news.getTimeNews());

            trendingHolder.binding.itemNewsTrending.setOnClickListener(v -> openLink(linkWeb));
        } else if (holder instanceof NewsViewHolder) {
            NewsViewHolder newsHolder = (NewsViewHolder) holder;
            Glide.with(context).load(news.getImageNew()).into(newsHolder.binding.imageNews);
            newsHolder.binding.titleNews.setText(news.getTitleNews());
            newsHolder.binding.timeNews.setText(news.getTimeNews());

            newsHolder.binding.itemNews.setOnClickListener(v -> openLink(linkWeb));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void openLink(String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Thông tin sẽ được cập nhật đến bạn", Toast.LENGTH_SHORT).show();
        }
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        final ItemNewsBinding binding;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class TrendingViewHolder extends RecyclerView.ViewHolder {
        final ItemNewsTrendingBinding binding;

        public TrendingViewHolder(@NonNull ItemNewsTrendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
