package com.example.eduinvest.activity;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.eduinvest.R;
import com.example.eduinvest.constants.Base;
import com.example.eduinvest.firebase.FireBaseClass;
import com.example.eduinvest.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signUpName, signUpEmail, signUpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // Đảm bảo bạn có đúng layout XML

        auth = FirebaseAuth.getInstance();

        // Khởi tạo các view
        signUpName = findViewById(R.id.signUpName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        Button signUpButton = findViewById(R.id.signUpButton);


        // Khi người dùng nhấn vào nút "Đăng ký"
        signUpButton.setOnClickListener(v -> registerUser());
    }

    // Đăng ký người dùng mới
    private void registerUser() {
        String name = signUpName.getText().toString();
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();

        if (validateForm(name, email, password)) {
            Base.showProgressBar(this);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Thành công
                            FirebaseUser user = task.getResult().getUser();
                            assert user != null;
                            String userId = user.getUid();

                            UserModel userInfo = new UserModel(name, userId, email, password);
                            new FireBaseClass().registerUser(userInfo);

                            Base.showToast(this, "Đăng Ký Tài Thành Công");
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            // Thất bại
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "User Id not created. Try again later";
                            Base.showToast(this, errorMessage);
                        }
                    });
        }
    }

    // Xác thực biểu mẫu đăng ký
    private boolean validateForm(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            signUpName.setError("Nhập tên người dùng");
            return false;
        } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Nhập email hợp lệ");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            signUpPassword.setError("Nhập mật khẩu");
            return false;
        }
        return true;
    }
}
