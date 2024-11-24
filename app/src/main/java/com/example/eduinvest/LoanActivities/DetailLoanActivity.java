package com.example.eduinvest.LoanActivities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.models.LoanModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLoanActivity extends AppCompatActivity {
    TextView detailNameBank, detailTitleBank, detailRateBank, detailBrowseBank, detailLoanPeriodBank,
            detailLimitBank, detailMoneyBank, detailDescribeBank, detailCosditonBank;
    String key = "";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bank);
        getID();

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailLoanActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Loan"); // Khởi tạo databaseReference
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

    // Phương thức lấy ID
    private void getID() {
        // Ánh xạ các thành phần giao diện
        detailNameBank = findViewById(R.id.detailNameBank);
        detailTitleBank = findViewById(R.id.detailTitleBank);
        detailRateBank = findViewById(R.id.detailRateBank);
        detailBrowseBank = findViewById(R.id.detailBrowseBank);
        detailLoanPeriodBank = findViewById(R.id.detailLoanPeriodBank);
        detailLimitBank = findViewById(R.id.detailLimitBank);
        detailMoneyBank = findViewById(R.id.detailMoneyBank);
        detailDescribeBank = findViewById(R.id.detailDescribeBank);
        detailCosditonBank = findViewById(R.id.detailCosditionBank);

        detailNameBank.setEnabled(false);
        detailTitleBank.setEnabled(false);
        detailRateBank.setEnabled(false);
        detailBrowseBank.setEnabled(false);
        detailLoanPeriodBank.setEnabled(false);
        detailLimitBank.setEnabled(false);
        detailMoneyBank.setEnabled(false);
        detailDescribeBank.setEnabled(false);
        detailCosditonBank.setEnabled(false);
    }

    // Phương thức đọc dữ liệu từ Firebase Realtime Database
    private void loadDataFromFirebase() {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LoanModel loanModel = snapshot.getValue(LoanModel.class);
                    if (loanModel != null) {
                        detailNameBank.setText(loanModel.getNameBank());
                        detailTitleBank.setText(loanModel.getTitleBank());
                        detailRateBank.setText(loanModel.getRateBank());
                        detailBrowseBank.setText(loanModel.getBrowseBank());
                        detailLoanPeriodBank.setText(loanModel.getLoanPeriodBank());
                        detailLimitBank.setText(loanModel.getLimitBank());
                        detailMoneyBank.setText(loanModel.getMoneyBank());
                        detailDescribeBank.setText(loanModel.getDescribleBank());
                        detailCosditonBank.setText(loanModel.getConditionBank());
                        String contact = loanModel.getContanctBank();
                        String imageBank = loanModel.getImageBank();
                        getContact(contact,imageBank);
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

    private void getContact(String contactLink,String imageBank) {
        Button button1 = findViewById(R.id.button1);
        try {
            // Thay thế anonymous class bằng lambda expression cho ngắn gọn
            button1.setOnClickListener(v -> {
                // Tạo một Intent để chuyển sang Activity UploadLoanRequestActivity
                Intent intent = new Intent(DetailLoanActivity.this, UploadLoanRequestActivity.class);

                // Thêm các dữ liệu cần thiết vào Intent bằng cách sử dụng putExtra
                intent.putExtra("typeBank", "VAYUUDAI");
                intent.putExtra("imageBank", imageBank);  // Hình ảnh của ngân hàng
                intent.putExtra("contactBank", contactLink);  // Liên hệ ngân hàng
                intent.putExtra("titleBank", detailTitleBank.getText().toString());  // Tiêu đề ngân hàng
                intent.putExtra("nameBank", detailNameBank.getText().toString());  // Tên ngân hàng
                intent.putExtra("rateBank", detailRateBank.getText().toString());  // Lãi suất của ngân hàng
                intent.putExtra("browseBank", detailBrowseBank.getText().toString());  // Địa chỉ website của ngân hàng
                intent.putExtra("loanPeriodBank", detailLoanPeriodBank.getText().toString());  // Thời gian vay
                intent.putExtra("limitBank", detailLimitBank.getText().toString());  // Hạn mức vay

                // Bắt đầu Activity mới
                startActivity(intent);
            });
        } catch (Exception e) {
            // Xử lý lỗi khi có sự cố và hiển thị thông báo
            Toast.makeText(DetailLoanActivity.this, "Chúng tôi sẽ liên lại cho bạn sớm nhất", Toast.LENGTH_SHORT).show();
        }


        Button button2 = findViewById(R.id.button2);
        try {
            // Thay thế anonymous class bằng lambda expression cho ngắn gọn
            button2.setOnClickListener(v -> sendEmailIntent(contactLink));
        } catch (Exception e) {
            // Xử lý lỗi khi có sự cố và hiển thị thông báo
            Toast.makeText(DetailLoanActivity.this, "Chúng tôi sẽ liên lại cho bạn sớm nhất", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailIntent(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Giải Đáp Thắc Mắc Gói Vay" + detailTitleBank.getText().toString() +" của "+detailNameBank.getText().toString());
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(DetailLoanActivity.this, "No email app found.", Toast.LENGTH_SHORT).show();
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
}
