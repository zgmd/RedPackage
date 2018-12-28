package com.mayi.jack.redpackage.wx.service;

/**
 * author: JACK
 * eamil: 1215530740@qq.com
 * date: 2018/12/28 9:59
 * des: 默认基于微信 6.7.5 版本
 */
public class WXConstant {


    //主页面，聊天页面
    public static String WX_Chat_LIST_UI_CLASSNAME = "com.tencent.mm.ui.LauncherUI";
    //红包详情页面
    public static String WX_Chat_UI_CLASSNAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    //红包窗口页面
    public static String WX_RP_OPEN_UI_CLASSNAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    //聊天列表红包组件id
    public static String chat_list_RP_ID = "com.tencent.mm:id/azn";
    //聊天列表红包组件文本
    public static String chat_list_RP_text = "微信红包";


    //聊天UI页面红包组件ID
    public static String chat_UI_RP_ID = "com.tencent.mm:id/alw";
    //聊天UI页面红包组件ID
    public static String chat_UI_RP_text= "领取红包";

    //聊天UI页面红包组件ID
    public static String LauncherUI_text= "微信(";


    //聊天页面红包弹窗--开  android.widget.Button    com.tencent.mm:id/cnu
    public static String chat_UI_RP_window_open_id= "com.tencent.mm:id/cnu";

    //红包详情，金额 ID  android.widget.TextView
    public static String RP_detail_UI_money_ID= "com.tencent.mm:id/ck_";
    //红包详情，返回键 ID  android.widget.ImageView
    public static String RP_detail_UI_BACK_ID= "com.tencent.mm:id/jc";



    //聊天页面红包弹窗--X  android.widget.ImageView
    public static String chat_UI_RP_window_close_id= "com.tencent.mm:id/cli";


    //主页面标示ID 微信（）
    public static String LauncherUI_id= "android:id/text1";
    //聊天页面头像
    public static String CHAT_UI_PHOTO_id= "com.tencent.mm:id/mo";




}
