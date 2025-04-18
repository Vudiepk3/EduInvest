package com.example.eduinvest.loanactivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.databinding.ActivityDetailLoanRequestBinding;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLoanRequestActivity extends AppCompatActivity {
    // Sử dụng View Binding thay cho findViewById
    private ActivityDetailLoanRequestBinding binding;
    String key = "";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailLoanRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailLoanRequestActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("LoanRequest");

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
            if (key != null) {
                loadDataFromFirebase();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // Phương thức đọc dữ liệu từ Firebase Realtime Database
    private void loadDataFromFirebase() {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LoanRequestModel loanModel = snapshot.getValue(LoanRequestModel.class);
                    if (loanModel != null) {
                        binding.detailInformationPersonBank.setText(
                                "Họ Và Tên: " + loanModel.getNamePerson() +
                                        "\nGiới Tính: " + loanModel.getGender() +
                                        "\nNgày Sinh: " + loanModel.getBirthDate() +
                                        "\nSố Điện Thoại: " + loanModel.getPhoneNumber() +
                                        "\nEmail: " + loanModel.getEmail()
                        );
                        if (loanModel.getTitleBank().equals("Null") && loanModel.getNameBank().equals("Null")) {
                            binding.detailInformationLoanRequestBank.setText(
                                    "Lãi Suất: " + loanModel.getRateBank() +
                                            "\nThời Gian Vay: " + loanModel.getLoanPeriodBank() +
                                            "\nGiới Hạn: " + loanModel.getLimitBank()
                            );

                        }else {
                            binding.detailInformationLoanRequestBank.setText(
                                    "Tên Ngân Hàng: " + loanModel.getNameBank() +
                                            "\nTên Gói Vay: " + loanModel.getTitleBank() +
                                            "\nLãi Suất: " + loanModel.getRateBank() +
                                            "\nThời Gian Vay: " + loanModel.getLoanPeriodBank() +
                                            "\nGiới Hạn: " + loanModel.getLimitBank()
                            );
                            if(loanModel.getLinkData().isEmpty()){
                                binding.detailPersonalAchievements.setText("Không có dữ liệu");
                            }else{
                                binding.detailPersonalAchievements.setText(loanModel.getLinkData());
                            }
                        }
                        switch (loanModel.getStatus()) {
                            case "DA_DUYET":
                                binding.detailStatusLoanRequestBank.setText("Đã duyệt. Sẽ có nhân viên liên hệ bạn");
                                binding.imgStatus.setBackgroundResource(R.drawable.ic_confirm);
                                break;
                            case "DANG_DUYET":
                                binding.detailStatusLoanRequestBank.setText("Chúng tôi đang xử lý yêu cầu.");
                                binding.imgStatus.setBackgroundResource(R.drawable.ic_processing);
                                break;
                            case "TU_CHOI":
                                binding.detailStatusLoanRequestBank.setText("Yêu cầu của bạn không được chấp thuận. Bạn có thể tạo yêu cầu mới để tiếp tục.");
                                binding.imgStatus.setBackgroundResource(R.drawable.ic_rejected);
                                break;
                        }
                    }
                } else {
                    Toast.makeText(DetailLoanRequestActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailLoanRequestActivity.this, "Lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    // Tải lại dữ liệu khi Activity trở lại hoạt động
    @Override
    protected void onResume() {
        super.onResume();
        if (key != null) {
            loadDataFromFirebase();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
