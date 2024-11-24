package com.example.eduinvest.LoanActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLoanRequestActivity extends AppCompatActivity {
    TextView detailInformationPersonBank,detailInformationLoanRequestBank,detailStatusLoanRequestBank;
    String key = "";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_loan_request);
        getID();

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailLoanRequestActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("LoanRequest"); // Khởi tạo databaseReference
        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
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
       detailInformationPersonBank = findViewById(R.id.detailInformationPersonBank);
       detailInformationLoanRequestBank = findViewById(R.id.detailInformationLoanRequestBank);
       detailStatusLoanRequestBank = findViewById(R.id.detailStatusLoanRequestBank);
    }

    // Phương thức đọc dữ liệu từ Firebase Realtime Database
    private void loadDataFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("LoanRequest"); // Khởi tạo databaseReference
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    LoanRequestModel loanModel = snapshot.getValue(LoanRequestModel.class);
                    if (loanModel != null) {
                        detailInformationPersonBank.setText("Họ Và Tên:"+loanModel.getNamePerson()
                                                            +"\nGiới Tính:"+loanModel.getGender()
                                                            +"\nNgày Sinh:"+loanModel.getBirthDate()
                                                            +"\nSố Điện Thoại:"+loanModel.getPhoneNumber()
                                                            +"\nEmail:"+loanModel.getEmail());
                        if(loanModel.getTitleBank().equals("Null") && loanModel.getNameBank().equals("Null")){
                            detailInformationLoanRequestBank.setText("Lãi Suất:"+loanModel.getRateBank()
                                    +"\nThời Gian Vay:"+loanModel.getLoanPeriodBank()
                                    +"\nGiới Hạn:"+loanModel.getLimitBank());
                        }else{
                            detailInformationLoanRequestBank.setText("Tên Ngân Hàng:"+loanModel.getNameBank()
                                    +"\nTên Gói Vay:"+loanModel.getTitleBank()
                                     +"\nLãi Suất:"+loanModel.getRateBank()
                                    +"\nThời Gian Vay:"+loanModel.getLoanPeriodBank()
                                    +"\nGiới Hạn:"+loanModel.getLimitBank());
                        }
                        switch (loanModel.getStatus()) {
                            case "DA_DUYET":
                                detailStatusLoanRequestBank.setText("Đã duyệt.Sẽ có nhân viên liên hệ bạn");
                                break;
                            case "DANG_DUYET":
                                detailStatusLoanRequestBank.setText("Đang duyệt yêu cầu.Chúng tôi sẽ xử lý nhanh nhất.");
                                break;
                            case "TU_CHOI":
                                detailStatusLoanRequestBank.setText("Từ chối.Yêu  cầu của bạn không được chấp thuận.Bạn có thể tạo yêu cầu mới để tiếp tục.");
                                break;
                        }
                    }
                } else {
                    // Xử lý trường hợp không tìm thấy dữ liệu với Key đã cho
                    Toast.makeText(DetailLoanRequestActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(DetailLoanRequestActivity.this, "Lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
