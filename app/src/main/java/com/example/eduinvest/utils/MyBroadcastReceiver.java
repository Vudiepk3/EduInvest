package com.example.eduinvest.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * MyBroadcastReceiver là một BroadcastReceiver nhận các broadcast liên quan đến thay đổi trạng thái kết nối mạng.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    /**
     * Phương thức onReceive được gọi khi BroadcastReceiver nhận được một Intent.
     *
     * @param context Context của ứng dụng.
     * @param intent  Intent được gửi đến BroadcastReceiver.
     */
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        // Kiểm tra xem action của intent có phải là CONNECTIVITY_ACTION (trạng thái kết nối mạng thay đổi) hay không.
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // Nếu có kết nối mạng, hiển thị thông báo "Network Available",
            // nếu không thì hiển thị "Network Unavailable".
            if (isNetworkAvailable(context)) {
                Toast.makeText(context, "Network Available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Phương thức isNetworkAvailable kiểm tra xem thiết bị có kết nối mạng hay không.
     *
     * @param context Context để truy cập hệ thống dịch vụ.
     * @return true nếu thiết bị có kết nối mạng, false nếu không.
     */
    private boolean isNetworkAvailable(@NonNull Context context) {
        // Lấy đối tượng ConnectivityManager từ hệ thống.
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        // Kiểm tra nếu phiên bản Android là Marshmallow (API 23) trở lên, sử dụng NetworkCapabilities
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Lấy mạng hiện tại đang được sử dụng.
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            // Lấy thông tin NetworkCapabilities cho mạng này.
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            // Kiểm tra xem mạng có hỗ trợ WiFi hoặc Cellular hay không.
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            // Với các phiên bản Android cũ hơn, sử dụng getActiveNetworkInfo()
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}