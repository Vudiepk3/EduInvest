package com.example.eduinvest.adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eduinvest.SupportActivities.AssiterFragment;
import com.example.eduinvest.SupportActivities.ChatGPTFragment;
import com.example.eduinvest.SupportActivities.FAQFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FAQFragment(); // Fragment cho tab FAQ
        } else if (position == 1) {
            return new ChatGPTFragment(); // Fragment cho tab Chat GPT
        } else if (position == 2) {
            return new AssiterFragment(); // Fragment cho tab Nhân viên hỗ trợ
        } else {
            return new FAQFragment(); // Fragment mặc định
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tab (FAQ, Chat GPT, Nhân viên hỗ trợ)
    }
}
