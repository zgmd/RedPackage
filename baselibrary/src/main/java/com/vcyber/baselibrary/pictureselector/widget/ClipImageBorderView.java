package com.vcyber.baselibrary.pictureselector.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.vcyber.baselibrary.pictureselector.PicSelector;
import com.vcyber.baselibrary.utils.Logger;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/article/details/39761281
 */
public class ClipImageBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 3;

    private Paint mPaint;
    private Paint mPaintCircle;
    private Paint mBoderPaint;
    private final int mRadius = 240;

    private Xfermode cur_xfermode;
    private Rect r;
    private RectF rf;
    //裁剪模式
    private int mClipMode;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mBoderPaint = new Paint();
        mBoderPaint.setAntiAlias(true);
        mBoderPaint.setStyle(Style.STROKE);
        mBoderPaint.setStrokeWidth(mBorderWidth);
        mBoderPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setAntiAlias(true);

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setStrokeWidth(mBorderWidth);

        cur_xfermode = new PorterDuffXfermode(Mode.DST_OUT);
//        cur_xfermode = new PorterDuffXfermode(Mode.XOR);
//        setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mClipMode == PicSelector.MODE_CLIP_SQUARE) {
            drawSquareFrame(canvas);
        } else {
            drawCircleFrame(canvas);
        }
    }

    /**
     * 画圆形裁剪框
     *
     * @param canvas
     */
    private void drawCircleFrame(Canvas canvas) {
        if (rf == null || rf.isEmpty()) {
            r = new Rect(0, 0, getWidth(), getHeight());
            rf = new RectF(r);
        }
        mPaint.setStyle(Style.STROKE);
        //TODO 此处还有一个背景图片没有高亮显示的BUG （原因不明）
        // 在imageview上面画入背景和 圆形
        int sc = canvas.saveLayer(rf, null, Canvas.ALL_SAVE_FLAG);
//        canvas.saveLayerAlpha(rf,Color.alpha(0));
//        canvas.saveLayerAlpha()
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Style.FILL);
        canvas.drawRect(r, mPaint);
        mPaint.setXfermode(cur_xfermode);
        // 绘制圆形
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
        // 绘制边框
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mBoderPaint);
        canvas.restoreToCount(sc);
        mPaint.setXfermode(null);
    }

    /**
     * 画方形裁剪框
     *
     * @param canvas
     */
    private void drawSquareFrame(Canvas canvas) {
        // 计算矩形区域的宽度
        mWidth = getWidth() - 2 * mHorizontalPadding;
        // 计算距离屏幕垂直边界 的边距
        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Style.FILL);
        // 绘制左边1
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        // 绘制右边2
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
                getHeight(), mPaint);
        // 绘制上边3
        canvas.drawRect(mHorizontalPadding, 0, getWidth() -
                mHorizontalPadding, mVerticalPadding, mPaint);
        // 绘制下边4
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
                getWidth() - mHorizontalPadding, getHeight(), mPaint);
        // 绘制外边框
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);

        canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() -
                mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
        Logger.e("透明度getAlpha-->" + getAlpha());
        Logger.e("getBackground()-->" + getBackground());
        Logger.e("getDrawingCacheBackgroundColor()-->" + getDrawingCacheBackgroundColor());
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;

    }

    public void setClipMode(int clipMode) {
        this.mClipMode = clipMode;
        invalidate();
    }
}
