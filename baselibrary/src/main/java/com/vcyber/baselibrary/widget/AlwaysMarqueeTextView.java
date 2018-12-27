package com.vcyber.baselibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class AlwaysMarqueeTextView extends EditText{

    private String text;
    Paint paint;

    public AlwaysMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getText().toString();
    }

    public AlwaysMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint = getPaint();
//        paint.
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawText("hello",);

        String testString = "测试：gafaeh:1234";
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(40);
        mPaint.setColor(Color.RED);
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(testString, 0, testString.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(testString,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);

    }
}
