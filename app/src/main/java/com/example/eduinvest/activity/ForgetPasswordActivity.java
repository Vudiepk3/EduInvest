package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.example.eduinvest.constants.Base;
import com.example.eduinvest.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends BaseActivity {
    private FirebaseAuth auth;
    private ActivityForgetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnForgotPasswordSubmit.setOnClickListener(v -> resetPassword());
        binding.tvSubmitMsg.setOnClickListener(v -> openGmail());
    }

    // Kiểm tra tính hợp lệ của email
    private boolean validateForm(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmailForgetPassword.setError("Nhập địa chỉ email hợp lệ");
            return false;
        }
        return true;
    }

    // Đặt lại mật khẩu
    private void resetPassword() {
        String email = binding.tilEmailForgetPassword.getText().toString();
        if (validateForm(email)) {
            Base.showProgressBar(this);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                Base.hideProgressBar(this);
                if (task.isSuccessful()) {
                    binding.tilEmailForgetPassword.setVisibility(View.GONE);
                    binding.tvSubmitMsg.setVisibility(View.VISIBLE);
                    binding.btnForgotPasswordSubmit.setVisibility(View.GONE);
                } else {
                    Base.showToast(this, "Không thể đặt lại mật khẩu. Thử lại sau.");
                }
            });
        }
    }

    // Mở ứng dụng Gmail
    private void openGmail() {
        String email = binding.tilEmailForgetPassword.getText().toString();
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
        Base.hideProgressBar(this);
        binding = null;
    }
}
