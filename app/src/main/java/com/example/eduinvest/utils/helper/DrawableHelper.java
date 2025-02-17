package com.example.eduinvest.utils.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

public class DrawableHelper {
    private static final String RECTANGLE = "RECTANGLE";
    private static final String OVAL = "OVAL";

    private static GradientDrawable init(String shape, Integer backgroundColor, Integer borderColor, Integer borderWidth, Float radius) {
        GradientDrawable drawable = new GradientDrawable();
        if (shape.equals(RECTANGLE)) {
            drawable.setShape(GradientDrawable.RECTANGLE);
        } else if (shape.equals(OVAL)) {
            drawable.setShape(GradientDrawable.OVAL);
        } else {
            drawable.setShape(GradientDrawable.RECTANGLE);
        }
        if (backgroundColor != null) {
            drawable.setColor(backgroundColor);
        }


        if (borderColor != null && borderWidth != null)
            drawable.setStroke(borderWidth, borderColor);
        if (radius != null) {
            drawable.setCornerRadius(radius);
        }
        return drawable;
    }

    public static GradientDrawable init(String shape, Integer backgroundColor, Integer borderColor, Integer borderWidth, float[] radius) {
        GradientDrawable drawable = new GradientDrawable();
        if (shape.equals(RECTANGLE)) {
            drawable.setShape(GradientDrawable.RECTANGLE);
        } else if (shape.equals(OVAL)) {
            drawable.setShape(GradientDrawable.OVAL);
        } else {
            drawable.setShape(GradientDrawable.RECTANGLE);
        }
        if (backgroundColor != null) {
            drawable.setColor(backgroundColor);
        }


        if (borderColor != null && borderWidth != null)
            drawable.setStroke(borderWidth, borderColor);
        if (radius != null) {
            drawable.setCornerRadii(radius);
        }
        return drawable;
    }

    public static GradientDrawable rectangle(Context context, Integer backgroundColor, Float radius) {
        if (context == null) return null;
        return init(
                RECTANGLE, ContextCompat.getColor(context, backgroundColor),
                null, null,
                GlobalHelper.convertDpToPixel(radius, context)
        );
    }

    public static GradientDrawable rectangle(Context context, String backgroundColor, Float radius) {
        return init(
                RECTANGLE, Color.parseColor(backgroundColor),
                null, null,
                GlobalHelper.convertDpToPixel(radius, context)
        );
    }

    public static GradientDrawable rectangle(
            Context context, Integer backgroundColor,
            Integer borderColor, Float borderWidth, Float radius
    ) {
        if (context == null) return null;
        return init(
                RECTANGLE, ContextCompat.getColor(context, backgroundColor),
                ContextCompat.getColor(context, borderColor),
                (int) GlobalHelper.convertDpToPixel(borderWidth, context),
                GlobalHelper.convertDpToPixel(radius, context)
        );
    }

    public static GradientDrawable rectangle(
            Context context, String backgroundColor,
            String borderColor, Float borderWidth, Float radius
    ) {
        return init(
                RECTANGLE, Color.parseColor(backgroundColor),
                Color.parseColor(borderColor),
                (int) GlobalHelper.convertDpToPixel(borderWidth, context),
                GlobalHelper.convertDpToPixel(radius, context)
        );
    }

    public static GradientDrawable rectangle(Context context, Integer backgroundColor, float[] radius) {
        if (context == null) return null;
        return init(
                RECTANGLE, ContextCompat.getColor(context, backgroundColor),
                null, null, radius
        );
    }

    public static GradientDrawable rectangle(int backgroundColor, float[] radius) {
        return init(
                RECTANGLE, backgroundColor,
                null, null, radius
        );
    }
    

    public static GradientDrawable rectangle(
            Context context, Integer borderColor, Float borderWidth, float[] radius
    ) {
        if (context == null) return null;
        return init(
                RECTANGLE, null,
                ContextCompat.getColor(context, borderColor),
                (int) GlobalHelper.convertDpToPixel(borderWidth, context),
                radius
        );
    }

    public static GradientDrawable rectangle(
            Context context, Integer backgroundColor,
            Integer borderColor, Float borderWidth, float[] radius
    ) {
        if (context == null) return null;
        return init(
                RECTANGLE, ContextCompat.getColor(context, backgroundColor),
                ContextCompat.getColor(context, borderColor),
                (int) GlobalHelper.convertDpToPixel(borderWidth, context),
                radius
        );
    }

    // background cho xml
    public static GradientDrawable attrsRectangle(
            Integer backgroundColor, Integer borderColor, Integer borderWidth, Integer radius
    ) {
        return init(
                RECTANGLE, backgroundColor != Color.TRANSPARENT ? backgroundColor : null,
                borderColor != Color.TRANSPARENT ? borderColor : null,
                borderWidth != 0 ? borderWidth : null,
                radius != 0 ? (float) radius : null
        );
    }

    public static GradientDrawable attrsRectangle(
            Integer backgroundColor, Integer borderColor, Integer borderWidth,
            Integer radiusTopLeft, Integer radiusTopRight, Integer radiusBottomRight, Integer radiusBottomLeft
    ) {
        return init(
                RECTANGLE, backgroundColor != Color.TRANSPARENT ? backgroundColor : null,
                borderColor != Color.TRANSPARENT ? borderColor : null,
                borderWidth != 0 ? borderWidth : null,
                new float[]{radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft}
        );
    }

 

    public static GradientDrawable attrsOval(Integer backgroundColor, Integer borderColor, Integer borderWidth) {
        return init(OVAL, backgroundColor != Color.TRANSPARENT ? backgroundColor : null,
                borderColor != Color.TRANSPARENT ? borderColor : null,
                borderWidth != 0 ? borderWidth : null, 0f);
    }


}

