package com.example.eduinvest;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView startTXT = findViewById(R.id.startTXT);
        startTXT.setOnClickListener(v -> {
            startNewActivity();
        });
    }
    private void startNewActivity() {
        FirebaseAuth auth = FirebaseAuth.getInstance(); // Lấy một instance của FirebaseAuth
        FirebaseUser currentUser = auth.getCurrentUser(); // Lấy người dùng hiện tại đã đăng nhập

        // Kiểm tra xem người dùng có đăng nhập không
        if (currentUser != null) {
            // Nếu người dùng đã đăng nhập, bắt đầu MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Nếu người dùng chưa đăng nhập, bắt đầu GetStartedActivity
            startActivity(new Intent(this, SignInActivity.class));
        }

        // Kết thúc activity hiện tại
        finish();
    }

}