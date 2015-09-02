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
import android.widget.Button;

/**
 * Created by ulas on 25/08/15.
 */
public class HighlightedButton extends Button {

    private final static String HIGHLIGHT_FILTER_COLOR = "#646464";
    private final static float HIGHLIGHT_RATIO = 0.5f;

    private Context contex;
    private int originalTextColor;

    public HighlightedButton(Context context) {
        super(context);
        this.contex = context;
    }

    public HighlightedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.contex = context;
    }

    public HighlightedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.contex = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HighlightedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.contex = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Highlighting for background
        Drawable drawable = getBackground();
        drawable = highlightDrawable(drawable, event);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }

        //Highlighting for compound drawables
        Drawable[] drawables = getCompoundDrawables();
        Drawable left =  highlightDrawable(drawables[0], event);
        Drawable top = highlightDrawable(drawables[1], event);
        Drawable right = highlightDrawable(drawables[2], event);
        Drawable bottom =  highlightDrawable(drawables[3], event);
        setCompoundDrawables(left, top, right, bottom);

        //Highlighting for text
        int newColor = 0;
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            originalTextColor = getCurrentTextColor();
            int red, green, blue;
            red = Color.red(originalTextColor);
            green = Color.green(originalTextColor);
            blue = Color.blue(originalTextColor);
            red *= HIGHLIGHT_RATIO;
            green *= HIGHLIGHT_RATIO;
            blue *= HIGHLIGHT_RATIO;
            newColor = Color.rgb(red, green, blue);
            setTextColor(newColor);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            newColor = originalTextColor;
            setTextColor(newColor);
        }


        return super.onTouchEvent(event);
    }

    private Drawable highlightDrawable(Drawable drawable, MotionEvent event) {
        if (drawable != null) {
            if (drawable instanceof ColorDrawable) {
                Bitmap bitmap = drawableToBitmap(drawable);
                drawable = new BitmapDrawable(this.contex.getResources(), bitmap);
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                int color = Color.parseColor(HIGHLIGHT_FILTER_COLOR);
                drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                drawable.clearColorFilter();
            }
        }
        return drawable;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
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
