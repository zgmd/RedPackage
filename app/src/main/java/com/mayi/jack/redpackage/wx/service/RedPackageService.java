package com.mayi.jack.redpackage.wx.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.vcyber.baselibrary.utils.Logger;
import com.vcyber.baselibrary.widget.previewImage.Info;

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
        Logger.e("onServiceConnected()--->服务连接成功");
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //当系统检测到与Accessibility服务指定的事件过滤参数
        // 匹配的AccessibilityEvent时调用


        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
//        event.getClassName();
        int eventType = event.getEventType();
        switch (eventType) {
            //通知栏来信息，判断是否含有微信红包字样，是的话跳转
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Logger.e("开始操作微信了-->" + "通知栏来信息");
                List<CharSequence> texts = event.getText();
                for (CharSequence text : texts) {
                    String content = text.toString();
                    if (!TextUtils.isEmpty(content)) {
                        //判断是否含有[微信红包]字样
                        if (content.contains("[微信红包]")) {
                            //如果有则打开微信红包页面
                            WXHelper.openWeChatPage(event);
                        }
                    }
                }
                break;
            //界面跳转的监听
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Logger.e("开始操作微信了-->" + "界面跳转的监听");
                Logger.e("开始操作微信了-->" + event.getClassName());
                //红包窗口页面
                if(WXConstant.WX_RP_OPEN_UI_CLASSNAME.equals(event.getClassName())){
//                    WXHelper.RPWindow(getRootInActiveWindow());

                    List<AccessibilityWindowInfo>  accessibilityWindowInfos =  getWindows();
                    Logger.e("info--->"+accessibilityWindowInfos.toString());
                    for(AccessibilityWindowInfo info:accessibilityWindowInfos){
                        WXHelper.RPWindow(info.getRoot());
                        AccessibilityNodeInfo info_ =  info.getRoot();
                        Logger.e("info--->"+info_.toString());
                    }
                }
//                WXHelper.chatListToChatUI(rootNodeInfo);
//                WXHelper.ChatUIOpenWindow(rootNodeInfo);
//                WXHelper.RPWindowBack(rootNodeInfo);
//                WXHelper.RPWindowRecordAndBack(rootNodeInfo);

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Logger.e("开始操作微信了-->" + "界面内容改变监听");
               boolean isLauncher =  WXHelper.isLauncher(rootNodeInfo);
               if(isLauncher){

               }
//                Logger.e("开始操作微信了-->" + event.getClassName());
//                List<AccessibilityNodeInfo> list1 = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/azn");
////                Logger.e("找到可操作对象--》"+list1.size());
//                if(list1!=null&&list1.size()>0){
//                    for(AccessibilityNodeInfo info : list1){
////                        Logger.e("info.getText().toString()--->"+info.getText().toString());
//                        if(info.getText().toString().contains("微信红包")){
//                            info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//
//                        }
//                    }
//                }


                break;

        }

    }

    @Override
    public void onInterrupt() {
        //当系统想要中断服务提供的反馈时调用
        Logger.e("onInterrupt()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //当系统即将关闭辅助功能服务时调用
        Logger.e("onDestroy()");
    }


}
