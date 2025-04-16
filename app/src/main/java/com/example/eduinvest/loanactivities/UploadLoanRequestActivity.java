package com.example.eduinvest.loanactivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.databinding.ActivityUploadLoanRequestBinding;
import com.example.eduinvest.databinding.DialogSucessBinding;
import com.example.eduinvest.models.LoanRequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class UploadLoanRequestActivity extends AppCompatActivity {
    private ActivityUploadLoanRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding
        binding = ActivityUploadLoanRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập dữ liệu nhận từ Intent (nếu có)
        setupIntentData();

        // Sự kiện khi nhấn nút gửi yêu cầu
        binding.sendButton.setOnClickListener(view -> uploadData());
    }

    /**
     * Phương thức thiết lập dữ liệu từ Intent gửi sang Activity này
     */
    @SuppressLint("SetTextI18n")
    private void setupIntentData() {
        String typeBank = getIntent().getStringExtra("typeBank");
        String nameBank = getIntent().getStringExtra("nameBank");
        String titleBank = getIntent().getStringExtra("titleBank");
        String limitBank = getIntent().getStringExtra("limitBank");
        String rateBank = getIntent().getStringExtra("rateBank");
        String loanPeriodBank = getIntent().getStringExtra("loanPeriodBank");

        if ("VAYUUDAI".equals(typeBank)) {
            // Gán giá trị cho các EditText liên quan đến hạn mức, lãi suất, thời gian vay
            binding.detailLimitBank.setText(limitBank);
            binding.detailRateBank.setText(rateBank);
            binding.detailLoanPeriodBank.setText(loanPeriodBank);
            binding.detailNoteBank.setText(nameBank + " - " + titleBank);

            // Vô hiệu hoá chỉnh sửa cho các trường dữ liệu này
            binding.detailLimitBank.setEnabled(false);
            binding.detailRateBank.setEnabled(false);
            binding.detailLoanPeriodBank.setEnabled(false);
            binding.detailNoteBank.setEnabled(false);

            // Gán nhãn cho các TextView hiển thị
            binding.txtRateBank.setText("Lãi suất");
            binding.txtLimitBank.setText("Hạn mức vay");
            binding.txtLoanPeriodBank.setText("Thời gian vay");
        }
    }

    /**
     * Phương thức kiểm tra các trường dữ liệu nhập vào
     */
    private boolean validateInput(String namePerson, String birthDate, String gender, String phoneNumber, String email, String limitBank, String rateBank, String loanPeriodBank) {
        if (namePerson.isEmpty()) return showError(binding.detailNamePerson, "Vui lòng nhập tên người vay");
        if (birthDate.isEmpty()) return showError(binding.detailBirthDate, "Vui lòng nhập ngày sinh");
        if (gender.isEmpty()) return showError(binding.detailGender, "Vui lòng nhập giới tính");
        if (phoneNumber.isEmpty()) return showError(binding.detailPhoneNumber, "Vui lòng nhập số điện thoại");
        if (email.isEmpty()) return showError(binding.detailEmail, "Vui lòng nhập email");
        if (limitBank.isEmpty()) return showError(binding.detailLimitBank, "Vui lòng nhập hạn mức vay");
        if (rateBank.isEmpty()) return showError(binding.detailRateBank, "Vui lòng nhập lãi suất");
        if (loanPeriodBank.isEmpty()) return showError(binding.detailLoanPeriodBank, "Vui lòng nhập thời gian vay");
        return true;
    }
    // Thong bao neu loi
    private boolean showError(android.widget.EditText field, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        field.requestFocus();
        return false;
    }

    /**
     * Phương thức tạo model dữ liệu yêu cầu vay dựa trên dữ liệu nhập vào và nhận từ Intent
     */
    private LoanRequestModel createDataModel(String typeBank, String namePerson, String birthDate, String gender, String phoneNumber, String email, String limitBank, String rateBank, String loanPeriodBank, String noteBank) {
        String nameBank = getIntent().getStringExtra("nameBank");
        String titleBank = getIntent().getStringExtra("titleBank");
        String imageBank = getIntent().getStringExtra("imageBank");
        String contactBank = getIntent().getStringExtra("contactBank");

        // Lấy UID của người dùng hiện tại từ Firebase Authentication
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        if ("VAYUUDAI".equals(typeBank)) {
            return new LoanRequestModel(
                    imageBank,
                    nameBank,
                    titleBank,
                    rateBank,
                    loanPeriodBank,
                    limitBank,
                    contactBank,
                    namePerson,
                    phoneNumber,
                    gender,
                    birthDate,
                    email,
                    noteBank,
                    "Gói Vay Ưu Đãi",
                    "DANG_DUYET",
                    userId
            );
        } else {
            return new LoanRequestModel(
                    "Null",
                    "Null",
                    "Null",
                    rateBank,
                    loanPeriodBank,
                    limitBank,
                    "Null",
                    namePerson,
                    phoneNumber,
                    gender,
                    birthDate,
                    email,
                    noteBank,
                    "Gói Vay Mong Muốn",
                    "DANG_DUYET",
                    userId
            );
        }
    }
    /*Phương thức xử lý gửi dữ liệu lên Firebase*/
    @SuppressLint("SetTextI18n")
    private void uploadData() {
        String namePerson = binding.detailNamePerson.getText().toString().trim();
        String birthDate = binding.detailBirthDate.getText().toString().trim();
        String gender = binding.detailGender.getText().toString().trim();
        String phoneNumber = binding.detailPhoneNumber.getText().toString().trim();
        String email = binding.detailEmail.getText().toString().trim();
        String limitBank = binding.detailLimitBank.getText().toString().trim();
        String rateBank = binding.detailRateBank.getText().toString().trim();
        String loanPeriodBank = binding.detailLoanPeriodBank.getText().toString().trim();
        String noteBank = binding.detailNoteBank.getText().toString().trim();
        String typeBank = getIntent().getStringExtra("typeBank");

        // Kiểm tra dữ liệu nhập vào
        if (!validateInput(namePerson, birthDate, gender, phoneNumber, email, limitBank, rateBank, loanPeriodBank)) {
            return;
        }

        // Tạo model dữ liệu từ thông tin đã nhập
        LoanRequestModel dataModel = createDataModel(typeBank, namePerson, birthDate, gender, phoneNumber, email, limitBank, rateBank, loanPeriodBank, noteBank);
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Gửi dữ liệu lên Firebase Realtime Database
        FirebaseDatabase.getInstance().getReference("LoanRequest").child(currentDate)
                .setValue(dataModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Tạo binding từ layout dialog
                        DialogSucessBinding dialogBinding = DialogSucessBinding.inflate(getLayoutInflater());
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setView(dialogBinding.getRoot())
                                .setCancelable(false)
                                .create();

                        dialog.show();

                        Handler handler = new Handler(Looper.getMainLooper());
                        Runnable goToManageLoan = () -> {
                            if (dialog.isShowing()) dialog.dismiss();
                            Intent intent = new Intent(this, ManageLoanActivities.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        };
                        handler.postDelayed(goToManageLoan, 2000);

                        dialogBinding.txtBack.setOnClickListener(v -> {
                            handler.removeCallbacks(goToManageLoan);
                            dialog.dismiss();
                            Intent intent = new Intent(this, ManageLoanActivities.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        });

                    } else {
                        Toast.makeText(this, "Thất Bại Trong Việc Gửi Yêu Cầu", Toast.LENGTH_SHORT).show();
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
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
