package com.vcyber.baselibrary.pixeladapter;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Author   : jack
 * Date     : 2018/8/22 15:20
 * E-mail   : 1215530740@qq.com
 * Consult  : https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
 * Describe : 适配工具类，调节density到适配的效果
 * <p>
 * px = density * dp
 * density = dpi / 160
 * px = (dpi / 160) * dp
 */
public class PixelAdapter {
    private static float sRoncompatDennsity;
    private static float sRoncompatScaledDensity;
    public static final int WIDTH = 0X55;
    public static final int HEIGTH = 0X66;

    //此处这两个值，依据具体UI的设计图而定
    private static final int WIDTH_DP = 360;
    private static final int HEIGTH_DP = 640;


    public static void setCustomDensity(int mode, @NonNull Activity activity, final @NonNull Application application) {

        //application
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sRoncompatDennsity == 0) {
            sRoncompatDennsity = appDisplayMetrics.density;
            sRoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sRoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity = 0.0f;
        if (mode == HEIGTH) {
            targetDensity = appDisplayMetrics.heightPixels / HEIGTH_DP;
        } else {
            //计算宽为360dp 同理可以设置高为640dp的根据实际情况
            targetDensity = appDisplayMetrics.widthPixels / WIDTH_DP;
        }


        final float targetScaledDensity = targetDensity * (sRoncompatScaledDensity / sRoncompatDennsity);
        final int targetDensityDpi = (int) (targetDensity * 160);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        appDisplayMetrics.scaledDensity = targetScaledDensity;

        //activity
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
    }


}
