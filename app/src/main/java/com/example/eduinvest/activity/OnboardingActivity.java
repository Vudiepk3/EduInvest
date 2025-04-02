package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.eduinvest.adapters.ViewOnboardingAdapter;
import com.example.eduinvest.databinding.ActivityOnboardingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private ViewOnboardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        setupClick();
    }

    private void setupViewPager() {
        adapter = new ViewOnboardingAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.circleIndicator.setViewPager(binding.viewPager);
    }

    private void setupClick() {
        binding.btnNext.setOnClickListener(v -> {
            int currentItem = binding.viewPager.getCurrentItem();
            if (currentItem < adapter.getCount() - 1) {
                binding.viewPager.setCurrentItem(currentItem + 1);
            } else {
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
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPager.getCurrentItem() > 0) {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
        } else {
            System.exit(0);
        }
    }
}
