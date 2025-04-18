package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.databinding.ItemBankBinding;

import com.example.eduinvest.loanactivities.DetailLoanActivity;
import com.example.eduinvest.models.LoanModel;
import com.example.eduinvest.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyBankViewHolder> {

    private final Context context;
    private List<LoanModel> dataList;

    public LoanAdapter(Context context, List<LoanModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBankBinding binding = ItemBankBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyBankViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBankViewHolder holder, int position) {
        LoanModel loanModel = dataList.get(position);

        Glide.with(context)
                .load(loanModel.getImageBank())
                .into(holder.binding.imageBank);
        holder.binding.nameBank.setText(loanModel.getNameBank());
        holder.binding.rateBank.setText(loanModel.getRateBank());
        holder.binding.timeBank.setText(loanModel.getBrowseBank());

        // Thiết lập sự kiện click cho item
        holder.binding.linearLayoutBank.setOnClickListener(view -> {
            Common.logEvent(context, "click_item_bank" + loanModel.getNameBank()+"_"+loanModel.getKey());
            Intent intent = new Intent(context, DetailLoanActivity.class);
            intent.putExtra("Key", loanModel.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<LoanModel> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    public static class MyBankViewHolder extends RecyclerView.ViewHolder {
        final ItemBankBinding binding;

        public MyBankViewHolder(@NonNull ItemBankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
