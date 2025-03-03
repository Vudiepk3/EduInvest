package com.example.eduinvest.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.eduinvest.adapters.ViewOnboardingAdapter;
import com.example.eduinvest.databinding.ActivityOnboardingBinding;

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
        binding.tvNext.setOnClickListener(v -> {
            int currentItem = binding.viewPager.getCurrentItem();
            if (currentItem < adapter.getCount() - 1) {
                binding.viewPager.setCurrentItem(currentItem + 1);
            } else {
                startActivity(new Intent(this, MainActivity.class));
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
            super.onBackPressed();
        }
    }
}
