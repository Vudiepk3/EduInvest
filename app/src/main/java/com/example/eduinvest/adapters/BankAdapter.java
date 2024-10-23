package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.BankActivities.DetailBankActivity;
import com.example.eduinvest.R;
import com.example.eduinvest.models.BankModel;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyBankViewHolder> {

    private final Context context;
    private List<BankModel> dataList;

    public BankAdapter(Context context, List<BankModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new MyBankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBankViewHolder holder, int position) {
        BankModel bankModel = dataList.get(position);

        // Load dữ liệu vào các thành phần giao diện
        Glide.with(context).load(bankModel.getImageBank()).into(holder.imageBank);
        holder.titleBank.setText(bankModel.getTitleBank());
        holder.nameBank.setText(bankModel.getNameBank());
        holder.browseBank.setText(bankModel.getBrowseBank());
        holder.rateBank.setText(bankModel.getRateBank());

        // Kiểm tra null và thiết lập OnClickListener
        if (holder.itemBank != null) {
            holder.itemBank.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailBankActivity.class);
                intent.putExtra("Key", bankModel.getKey());
                context.startActivity(intent);
            });
        } else {
            Log.e("BankAdapter", "itemBank is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<BankModel> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    // Class ViewHolder cần được public static để đảm bảo truy cập đúng
    public static class MyBankViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBank;
        TextView titleBank, nameBank, browseBank, rateBank;
        CardView itemBank;

        public MyBankViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần giao diện
            imageBank = itemView.findViewById(R.id.imageBank);
            titleBank = itemView.findViewById(R.id.titleBank);
            nameBank = itemView.findViewById(R.id.nameBank);
            browseBank = itemView.findViewById(R.id.browseBank);
            rateBank = itemView.findViewById(R.id.rateBank);
            itemBank = itemView.findViewById(R.id.itemBank);  // CardView

            // Kiểm tra log để đảm bảo không null
            if (itemBank == null) {
                Log.e("MyBankViewHolder", "itemBank is null");
            }
        }
    }
}
