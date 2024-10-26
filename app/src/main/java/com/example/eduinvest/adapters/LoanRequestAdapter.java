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
import com.example.eduinvest.LoanActivities.DetailLoanActivity;
import com.example.eduinvest.LoanActivities.DetailLoanRequestActivity;
import com.example.eduinvest.R;
import com.example.eduinvest.models.LoanRequestModel;

import java.util.ArrayList;
import java.util.List;

public class LoanRequestAdapter extends RecyclerView.Adapter<LoanRequestAdapter.MyLoanRequestViewHolder> {

    private final Context context;
    private List<LoanRequestModel> dataList;

    public LoanRequestAdapter(Context context, List<LoanRequestModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyLoanRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new MyLoanRequestViewHolder(view);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyLoanRequestViewHolder holder, int position) {
        LoanRequestModel LoanRequestModel = dataList.get(position);
        if(!LoanRequestModel.getImageBank().equals("Null")){
            Glide.with(context).load(LoanRequestModel.getImageBank()).into(holder.imageBank);
        }
        holder.titleBank.setText(LoanRequestModel.getNamePerson());
        holder.titleBank.setText(LoanRequestModel.getNamePerson() + " - " + LoanRequestModel.getGender());
        holder.nameBank.setText(LoanRequestModel.getPhoneNumber()+" - " + LoanRequestModel.getEmail());
        holder.browseBank.setText(LoanRequestModel.getLoanPeriodBank());
        holder.rateBank.setText(LoanRequestModel.getRateBank());
        //holder.typeBank.setText(LoanRequestModel.getTypeBank());
        holder.statusBank.setText(LoanRequestModel.getStatus());

        // Kiểm tra null và thiết lập OnClickListener
        if (holder.itemBank != null) {
            holder.itemBank.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailLoanRequestActivity.class);
                intent.putExtra("key", LoanRequestModel.getKey());
                context.startActivity(intent);
            });
        } else {
            Log.e("LoanRequestAdapter", "itemBank is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<LoanRequestModel> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

    // Class ViewHolder cần được public static để đảm bảo truy cập đúng
    public static class MyLoanRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBank;
        TextView titleBank, nameBank, browseBank, rateBank, statusBank,typeBank;
        CardView itemBank;

        public MyLoanRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần giao diện
            imageBank = itemView.findViewById(R.id.imageBank);
            titleBank = itemView.findViewById(R.id.titleBank);
            nameBank = itemView.findViewById(R.id.nameBank);
            browseBank = itemView.findViewById(R.id.browseBank);
            rateBank = itemView.findViewById(R.id.rateBank);
            statusBank = itemView.findViewById(R.id.statusBank);
            //typeBank = itemView.findViewById(R.id.typeBank);
            itemBank = itemView.findViewById(R.id.itemBank);  // CardView

            // Kiểm tra log để đảm bảo không null
            if (itemBank == null) {
                Log.e("MyLoanRequestViewHolder", "itemBank is null");
            }
        }
    }
}
