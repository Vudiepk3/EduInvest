package com.example.eduinvest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.example.eduinvest.BuildConfig;
import com.dino.rate.RatingDialog;
import com.example.eduinvest.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Common {

    public static void showRate(Context context) {

        RatingDialog ratingDialog = new RatingDialog.Builder((Activity) context)
                .session(1)
                .date(1)
                .setNameApp(context.getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher_round)
                .setEmail("vulq2k3@gmail.com")
                .ignoreRated(false)
                .isShowButtonLater(true)
                .isClickLaterDismiss(true)
                .setTextButtonLater("Maybe Later")
                .setOnlickMaybeLate(() -> {
                })
                .ratingButtonColor(Color.parseColor("#1D7FFF"))
                .build();

        ratingDialog.setCanceledOnTouchOutside(false);
        ratingDialog.show();
    }
    public static void logEvent(Context context, String eventName) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("onEvent", context.getClass().getSimpleName());
        firebaseAnalytics.logEvent(eventName + "_" + BuildConfig.VERSION_CODE, bundle);
        Log.d("===Event", eventName + "_" + BuildConfig.VERSION_CODE);
    }
    private static final String MY_SHARED_PREFERENCE = "MySharedPreference";

    public static void setFirstOpen(Context context, boolean isFirstOpen) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FirstOpen", isFirstOpen);
        editor.apply(); // hoặc editor.commit();
    }

    public static boolean getFirstOpen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("FirstOpen", true);
    }


}
