package com.example.eduinvest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eduinvest.listener.VoidCallback;
import com.example.eduinvest.utils.helper.DrawableHelper;
import com.example.eduinvest.R;

public class ButtonTextView extends androidx.appcompat.widget.AppCompatTextView {

    // Constructor khởi tạo khi không có thuộc tính
    public ButtonTextView(@NonNull Context context) {
        super(context);
    }

    // Constructor khởi tạo với thuộc tính từ XML
    public ButtonTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    // Phương thức khởi tạo giao diện từ XML
    private void initView(AttributeSet attrs) {
        try (TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonTextView)) {
            int colorBackground = typedArray.getColor(R.styleable.ButtonTextView_button_color, Color.TRANSPARENT);
            int borderColor = typedArray.getColor(R.styleable.ButtonTextView_button_borderColor, Color.TRANSPARENT);
            int borderWidth = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_borderWidth, 0);

            int shape = typedArray.getInt(R.styleable.ButtonTextView_button_shape, 0);
            if (shape == 1) {
                // Nếu là hình oval
                setBackground(DrawableHelper.attrsOval(colorBackground, borderColor, borderWidth));
            } else {
                // Nếu là hình chữ nhật
                int radius = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_radius, 0);
                if (radius == 0) {
                    // Nếu không có góc bo chung, lấy từng góc riêng
                    int radiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_radiusTopLeft, 0);
                    int radiusTopRight = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_radiusTopRight, 0);
                    int radiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_radiusBottomLeft, 0);
                    int radiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.ButtonTextView_button_radiusBottomRight, 0);

                    if (radiusTopLeft != 0 || radiusTopRight != 0 || radiusBottomRight != 0 || radiusBottomLeft != 0) {
                        setBackground(DrawableHelper.attrsRectangle(colorBackground, borderColor, borderWidth,
                                radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft));
                    } else {
                        setBackground(DrawableHelper.attrsRectangle(colorBackground, borderColor, borderWidth, radius));
                    }
                } else {
                    // Nếu có góc bo chung
                    setBackground(DrawableHelper.attrsRectangle(colorBackground, borderColor, borderWidth, radius));
                }
            }

            // Cắt phần hiển thị theo đường viền
            boolean isClipToOutline = typedArray.getBoolean(R.styleable.ButtonTextView_button_clipToOutline, false);
            setClipToOutline(isClipToOutline);

            // Thiết lập gạch chân cho văn bản nếu cần
            boolean isUnderline = typedArray.getBoolean(R.styleable.ButtonTextView_button_underline, false);
            if (isUnderline) {
                setPaintFlags(getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
            }
        }
    }

    private final long CLICK_DELAY = 500; // Độ trễ giữa hai lần nhấn (0.5 giây)
    private long lastClickTime = 0;

    // Phương thức thiết lập sự kiện click, tránh click liên tục
    public void onClickListener(VoidCallback callback) {
        setOnClickListener(view -> {
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - lastClickTime) < CLICK_DELAY) return;
            lastClickTime = currentTime;
            if (callback != null) {
                callback.execute();
            }
        });
    }
}
