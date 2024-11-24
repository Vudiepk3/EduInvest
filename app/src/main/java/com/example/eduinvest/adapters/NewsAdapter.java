package com.example.eduinvest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new MyDicoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDicoverViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageNew()).into(holder.imageNews);
        holder.titleNews.setText(dataList.get(position).getTitleNews());
        holder.timeNews.setText(dataList.get(position).getTimeNews());

        String linkWeb = dataList.get(position).getLinkNews();

        // Sử dụng lambda expression thay cho anonymous class
        holder.itemNews.setOnClickListener(v -> {
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

    // Lớp ViewHolder như là một inner class trong NewsAdapter
    public static class MyDicoverViewHolder extends RecyclerView.ViewHolder {

        ImageView imageNews;
        TextView titleNews, timeNews;
        LinearLayout itemNews;

        public MyDicoverViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNews = itemView.findViewById(R.id.imageNews);
            titleNews = itemView.findViewById(R.id.titleNews);
            timeNews = itemView.findViewById(R.id.timeNews);
            itemNews = itemView.findViewById(R.id.itemNews);
        }
    }
}
