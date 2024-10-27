package com.example.eduinvest.LoanActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadLoanRequestActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button sendButton;
    private EditText detailNamePerson, detailBirthDate, detailGender, detailPhoneNumber, detailEmail, detailLimitBank, detailRateBank, detailLoanPeriodBank, detailNoteBank;
    private TextView txtLimitBank;
    private TextView txtRateBank;
    private TextView txtLoanPeriodBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_loan_request);

        initializeViews();
        setupIntentData();

        sendButton.setOnClickListener(view -> uploadData());
    }

    private void initializeViews() {
        detailNamePerson = findViewById(R.id.detailNamePerson);
        detailBirthDate = findViewById(R.id.detailBirthDate);
        detailGender = findViewById(R.id.detailGender);
        detailPhoneNumber = findViewById(R.id.detailPhoneNumber);
        detailEmail = findViewById(R.id.detailEmail);
        detailLimitBank = findViewById(R.id.detailLimitBank);
        detailRateBank = findViewById(R.id.detailRateBank);
        detailLoanPeriodBank = findViewById(R.id.detailLoanPeriodBank);
        detailNoteBank = findViewById(R.id.detailNoteBank);
        TextView txtNoteBank = findViewById(R.id.txtNoteBank);
        txtLimitBank = findViewById(R.id.txtLimitBank);
        txtRateBank = findViewById(R.id.txtRateBank);
        txtLoanPeriodBank = findViewById(R.id.txtLoanPeriodBank);
        sendButton = findViewById(R.id.sendButton);
    }

    @SuppressLint("SetTextI18n")
    private void setupIntentData() {
        String typeBank = getIntent().getStringExtra("typeBank");
        String nameBank = getIntent().getStringExtra("nameBank");
        String titleBank = getIntent().getStringExtra("titleBank");
        String limitBank = getIntent().getStringExtra("limitBank");
        String rateBank = getIntent().getStringExtra("rateBank");
        String loanPeriodBank = getIntent().getStringExtra("loanPeriodBank");

        if ("VAYUUDAI".equals(typeBank)) {
            detailLimitBank.setText(limitBank);
            detailRateBank.setText(rateBank);
            detailLoanPeriodBank.setText(loanPeriodBank);
            detailNoteBank.setText(nameBank + " - " + titleBank);

            // Disable editing for promotional loan fields
            detailLimitBank.setEnabled(false);
            detailRateBank.setEnabled(false);
            detailLoanPeriodBank.setEnabled(false);
            detailNoteBank.setEnabled(false);
            txtRateBank.setText("Lãi suất");
            txtLimitBank.setText("Hạn mức vay");
            txtLoanPeriodBank.setText("Thời gian vay");
        }
    }

    @SuppressLint("SetTextI18n")
    private void uploadData() {
        String namePerson = detailNamePerson.getText().toString();
        String birthDate = detailBirthDate.getText().toString();
        String gender = detailGender.getText().toString();
        String phoneNumber = detailPhoneNumber.getText().toString();
        String email = detailEmail.getText().toString();
        String limitBank = detailLimitBank.getText().toString();
        String rateBank = detailRateBank.getText().toString();
        String loanPeriodBank = detailLoanPeriodBank.getText().toString();
        String noteBank = detailNoteBank.getText().toString();
        String typeBank = getIntent().getStringExtra("typeBank");

        if (!validateInput(namePerson, birthDate, gender, phoneNumber, email, limitBank, rateBank, loanPeriodBank)) {
            return;
        }

        LoanRequestModel dataModel = createDataModel(typeBank, namePerson, birthDate, gender, phoneNumber, email, limitBank, rateBank, loanPeriodBank, noteBank);
        saveDataToFirebase(dataModel);
    }

    private boolean validateInput(String namePerson, String birthDate, String gender, String phoneNumber, String email, String limitBank, String rateBank, String loanPeriodBank) {
        if (namePerson.isEmpty()) return showError(detailNamePerson, "Vui lòng nhập tên người vay");
        if (birthDate.isEmpty()) return showError(detailBirthDate, "Vui lòng nhập ngày sinh");
        if (gender.isEmpty()) return showError(detailGender, "Vui lòng nhập giới tính");
        if (phoneNumber.isEmpty()) return showError(detailPhoneNumber, "Vui lòng nhập số điện thoại");
        if (email.isEmpty()) return showError(detailEmail, "Vui lòng nhập email");
        if (limitBank.isEmpty()) return showError(detailLimitBank, "Vui lòng nhập hạn mức vay");
        if (rateBank.isEmpty()) return showError(detailRateBank, "Vui lòng nhập lãi suất");
        if (loanPeriodBank.isEmpty()) return showError(detailLoanPeriodBank, "Vui lòng nhập thời gian vay");
        return true;
    }

    private boolean showError(EditText field, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        field.requestFocus();
        return false;
    }

    private LoanRequestModel createDataModel(String typeBank, String namePerson, String birthDate, String gender, String phoneNumber, String email, String limitBank, String rateBank, String loanPeriodBank, String noteBank) {
        String nameBank = getIntent().getStringExtra("nameBank");
        String titleBank = getIntent().getStringExtra("titleBank");
        String imageBank = getIntent().getStringExtra("imageBank");
        String contactBank = getIntent().getStringExtra("contactBank");

        // Lấy User UID từ Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if ("VAYUUDAI".equals(typeBank)) {
            return new LoanRequestModel(imageBank,nameBank, titleBank, rateBank, loanPeriodBank, limitBank,contactBank, namePerson, phoneNumber, gender, birthDate, email, noteBank, "Gói Vay Ưu Đãi", "DANG_DUYET", userId);
        } else {
            return new LoanRequestModel("Null","Null", "Null", rateBank, loanPeriodBank, limitBank,"Null", namePerson, phoneNumber, gender, birthDate, email, noteBank, "Gói Vay Mong Muốn", "DANG_DUYET", userId);
        }
    }
    private void saveDataToFirebase(LoanRequestModel dataModel) {
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("LoanRequest").child(currentDate)
                .setValue(dataModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Gửi Yêu Cầu Thành Công", Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            finish(); // Kết thúc Activity hiện tại sau khi chuyển
                        }, 2000);
                    } else {
                        Toast.makeText(this, "Thất Bại Trong Việc Gửi Yêu Cầu", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
