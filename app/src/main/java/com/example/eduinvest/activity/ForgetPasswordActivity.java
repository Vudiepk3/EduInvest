package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.constants.Base;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button btnForgotPasswordSubmit;
    private EditText titleEmailForgetPassword;
    private TextView tvSubmitMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();

        btnForgotPasswordSubmit = findViewById(R.id.btnForgotPasswordSubmit);
        titleEmailForgetPassword = findViewById(R.id.tilEmailForgetPassword);
        tvSubmitMsg = findViewById(R.id.tvSubmitMsg);
        btnForgotPasswordSubmit.setOnClickListener(v -> resetPassword());
        tvSubmitMsg.setOnClickListener(v -> openGmail());

    }

    // Kiểm tra tính hợp lệ của biểu mẫu
    private boolean validateForm(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            titleEmailForgetPassword.setError("Nhập địa chỉ email hợp lệ");
            return false;
        }
        return true;
    }

    // Đặt lại mật khẩu
    private void resetPassword() {
        String email = titleEmailForgetPassword.getText().toString();
        if (validateForm(email)) {
            // Hiển thị thanh tiến trình
            Base.showProgressBar(this);
            // Gửi email đặt lại mật khẩu
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        // Ẩn thanh tiến trình khi hoàn thành
                        Base.hideProgressBar(this);
                        if (task.isSuccessful()) {
                            // Đặt lại thành công
                            titleEmailForgetPassword.setVisibility(View.GONE);
                            tvSubmitMsg.setVisibility(View.VISIBLE);
                            btnForgotPasswordSubmit.setVisibility(View.GONE);
                        } else {
                            // Đặt lại không thành công
                            Base.showToast(this, "Không thể đặt lại mật khẩu. Thử lại sau.");
                        }
                    });
        }
    }
    // Phương thức mở Gmail với email của người dùng
    private void openGmail() {
        String email = titleEmailForgetPassword.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.setPackage("com.google.android.gm");

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Base.showToast(this, "Không tìm thấy ứng dụng Gmail");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đảm bảo đóng ProgressBar nếu Activity bị hủy
        Base.hideProgressBar(this);
    }
}
