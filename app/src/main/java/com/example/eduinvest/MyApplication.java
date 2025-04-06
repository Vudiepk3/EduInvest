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
        BannerRepository.getInstance().loadData();
        NewsRepository.getInstance();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}

