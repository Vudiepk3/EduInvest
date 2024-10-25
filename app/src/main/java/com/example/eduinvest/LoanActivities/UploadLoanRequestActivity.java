package com.example.eduinvest.LoanActivities;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;

import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadLoanRequestActivity extends AppCompatActivity {

    Button sendButton;
    EditText detailNamePerson, detailBirthDate, detailGender, detailPhoneNumber, detailEmail, detailLimitBank, detailRateBank, detailLoanPeriodBank,detailNoteBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_loan_request);
         detailNamePerson = findViewById(R.id.detailNamePerson);
         detailBirthDate =  findViewById(R.id.detailBirthDate);
         detailGender =  findViewById(R.id.detailGender);
         detailPhoneNumber =  findViewById(R.id.detailPhoneNumber);
         detailEmail =  findViewById(R.id.detailEmail);
         detailLimitBank =  findViewById(R.id.detailLimitBank);
         detailRateBank =  findViewById(R.id.detailRateBank);
         detailLoanPeriodBank =  findViewById(R.id.detailLoanPeriodBank);
         detailNoteBank =  findViewById(R.id.detailNoteBank);

         sendButton =  findViewById(R.id.sendButton);



        // Set click listener for saving data
        sendButton.setOnClickListener(view -> {
                uploadData();
        });
    }



    // Upload data to Firebase Realtime Database
    public void uploadData() {
        String namePerson = detailNamePerson.getText().toString();
        String birthDate = detailBirthDate.getText().toString();
        String gender = detailGender.getText().toString();
        String phoneNumber = detailPhoneNumber.getText().toString();
        String email = detailEmail.getText().toString();
        String limitBank = detailLimitBank.getText().toString();
        String rateBank = detailRateBank.getText().toString();
        String loanPeriodBank = detailLoanPeriodBank.getText().toString();
        String noteBank = detailNoteBank.getText().toString();

        // Kiểm tra từng trường thông tin
        if (namePerson.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập tên người vay", Toast.LENGTH_SHORT).show();
            detailNamePerson.requestFocus(); // Đưa con trỏ tới ô tên người vay
            return;
        }
        if (birthDate.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập ngày sinh", Toast.LENGTH_SHORT).show();
            detailBirthDate.requestFocus(); // Đưa con trỏ tới ô ngày sinh
            return;
        }
        if (gender.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập giới tính", Toast.LENGTH_SHORT).show();
            detailGender.requestFocus(); // Đưa con trỏ tới ô giới tính
            return;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            detailPhoneNumber.requestFocus(); // Đưa con trỏ tới ô số điện thoại
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            detailEmail.requestFocus(); // Đưa con trỏ tới ô email
            return;
        }
        if (limitBank.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập hạn mức vay", Toast.LENGTH_SHORT).show();
            detailLimitBank.requestFocus(); // Đưa con trỏ tới ô hạn mức vay
            return;
        }
        if (rateBank.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập lãi suất", Toast.LENGTH_SHORT).show();
            detailRateBank.requestFocus(); // Đưa con trỏ tới ô lãi suất
            return;
        }
        if (loanPeriodBank.isEmpty()) {
            Toast.makeText(UploadLoanRequestActivity.this, "Vui lòng nhập thời gian vay", Toast.LENGTH_SHORT).show();
            detailLoanPeriodBank.requestFocus(); // Đưa con trỏ tới ô thời gian vay
            return;
        }

        // Nếu tất cả các trường đều đã được nhập, thực hiện gửi dữ liệu
        LoanRequestModel dataClass = new LoanRequestModel(rateBank, loanPeriodBank, limitBank, namePerson, phoneNumber, gender, birthDate, email, noteBank, "Đang Gửi Yêu Cầu");
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("LoanRequest").child(currentDate)
                .setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UploadLoanRequestActivity.this, "Lưu Thành Công", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2000);
                    } else {
                        Toast.makeText(UploadLoanRequestActivity.this, "Thất Bại Trong Việc Lưu", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UploadLoanRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
