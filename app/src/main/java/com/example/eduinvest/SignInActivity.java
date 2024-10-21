package com.example.eduinvest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
         getActivities();
    }
    private void getActivities(){
        Button signInButton = findViewById(R.id.login_button);
        signInButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}