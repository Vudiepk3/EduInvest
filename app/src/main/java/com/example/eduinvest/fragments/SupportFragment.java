package com.example.eduinvest.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SupportFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public SupportFragment() {
        // Required empty public constructor
    }

    public static SupportFragment newInstance() {
        return new SupportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        getViews(view);
        return view;
    }

    private void getViews(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        // Sử dụng ViewPagerAdapter để quản lý các fragment
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        // Thêm các tab
        tabLayout.addTab(tabLayout.newTab().setText("Câu hỏi FAQ"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat GPT"));
        tabLayout.addTab(tabLayout.newTab().setText("Nhân Viên Hỗ Trợ"));

        // Xử lý sự kiện khi chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không làm gì
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không làm gì
            }
        });

        // Đồng bộ giữa ViewPager và TabLayout
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}