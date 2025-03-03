package com.example.eduinvest.utils.helper;

import android.content.Context;
import android.util.DisplayMetrics;

public class GlobalHelper {
    public static float convertDpToPixel(float dp, Context context) {
        if (context == null) return dp;
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
