package com.example.eduinvest;

import android.app.Application;
import com.example.eduinvest.repository.BannerRepository;
import com.example.eduinvest.repository.NewsRepository;

public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Load banner ngay khi ứng dụng khởi chạy
        BannerRepository.getInstance().loadData();
        NewsRepository.getInstance();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}

