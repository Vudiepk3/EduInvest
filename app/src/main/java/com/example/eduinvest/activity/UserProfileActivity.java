package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.EditProfileAdapter;
import com.example.eduinvest.firebase.FireBaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Khởi tạo các view
        TextView tvEmailId = findViewById(R.id.tvEmailId);
        TextView tvUserName = findViewById(R.id.tvUserName);
        ImageView userProfilePic = findViewById(R.id.userProfilePic);
        CardView cvSignOut = findViewById(R.id.cvSignOut);
        CardView cvEditProfile = findViewById(R.id.cvEditProfile);
        auth = FirebaseAuth.getInstance();
        // hiển thị thông tin người dùng
        loadUserInfo(tvEmailId, tvUserName, userProfilePic);

        cvSignOut.setOnClickListener(v -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                auth.signOut();
                Intent intent = new Intent(UserProfileActivity.this, IntroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        cvEditProfile.setOnClickListener(v -> {
            EditProfileAdapter bottomSheetFragment = new EditProfileAdapter(UserProfileActivity.this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    private void loadUserInfo(TextView tvEmailId, TextView tvUserName, ImageView userProfilePic) {

        FireBaseClass.getUserInfo(userInfo -> {
            if (userInfo != null) {
                tvUserName.setText(userInfo.getName());
                tvEmailId.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
                // hiển thị ảnh đại diện
                Glide.with(this)
                        .load(userInfo.getImage())
                        .placeholder(R.drawable.image_user) // Placeholder image
                        .error(R.drawable.image_user) // Fallback image
                        .into(userProfilePic);
            } else {
                tvUserName.setText("N/A");
                tvEmailId.setText("N/A");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo(findViewById(R.id.tvEmailId), findViewById(R.id.tvUserName), findViewById(R.id.userProfilePic));
    }
}
