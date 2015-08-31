package com.ulassancak.highlightedbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**
 * Created by ulas on 25/08/15.
 */
public class HighlightedImageButton extends ImageButton {

    private final static String HIGHLIGHT_FILTER_COLOR = "#646464";

    private Context contex;

    public HighlightedImageButton(Context context) {
        super(context);
        this.contex = context;
    }

    public HighlightedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.contex = context;
    }

    public HighlightedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.contex = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HighlightedImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.contex = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (drawable instanceof ColorDrawable) {
                Bitmap bitmap = drawableToBitmap(drawable);
                drawable = new BitmapDrawable(contex.getResources(), bitmap);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int color = Color.parseColor(HIGHLIGHT_FILTER_COLOR);
                drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
            else {
                drawable.clearColorFilter();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(drawable);
            }
            else {
                setBackgroundDrawable(drawable);
            }
        }
        return super.onTouchEvent(event);
    }

    private Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
