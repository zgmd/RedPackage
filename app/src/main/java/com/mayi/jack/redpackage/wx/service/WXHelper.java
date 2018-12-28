package com.mayi.jack.redpackage.wx.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.vcyber.baselibrary.utils.Logger;

import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * author: JACK
 * eamil: 1215530740@qq.com
 * date: 2018/12/28 10:23
 * des:
 */
public class WXHelper {


    /**
     * 窗口自动开启红包所在的聊天页面
     */
    public static void openWeChatPage(AccessibilityEvent event) {
        //A instanceof B 用来判断内存中实际对象A是不是B类型，常用于强制转换前的判断
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            //打开对应的聊天界面
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }


    //1.聊天列表页面跳转到聊天页面
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void chatListToChatUI(AccessibilityNodeInfo nodeInfo){
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.chat_list_RP_ID);
        if(list!=null&&list.size()>0){
            for(AccessibilityNodeInfo info : list){
                if(info.getText().toString().contains(WXConstant.chat_list_RP_text)){
                    //模拟点击进入聊天页面
                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
    //2.聊天页面点击微信红包
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void ChatUIOpenWindow(AccessibilityNodeInfo nodeInfo){
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.chat_list_RP_ID);
        if(list!=null&&list.size()>0){
            for(AccessibilityNodeInfo info : list){
                if(info.getText().toString().contains(WXConstant.chat_UI_RP_text)){
                    //模拟点击
                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
    //3.点击开按钮
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void RPWindow(AccessibilityNodeInfo nodeInfo){
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.chat_UI_RP_window_open_id);
////        nodeInfo.find
//        Logger.e("------>"+list.size());
//        for(AccessibilityNodeInfo info:list){
//            Logger.e("------>"+info.getClassName());
//        }
//        nodeInfo.getChildCount();
        Logger.e("------->"+(nodeInfo==null));
        if(nodeInfo==null||nodeInfo.getChildCount()==0){
            Logger.e("跳出遍历");
            return;
        }
        for(int i = 0;i<nodeInfo.getChildCount();i++){
            AccessibilityNodeInfo childInfo =   nodeInfo.getChild(i);
            Logger.e("子空间的名称---》"+childInfo.getClassName());
            if(childInfo.getClassName().equals("android.widget.Button")){
                childInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }else{

                RPWindow(childInfo);
            }
        }
//        if(list!=null&&list.size()>0){
//            for(AccessibilityNodeInfo info : list){
//                Logger.e("点击打开红包-->"+info.toString());
//                if(info.getClassName().equals("android.widget.Button")){
//                    //模拟点击
//                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
//            }
//        }
    }
    //4.采集金额，记录时间，次数，做统计
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void RPWindowRecordAndBack(AccessibilityNodeInfo nodeInfo){
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.RP_detail_UI_money_ID);
        if(list!=null&&list.size()>0){
            for(AccessibilityNodeInfo info : list){
                Logger.e("点击打开红包金额-->"+info.getText());

                //TODO 记录数据
                if(info.getText().toString().contains(WXConstant.chat_UI_RP_text)){
                    //模拟点击
//                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

        List<AccessibilityNodeInfo> list_ = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.RP_detail_UI_BACK_ID);
        if(list!=null&&list.size()>0){
            for(AccessibilityNodeInfo info : list_){
                Logger.e("点击打开红包返回键-->"+info.toString());
                if(info.getText().toString().contains(WXConstant.chat_UI_RP_text)){
                    //模拟点击
                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }
    //5.点击红包页面的返回
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void RPWindowBack(AccessibilityNodeInfo nodeInfo){
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.chat_UI_RP_window_close_id);
        if(list!=null&&list.size()>0){
            for(AccessibilityNodeInfo info : list){
                Logger.e("点击红包窗口返回-->"+info.toString());
                if(info.getText().toString().contains(WXConstant.chat_UI_RP_text)){
                    //模拟点击
                    info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean  isLauncher(AccessibilityNodeInfo nodeInfo){
        Logger.e("判断是否为微信主页面-->");
        try{
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(WXConstant.CHAT_UI_PHOTO_id);
            if(list!=null&&list.size()>0){
                    return false;
            }else {
                return true;
            }
        }catch (Exception e){
            Logger.e("判断是否为主页面报错了--》"+e.getMessage());
            return true;
        }

    }
}
