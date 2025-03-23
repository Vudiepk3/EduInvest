package com.example.eduinvest.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MoreFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public MoreFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_support, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
