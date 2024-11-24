package com.example.eduinvest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver networkChangeReceiver;
    private boolean isConnected = true;
    private boolean isReceiverRegistered = false; // Biến để kiểm tra trạng thái đăng ký

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra trạng thái đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Kết thúc MainActivity
            return; // Dừng thực thi tiếp theo
        }

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

        // Đăng ký BroadcastReceiver để lắng nghe thay đổi kết nối mạng
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isAvailable = isNetworkAvailable(context);

                if (isAvailable && !isConnected) {
                    Toast.makeText(context, "Kết nối internet đã được khôi phục", Toast.LENGTH_SHORT).show();
                    isConnected = true;
                } else if (!isAvailable && isConnected) {
                    Toast.makeText(context, "Không có kết nối internet", Toast.LENGTH_SHORT).show();
                    isConnected = false;
                }
            }
        };
    }

    // Thay thế Fragment hiện tại bằng một Fragment mới
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Kiểm tra xem mạng có khả dụng hay không
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký BroadcastReceiver khi Activity chuyển sang trạng thái onResume
        if (!isReceiverRegistered) {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver khi Activity chuyển sang trạng thái onPause
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver khi Activity bị hủy
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;
        }
    }
}
