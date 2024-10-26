package com.example.eduinvest.constants;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Base {
    private static ProgressDialog progressDialog;

    // Hiển thị ProgressDialog
    public static void showProgressBar(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    // Ẩn ProgressDialog
    public static void hideProgressBar(Context context) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    // Hiển thị thông báo Toast
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
