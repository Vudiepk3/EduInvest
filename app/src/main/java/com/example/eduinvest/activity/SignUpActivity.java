package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.constants.Base;
import com.example.eduinvest.databinding.ActivitySignUpBinding;
import com.example.eduinvest.firebase.FireBaseClass;
import com.example.eduinvest.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivitySignUpBinding binding; // ViewBinding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        // Xử lý sự kiện đăng ký
        binding.signUpButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = binding.signUpName.getText().toString().trim();
        String email = binding.signUpEmail.getText().toString().trim();
        String password = binding.signUpPassword.getText().toString().trim();

        if (!validateForm(name, email, password)) return;

        Base.showProgressBar(this);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Base.hideProgressBar(this);
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            String userId = user.getUid();
                            UserModel userInfo = new UserModel(name, userId, email, "","","",""); // Không lưu mật khẩu
                            new FireBaseClass().registerUser(userInfo);

                            Base.showToast(this, "Đăng ký thành công!");
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    } else {
                        handleFirebaseError(task.getException());
                    }
                });
    }

    private boolean validateForm(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            binding.signUpName.setError("Nhập tên người dùng");
            binding.signUpName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signUpEmail.setError("Nhập email hợp lệ");
            binding.signUpEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            binding.signUpPassword.setError("Nhập mật khẩu");
            binding.signUpPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            binding.signUpPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            binding.signUpPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void handleFirebaseError(Exception exception) {
        if (exception == null) {
            Base.showToast(this, "Lỗi không xác định, vui lòng thử lại.");
            return;
        }

        String errorMessage;
        String error = exception.getMessage();
        if (error != null) {
            if (error.contains("email address is already in use")) {
                errorMessage = "Email này đã được đăng ký.";
            } else if (error.contains("badly formatted")) {
                errorMessage = "Email không đúng định dạng.";
            } else if (error.contains("password is invalid or the user does not have a password")) {
                errorMessage = "Mật khẩu không hợp lệ.";
            } else {
                errorMessage = "Lỗi: " + error;
            }
        } else {
            errorMessage = "Lỗi không xác định.";
        }

        Base.showToast(this, errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Giải phóng bộ nhớ
    }
}
