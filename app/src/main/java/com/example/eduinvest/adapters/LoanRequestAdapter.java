package com.example.eduinvest.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.LoanActivities.DetailLoanRequestActivity;
import com.example.eduinvest.R;
import com.example.eduinvest.models.LoanRequestModel;
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
        holder.time.setText("Thời Gian Vay(Tháng)");
        //holder.typeBank.setText(LoanRequestModel.getTypeBank());
        String status = LoanRequestModel.getStatus();
        holder.statusBank.setText(status);
       // Kiểm tra nếu trạng thái là "Từ Chối" thì đổi màu thành đỏ
        if ("TU_CHOI".equals(status)) {
            holder.statusBank.setText("Từ Chối");
            holder.statusBank.setTextColor(Color.RED);
        } else if ("DANG_DUYET".equals(status)) {
            holder.statusBank.setText("Đang Gửi Yêu Cầu");
            holder.statusBank.setTextColor(ContextCompat.getColor(context, R.color.orange));
        }
        else if ("DA_DUYET".equals(status)) {
            holder.statusBank.setText("Đã duyệt.");
            holder.statusBank.setTextColor(Color.GREEN);
        }
        else {
            // Màu mặc định nếu không phải là "Từ Chối" và "Đang Gửi Yêu Cầu"
            holder.statusBank.setTextColor(Color.GREEN);
        }

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
        if (holder.itemBank != null) {
            holder.itemBank.setOnLongClickListener(view -> {
                // Tạo dialog xác nhận xóa
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa mục này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Thực hiện xóa khỏi Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("LoanRequest")
                                    .child(LoanRequestModel.getKey());

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
        TextView titleBank, nameBank, browseBank, rateBank, statusBank,time;
        CardView itemBank;

        public MyLoanRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần giao diện
            imageBank = itemView.findViewById(R.id.imageBank);
            titleBank = itemView.findViewById(R.id.titleBank);
            nameBank = itemView.findViewById(R.id.nameBank);
            browseBank = itemView.findViewById(R.id.browseBank);
            rateBank = itemView.findViewById(R.id.rateBank);
            time = itemView.findViewById(R.id.time);
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
