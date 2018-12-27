package com.mayi.jack.redpackage.wx.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vcyber.baselibrary.utils.Logger;

import java.util.List;

/**
 * author: JACK
 * eamil: 1215530740@qq.com
 * date: 2018/12/27 15:07
 * des:
 */
public class RedPackageService extends AccessibilityService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onServiceConnected() {
        //系统成功连接到辅助功能服务时调用
        super.onServiceConnected();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //当系统检测到与Accessibility服务指定的事件过滤参数
        // 匹配的AccessibilityEvent时调用
        Logger.e("开始操作微信了-->" + event.getClassName());

        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
//        event.getClassName();
        int eventType = event.getEventType();
        switch (eventType) {
            //通知栏来信息，判断是否含有微信红包字样，是的话跳转
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                break;
            //界面跳转的监听
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                if (event.getClassName() == "com.tencent.mm.ui.LauncherUI") {
                    List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/azn");
                    Logger.e("找到可操作对象--》"+list.size());
                    if(list!=null&&list.size()>0){
                        for(AccessibilityNodeInfo info : list){
                            Logger.e("info.getText().toString()--->"+info.getText().toString());
                            if(info.getText().toString().equals("*  小太阳: [图片]")){
                                info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                            }
                        }
                    }
//                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                List<AccessibilityNodeInfo> list1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/azn");
                Logger.e("找到可操作对象--》"+list1.size());
                if(list1!=null&&list1.size()>0){
                    for(AccessibilityNodeInfo info : list1){
                        Logger.e("info.getText().toString()--->"+info.getText().toString());
                        if(info.getText().toString().equals("*  小太阳: [图片]")){
                            info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                        }
                    }
                }
                break;

        }

    }

    @Override
    public void onInterrupt() {
        //当系统想要中断服务提供的反馈时调用
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //当系统即将关闭辅助功能服务时调用
    }


}
