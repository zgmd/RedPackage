package com.vcyber.baselibrary.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.vcyber.baselibrary.R;
import com.vcyber.baselibrary.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   : jack
 * Date     : 2018/8/27 17:31
 * E-mail   : 1215530740@qq.com
 * Describe :
 */
public class TurntableView extends View implements GestureDetector.OnGestureListener {

    private GestureDetector mDetector;
    //
    Paint mPaint;
    //绘制文本
    Paint mPaintText;
    private int height;
    private int width;
    private int mPaintWidth = 20;
    private int copies = 10;
    private Context mContext;

    private List<ActionItem> subActionItems = new ArrayList();
    private List<Path> paths = new ArrayList<>();
    List<float[]> currentPoints = new ArrayList<>();
    List<float[]> startPoints = new ArrayList<>();

    //图片与文本的间距
    private int bitmapToTextDistance = 30;
//    private String mText = ;
    //
    /**
     * 大圆偏移量 1,2,3
     * 1--左1/3圆
     * 2--半圆
     * 3--右1/3圆
     */
    private int circleOffset = 0;
    private Canvas mCanvas;
    private RectF mArea;
    //是否是第一次绘制
    private boolean isFirst = true;
    //大圆的path
    private Path orbit;
    private PathMeasure measure;


    public TurntableView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TurntableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        BitmapDrawable drawable = (BitmapDrawable) mContext.getDrawable(R.drawable.icon);
        Bitmap bitmap = drawable.getBitmap();
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));
        subActionItems.add(new ActionItem(bitmap));

        for (int i = 0; i < subActionItems.size(); i++) {
            paths.add(new Path());
        }

        mDetector = new GestureDetector(context, this);

//        initAnimtor();
    }


    public TurntableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private void initAnimtor() {

        int radius = width / 2 - mPaintWidth;
        int startAngle = -180;
        int endAngle = 180;
        //获取中心圆点的坐标值
        Point center = getActionViewCenter();
        //内切弧形路径
        //以圆点坐标（x，y）为中心画一个矩形RectF
        int offset = 0;
        if (circleOffset == 0) {
            offset = 0;
        } else if (circleOffset == 1) {
            offset = radius / 2;
        } else if (circleOffset == 2) {
            offset = radius;
        } else if (circleOffset == 3) {
            offset = radius + radius / 2;
        }
        //TODO 此处控制偏移量
        mArea = new RectF(center.x - radius - offset, center.y - radius, center.x + radius - offset, center.y + radius);
        orbit = new Path();
        orbit.moveTo(-radius, center.y);
        orbit.addArc(mArea, startAngle, endAngle - startAngle);
        measure = new PathMeasure(orbit, true);

        //然后将该路径平分成subActionItems.size()段
        int divisor;
        if (Math.abs(endAngle - startAngle) >= 360 || subActionItems.size() <= 1) {
            divisor = subActionItems.size();
        } else {
            divisor = subActionItems.size() - 1;
        }
        for (int i = 0; i < subActionItems.size(); i++) {
            if (isFirst) {
                float[] coords = new float[]{0f, 0f};
                //利用PathMeasure分别测量出各个点的坐标值coords
                measure.getPosTan((i) * measure.getLength() / divisor + count, coords, null);
                subActionItems.get(i).x = (int) coords[0] - subActionItems.get(i).bitmap.getWidth() / 2;
                subActionItems.get(i).y = (int) coords[1] - subActionItems.get(i).bitmap.getHeight() / 2;
//                currentPoints.add(new Point(([subActionItems.get(i).x, subActionItems.get(i).y]));
                float[] point = new float[]{subActionItems.get(i).x, subActionItems.get(i).y};
                currentPoints.add(point);
                paths.get(i).moveTo(subActionItems.get(i).x, subActionItems.get(i).y);
            } else {

            }

        }


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, measure.getLength());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
//                mPathMeasure.getPosTan(value, mCurrentPosition, null);
//                mPath2.lineTo(mCurrentPosition[0], mCurrentPosition[1]);
                for (int i = 0, j = subActionItems.size(); i < j; i++) {
                    measure.getPosTan(value, currentPoints.get(i), null);
                    paths.get(i).lineTo(currentPoints.get(i)[0], currentPoints.get(i)[1]);
                }
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
//                mPath2 = new Path();
//                mPathMeasure = new PathMeasure(mPath, false);
//                mPathMeasure.getPosTan(0, mCurrentPosition, null);
//                mPath2.moveTo(mCurrentPosition[0], mCurrentPosition[1]);
//                if (mPaint2.getColor() == Color.BLUE) {
//                    mPaint2.setColor(Color.YELLOW);
//                } else {
//                    mPaint2.setColor(Color.BLUE);
//                }
            }
        });
        valueAnimator.setDuration(5000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        height = getMeasuredHeight();
        width = getMeasuredWidth();

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextSize(20);
        mPaintText.setColor(Color.BLUE);
        mPaintText.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        calculateItemPositions(canvas);
    }

    int count = 0;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void calculateItemPositions(final Canvas canvas) {

        int radius = width / 2 - mPaintWidth;
        int startAngle = -180;
        int endAngle = 180;

        //获取中心圆点的坐标值
        Point center = getActionViewCenter();
        //内切弧形路径
        //以圆点坐标（x，y）为中心画一个矩形RectF
        int offset = 0;
        if (circleOffset == 0) {
            offset = 0;
        } else if (circleOffset == 1) {
            offset = radius / 2;
        } else if (circleOffset == 2) {
            offset = radius;
        } else if (circleOffset == 3) {
            offset = radius + radius / 2;
        }

        //TODO 此处控制偏移量
        mArea = new RectF(center.x - radius - offset, center.y - radius, center.x + radius - offset, center.y + radius);
        orbit = new Path();
        Path path = new Path();
        orbit.moveTo(-radius, center.y);
        orbit.addArc(mArea, startAngle, endAngle - startAngle);
        path.addArc(mArea, startAngle, endAngle - startAngle);
        orbit.addPath(path);

        measure = new PathMeasure(orbit, true);
        Logger.i("measure.getLength()--add前>"+measure.getLength());

        //然后将该路径平分成subActionItems.size()段
        int divisor;
        if (Math.abs(endAngle - startAngle) >= 360 || subActionItems.size() <= 1) {
            divisor = subActionItems.size();
        } else {
            divisor = subActionItems.size() - 1;
        }

        canvas.drawPath(orbit, mPaint);
        Logger.i("measure.getLength()-->"+measure.getLength());
//        measure.setPath(path,false);
        Logger.i("measure.getLength()-->"+measure.getLength());
        for (int i = 0; i < subActionItems.size(); i++) {
            if (isFirst) {
                float[] coords = new float[]{0f, 0f};
                //利用PathMeasure分别测量出各个点的坐标值coords
                measure.getPosTan((i) * measure.getLength() / divisor, coords, null);
                subActionItems.get(i).x = (int) coords[0] - subActionItems.get(i).bitmap.getWidth() / 2;
                subActionItems.get(i).y = (int) coords[1] - subActionItems.get(i).bitmap.getHeight() / 2;
                float[] point = new float[]{subActionItems.get(i).x, subActionItems.get(i).y};
                startPoints.add(point);
                paths.get(i).moveTo(subActionItems.get(i).x, subActionItems.get(i).y);
            } else {


            float[] coords = new float[]{0f, 0f};
            //利用PathMeasure分别测量出各个点的坐标值coords
                float distance = 0f;
                if((i) * measure.getLength() / divisor==measure.getLength()){
                    distance = (measure.getLength() / divisor)*i;
                }else{
                    distance = (i) * measure.getLength() / divisor;
                }
            measure.getPosTan(distance + count, coords, null);
            subActionItems.get(i).x = (int) coords[0] - subActionItems.get(i).bitmap.getWidth() / 2;
            subActionItems.get(i).y = (int) coords[1] - subActionItems.get(i).bitmap.getHeight() / 2;
//                currentPoints.add(new Point(subActionItems.get(i).x, subActionItems.get(i).y));
//            float[] point = new float[]{subActionItems.get(i).x, subActionItems.get(i).y};
//            currentPoints.add(point);
////            paths.get(i).moveTo(subActionItems.get(i).x, subActionItems.get(i).y);
//            int distanceXStart = (int) (currentPoints.get(i)[0] - startPoints.get(0)[0]);
//            int distanceYStart = (int) (currentPoints.get(i)[1] - startPoints.get(0)[1]);
//            int distanceXEnd = (int) (currentPoints.get(i)[0] - startPoints.get(startPoints.size()-1)[0]);
//            int distanceYEnd = (int) (currentPoints.get(i)[1] - startPoints.get(startPoints.size()-1)[1]);
//            if (Math.abs(distanceXStart) <= 2 && Math.abs(distanceYStart) <= 2) {
//                subActionItems.get(i).x = (int) startPoints.get(0)[0];
//                subActionItems.get(i).y = (int) startPoints.get(0)[1];
//            }
//            if(Math.abs(distanceXEnd) <= 2 && Math.abs(distanceYEnd) <= 2){
//                subActionItems.get(i).x = (int) startPoints.get(subActionItems.size()-1)[0];
//                subActionItems.get(i).y = (int) startPoints.get(subActionItems.size()-1)[1];
//            }
            }
            Logger.i("要绘制第"+i+"个，坐标为("+subActionItems.get(i).x+","+subActionItems.get(i).y+")");
            drawPicAndText(canvas, subActionItems.get(i));



//            //图片大小
//            int x = subActionItems.get(i).x;
//            int y = subActionItems.get(i).y;
//            int scaleSize = bitmapWidth / 4;
//            int l = x + scaleSize;
//            int t = y + scaleSize;
//            int r = x + bitmapWidth - scaleSize;
//            int b = y + bitmapHeight - scaleSize;
//////        //显示位置
//            Rect rect1 = new Rect(l, t, r, b);
//            canvas.drawBitmap(bitmap, null, rect1, mPaint);
//
//            //绘制文字
//            Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
//            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
//            Logger.i("top-->" + top + "\n bottom--->" + bottom);
//            int textX = r + bitmapToTextDistance;
//            int textY = (int) (b - rect1.height() / 2 + (-top + bottom) / 2 - bottom);
//            canvas.drawText(subActionItems.get(i).text + "我是第" + i + "个", textX, textY, mPaintText);

            //TODO 处理图片显示不全
        }


    }

    //subActionItems.get(i).text + "我是第" + i + "个"
    private void drawPicAndText(Canvas canvas, ActionItem item) {
        //图片大小
//        int x = subActionItems.get(i).x;
//        int y = subActionItems.get(i).y;

        int bitmapWidth = item.bitmap.getWidth();
        int bitmapHeight = item.bitmap.getHeight();
        int scaleSize = bitmapWidth / 4;
        int l = item.x + scaleSize;
        int t = item.y + scaleSize;
        int r = item.x + bitmapWidth - scaleSize;
        int b = item.y + bitmapHeight - scaleSize;
////        //显示位置
        Rect rect1 = new Rect(l, t, r, b);
        canvas.drawBitmap(item.bitmap, null, rect1, mPaint);

        //绘制文字
        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        Logger.i("top-->" + top + "\n bottom--->" + bottom);
        int textX = r + bitmapToTextDistance;
        int textY = (int) (b - rect1.height() / 2 + (-top + bottom) / 2 - bottom);
        canvas.drawText(item.text, textX, textY, mPaintText);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    public Point getActionViewCenter() {
        return new Point(width / 2, height / 2);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Logger.e("onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Logger.e("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Logger.e("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Logger.e("onScroll");
        isFirst = false;
        count += -distanceY;
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,"rotation",distanceY);
//        objectAnimator.setDuration(1000);
//        objectAnimator.start();
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Logger.e("onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Logger.e("onFling");
        return false;
    }


    static class ActionItem {


        public int x;
        public int y;
        public int drawableId = R.drawable.icon;
        public int width;
        public int height;
        public String text = "我是要绘制的文本";
        public Bitmap bitmap;

        public ActionItem(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }
}
