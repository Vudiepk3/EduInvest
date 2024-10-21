package com.example.eduinvest;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eduinvest.fragments.HomeFragment;
import com.example.eduinvest.fragments.MySelfFragment;
import com.example.eduinvest.fragments.NewsFragment;
import com.example.eduinvest.fragments.SupportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver networkChangeReceiver;
    private boolean isConnected = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sử dụng layout trực tiếp

        // Thay thế binding bằng findViewById
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.news) {
                replaceFragment(new NewsFragment());
            } else if (itemId == R.id.support) {
                replaceFragment(new SupportFragment());
            } else if (itemId == R.id.myself) {
                replaceFragment(new MySelfFragment());
            }
            return true;
        });
        //kiểm tra trạng thái mạng
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isAvailable = isNetworkAvailable(context);

                if (isAvailable && !isConnected) {
                    // Kết nối internet đã được khôi phục
                    Toast.makeText(context, "Kết nối internet đã được khôi phục", Toast.LENGTH_SHORT).show();
                    isConnected = true; // Cậpnhật trạng thái kết nối
                } else if (!isAvailable && isConnected) {
                    // Mất kết nối mạng
                    Toast.makeText(context, "Không có kết nối internet", Toast.LENGTH_SHORT).show();
                    isConnected = false; // Cập nhật trạng thái kết nối
                }
            }
        };

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký BroadcastReceiver khi Activity được hiển thị
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver khi Activity bị ẩn
        unregisterReceiver(networkChangeReceiver);
    }
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        unregisterReceiver(networkChangeReceiver);
    }
}
