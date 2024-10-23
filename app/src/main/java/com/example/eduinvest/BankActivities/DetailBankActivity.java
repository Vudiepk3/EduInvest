package com.example.eduinvest.BankActivities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.models.BankModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailBankActivity extends AppCompatActivity {
    TextView detailNameBank, detailTitleBank, detailRateBank, detailBrowseBank, detailLoanPeriodBank,
            detailLimitBank, detailMoneyBank, detailDescribeBank, detailCosditonBank, detailTypeBank, detailContactBank;
    ImageView detailImageBank;
    String key = "";

    String imageUrl = "";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bank);
        getID();

        // Hiển thị dialog tiến trình
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetailBankActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Bank"); // Khởi tạo databaseReference
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
        detailTitleBank = findViewById(R.id.detailTitleBank);
        detailRateBank = findViewById(R.id.detailRateBank);
        detailBrowseBank = findViewById(R.id.detailBrowseBank);
        detailLoanPeriodBank = findViewById(R.id.detailLoanPeriodBank);
        detailLimitBank = findViewById(R.id.detailLimitBank);
        detailMoneyBank = findViewById(R.id.detailMoneyBank);
        detailDescribeBank = findViewById(R.id.detailDescribeBank);
        detailCosditonBank = findViewById(R.id.detailCosditionBank);
    }

    // Phương thức đọc dữ liệu từ Firebase Realtime Database
    private void loadDataFromFirebase() {
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    BankModel bankModel = snapshot.getValue(BankModel.class);
                    if (bankModel != null) {
                        detailTitleBank.setText(bankModel.getTitleBank());
                        detailRateBank.setText(bankModel.getRateBank());
                        detailBrowseBank.setText(bankModel.getBrowseBank());
                        detailLoanPeriodBank.setText(bankModel.getLoanPeriodBank());
                        detailLimitBank.setText(bankModel.getLimitBank());
                        detailMoneyBank.setText(bankModel.getMoneyBank());
                        detailDescribeBank.setText(bankModel.getDescribleBank());
                        detailCosditonBank.setText(bankModel.getConditionBank());
                        String contact = bankModel.getContanctBank();
                        getContact(contact);
                    }
                } else {
                    // Xử lý trường hợp không tìm thấy dữ liệu với Key đã cho
                    Toast.makeText(DetailBankActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(DetailBankActivity.this, "Lỗi khi đọc dữ liệu", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getContact(String contactLink) {
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        try {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     sendEmailIntent(contactLink);
                }
            });
        } catch (Exception e) {
            Toast.makeText(DetailBankActivity.this, "Chúng tôi sẽ liên lại cho bạn sớm nhất", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailIntent(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Liên Hệ Vay");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(DetailBankActivity.this, "No email app found.", Toast.LENGTH_SHORT).show();
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
