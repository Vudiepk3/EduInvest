package com.example.eduinvest.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.dino.rate.RatingDialog;
import com.example.eduinvest.R;

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

}
