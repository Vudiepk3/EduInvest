package com.example.eduinvest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.eduinvest.adapters.EditProfileAdapter;
import com.example.eduinvest.firebase.FireBaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ProgressBar progressBar; // Add a ProgressBar to indicate loading

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
        // Load user information
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
        // Hiển thị dialog tiến trình
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();// Show loading indicator
        FireBaseClass.getUserInfo(userInfo -> {
            if (userInfo != null) {
                tvUserName.setText(userInfo.getName());
                tvEmailId.setText(auth.getCurrentUser().getEmail());
                // Load the profile image if available
                Glide.with(this)
                        .load(userInfo.getImage())
                        .placeholder(R.drawable.image_user) // Placeholder image
                        .error(R.drawable.image_user) // Fallback image
                        .into(userProfilePic);
                dialog.dismiss(); // Hide loading indicator
            } else {
                tvUserName.setText("N/A");
                tvEmailId.setText("N/A");
            }
        });
    }
}
