package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.EditProfileAdapter;
import com.example.eduinvest.databinding.ActivityUserProfileBinding;
import com.example.eduinvest.firebase.FireBaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        // Hiển thị thông tin người dùng
        loadUserInfo();

        binding.cvSignOut.setOnClickListener(v -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                auth.signOut();
                Intent intent = new Intent(this, OnboardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.cvEditProfile.setOnClickListener(v -> {
            EditProfileAdapter bottomSheetFragment = new EditProfileAdapter(this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    private void loadUserInfo() {
        FireBaseClass.getUserInfo(userInfo -> {
            if (userInfo != null) {
                binding.tvUserName.setText(userInfo.getName());
                binding.tvEmailId.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());

                // Hiển thị ảnh đại diện
                Glide.with(this)
                        .load(userInfo.getImage())
                        .placeholder(R.drawable.img_user)
                        .error(R.drawable.img_user)
                        .into(binding.userProfilePic);
            } else {
//                binding.tvUserName.setText("N/A");
//                binding.tvEmailId.setText("N/A");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Giải phóng bộ nhớ
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
