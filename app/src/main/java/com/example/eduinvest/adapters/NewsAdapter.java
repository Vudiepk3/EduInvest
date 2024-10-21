package com.example.eduinvest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.models.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<MyDicoverViewHolder> {

    private final Context context;
    private List<NewsModel> dataList;

    public NewsAdapter (Context context, List<NewsModel> dataList) {
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

        String linkweb = dataList.get(position).getLinkNews();

        holder.itemnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkweb));
                    context.startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(context, "Thông tin sẽ được cập nhất đến bạn", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<NewsModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyDicoverViewHolder extends RecyclerView.ViewHolder{

    ImageView imageNews;
    TextView titleNews,timeNews,linkNews,typeNews;
    CardView itemnews;

    public MyDicoverViewHolder(@NonNull View itemView) {
        super(itemView);
        imageNews = itemView.findViewById(R.id.imageNews);
        titleNews = itemView.findViewById(R.id.titleNews);
        timeNews = itemView.findViewById(R.id.timeNews);
        itemnews = itemView.findViewById(R.id.itemNews);



    }
}