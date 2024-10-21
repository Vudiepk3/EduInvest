package com.example.eduinvest;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TextView startTXT = findViewById(R.id.startTXT);
        startTXT.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, SignInActivity.class));
        });
    }
}