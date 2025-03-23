package com.example.eduinvest.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.eduinvest.R;
import com.example.eduinvest.adapters.ViewPagerFragmentAdapter;
import com.example.eduinvest.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPagerFragmentAdapter adapter;
    private static final int NOTIFICATION_PERMISSION_CODE = 1808; // Mã yêu cầu quyền thông báo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestNotificationPermission(); // Kiểm tra & yêu cầu quyền thông báo
        checkUserAuthentication(); // Kiểm tra người dùng đã đăng nhập chưa
        setupViewPagerAndTabs(); // Thiết lập ViewPager2 & TabLayout
    }

    // 🔹 Yêu cầu quyền gửi thông báo cho Android 13 trở lên
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }

    // 🔹 Xử lý kết quả cấp quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
            } else {
                // Người dùng từ chối quyền
            }
        }
    }

    // 🔹 Kiểm tra người dùng đã đăng nhập chưa
    private void checkUserAuthentication() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, OnboardingActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    // 🔹 Thiết lập ViewPager2 & TabLayout
    private void setupViewPagerAndTabs() {
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_home);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_news);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_person);
                    break;
                case 3:
                    tab.setIcon(R.drawable.ic_more);
                    break;
            }
        }).attach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Tránh memory leak
    }
}
