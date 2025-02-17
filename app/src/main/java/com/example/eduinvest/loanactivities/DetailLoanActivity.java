package com.example.eduinvest.loanactivities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.databinding.ActivityDetailBankBinding;
import com.example.eduinvest.models.LoanModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLoanActivity extends AppCompatActivity {
    // Khai báo biến binding thay cho các biến view cũ
    private ActivityDetailBankBinding binding;
    String key = "";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding
        binding = ActivityDetailBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Vô hiệu hoá chỉnh sửa cho các TextView
        binding.detailNameBank.setEnabled(false);
        binding.detailTitleBank.setEnabled(false);
        binding.detailRateBank.setEnabled(false);
        binding.detailBrowseBank.setEnabled(false);
        binding.detailLoanPeriodBank.setEnabled(false);
        binding.detailLimitBank.setEnabled(false);
        binding.detailMoneyBank.setEnabled(false);
        binding.detailDescribeBank.setEnabled(false);
        binding.detailCosditionBank.setEnabled(false);

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailLoanActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Loan");

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            if (key != null) {
                loadDataFromFirebase();
                dialog.dismiss();
            } else {
                // Xử lý trường hợp không có Key
                Toast.makeText(this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // Phương thức đọc dữ liệu từ Firebase Realtime Database
    private void loadDataFromFirebase() {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LoanModel loanModel = snapshot.getValue(LoanModel.class);
                    if (loanModel != null) {
                        binding.detailNameBank.setText(loanModel.getNameBank());
                        binding.detailTitleBank.setText(loanModel.getTitleBank());
                        binding.detailRateBank.setText(loanModel.getRateBank());
                        binding.detailBrowseBank.setText(loanModel.getBrowseBank());
                        binding.detailLoanPeriodBank.setText(loanModel.getLoanPeriodBank());
                        binding.detailLimitBank.setText(loanModel.getLimitBank());
                        binding.detailMoneyBank.setText(loanModel.getMoneyBank());
                        binding.detailDescribeBank.setText(loanModel.getDescribleBank());
                        binding.detailCosditionBank.setText(loanModel.getConditionBank());
                        String contact = loanModel.getContanctBank();
                        String imageBank = loanModel.getImageBank();
                        getContact(contact, imageBank);
                    }
                } else {
                    // Xử lý trường hợp không tìm thấy dữ liệu với Key đã cho
                    Toast.makeText(DetailLoanActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(DetailLoanActivity.this, "Lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getContact(String contactLink, String imageBank) {
        // Thiết lập sự kiện cho nút button1 (chuyển sang UploadLoanRequestActivity)
        try {
            binding.button1.setOnClickListener(v -> {
                Intent intent = new Intent(DetailLoanActivity.this, UploadLoanRequestActivity.class);
                intent.putExtra("typeBank", "VAYUUDAI");
                intent.putExtra("imageBank", imageBank);  // Hình ảnh của ngân hàng
                intent.putExtra("contactBank", contactLink);  // Liên hệ ngân hàng
                intent.putExtra("titleBank", binding.detailTitleBank.getText().toString());  // Tiêu đề ngân hàng
                intent.putExtra("nameBank", binding.detailNameBank.getText().toString());  // Tên ngân hàng
                intent.putExtra("rateBank", binding.detailRateBank.getText().toString());  // Lãi suất của ngân hàng
                intent.putExtra("browseBank", binding.detailBrowseBank.getText().toString());  // Địa chỉ website của ngân hàng
                intent.putExtra("loanPeriodBank", binding.detailLoanPeriodBank.getText().toString());  // Thời gian vay
                intent.putExtra("limitBank", binding.detailLimitBank.getText().toString());  // Hạn mức vay
                startActivity(intent);
            });
        } catch (Exception e) {
            Toast.makeText(DetailLoanActivity.this, "Chúng tôi sẽ liên lại cho bạn sớm nhất", Toast.LENGTH_SHORT).show();
        }

        // Thiết lập sự kiện cho nút button2 (gửi email)
        try {
            binding.button2.setOnClickListener(v -> sendEmailIntent(contactLink));
        } catch (Exception e) {
            Toast.makeText(DetailLoanActivity.this, "Chúng tôi sẽ liên lại cho bạn sớm nhất", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailIntent(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Giải Đáp Thắc Mắc Gói Vay"
                + binding.detailTitleBank.getText().toString() + " của "
                + binding.detailNameBank.getText().toString());
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(DetailLoanActivity.this, ".", Toast.LENGTH_SHORT).show();
        }
    }

    // Tải lại dữ liệu từ Firebase khi Activity trở lại hoạt động
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

}
