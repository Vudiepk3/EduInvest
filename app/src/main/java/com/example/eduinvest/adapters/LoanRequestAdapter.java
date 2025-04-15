package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.databinding.ItemBankBinding;
import com.example.eduinvest.loanactivities.DetailLoanRequestActivity;
import com.example.eduinvest.models.LoanRequestModel;
import com.example.eduinvest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        ItemBankBinding binding = ItemBankBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyLoanRequestViewHolder(binding);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyLoanRequestViewHolder holder, int position) {
        LoanRequestModel loanRequestModel = dataList.get(position);

        // Load ảnh nếu không bằng chuỗi "Null"
        if (!loanRequestModel.getImageBank().equals("Null")) {
            Glide.with(context).load(loanRequestModel.getImageBank()).into(holder.binding.imageBank);
        }

        // Thiết lập dữ liệu cho các thành phần giao diện
        holder.binding.nameBank.setText(loanRequestModel.getPhoneNumber() + " - " + loanRequestModel.getEmail());
        holder.binding.rateBank.setText("%"+loanRequestModel.getRateBank());
        holder.binding.timeBank.setText(loanRequestModel.getRateBank());

        // Thiết lập trạng thái
        String status = loanRequestModel.getStatus();
        holder.binding.statusBank.setVisibility(View.VISIBLE);
        holder.binding.statusBank.setText(status);
        if ("TU_CHOI".equals(status)) {
            holder.binding.statusBank.setText("Từ Chối");
            holder.binding.statusBank.setTextColor(Color.RED);
        } else if ("DANG_DUYET".equals(status)) {
            holder.binding.statusBank.setText("Đang Gửi Yêu Cầu");
            holder.binding.statusBank.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else if ("DA_DUYET".equals(status)) {
            holder.binding.statusBank.setText("Đã duyệt.");
            holder.binding.statusBank.setTextColor(Color.GREEN);
        } else {
            // Màu mặc định nếu không thuộc các trạng thái trên
            holder.binding.statusBank.setTextColor(Color.GREEN);
        }

        // Xử lý sự kiện click cho item
        holder.binding.itemBank.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailLoanRequestActivity.class);
            intent.putExtra("key", loanRequestModel.getKey());
            context.startActivity(intent);
        });

        // Xử lý sự kiện long click để xóa item
        holder.binding.itemBank.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa mục này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference("LoanRequest")
                                .child(loanRequestModel.getKey());

                        databaseReference.removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });
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

    public static class MyLoanRequestViewHolder extends RecyclerView.ViewHolder {
        final ItemBankBinding binding;

        public MyLoanRequestViewHolder(@NonNull ItemBankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
