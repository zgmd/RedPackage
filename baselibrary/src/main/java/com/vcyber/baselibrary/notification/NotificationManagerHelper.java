package com.vcyber.baselibrary.notification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.vcyber.baselibrary.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationManagerHelper {

    static NotificationManager manager;

    private static NotificationManagerHelper helper;

    private NotificationManagerHelper(){
        if(helper==null){
            synchronized (NotificationManagerHelper.class){
                if(helper==null){
                    helper = new NotificationManagerHelper();

                }
            }
        }
    }

    public static NotificationManagerHelper getHelper(Application application){
        if(manager==null){
            manager  = (NotificationManager)application.getSystemService(NOTIFICATION_SERVICE);
        }
        return helper;
    }


    /**
     * 8.0及以上，需要创建channel
     * @param application
     * @param channelId
     * @param channelName
     * @param importance
     */
    public static void createNotificationChannel(Application application,String channelId,String channelName,int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            if(channelId.equals("subscribe")){
                channel.setSound(null,null);
            }
            channel.setShowBadge(true);
            NotificationManager notificationManager = (NotificationManager) application.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * 发送普通通知
     * @param application
     * @param channelId
     * @param contentTitle
     * @param contentText
     * @param smallIcon
     * @param largeIcon
     */
    public static void sendNotification(Application application,
                                        String channelId,String contentTitle,
                                        String contentText,int smallIcon,int largeIcon,Intent resultIntent) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                application, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(application, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(application.getResources(),largeIcon))
                .setAutoCancel(true)
                .setNumber(1)
                .setContentIntent(resultPendingIntent)
                .build();
        manager.notify(NotificationConstant.COMMENT_NOTIFICATION, notification);
    }


    public static void sendBigNotification(Application application,
                                    String channelId,String contentTitle,
                                    String contentText,int smallIcon,int largeIcon,Intent dismissIntent,Intent snoozeIntent ){
//        dismissIntent.setAction();
        PendingIntent piDismiss = PendingIntent.getService(
                application, 0, dismissIntent, 0);
        snoozeIntent.setAction("ACTION_SNOOZE");
        PendingIntent piSnooze =
                PendingIntent.getService(application, 0, snoozeIntent, 0);
        Notification notification = new NotificationCompat.Builder(application, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(application.getResources(),largeIcon))
                .setAutoCancel(true)
                .setNumber(1)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("big text"))
                //添加Action Button
                .addAction (R.drawable.icon,
                        "ACTION_DISMISS", piDismiss)
                .addAction (R.drawable.icon,
                        "ACTION_SNOOZE", piSnooze)
                .build();
        manager.notify(23, notification);
    }


    /**
     * 大文本通知
     * @param application
     * @param intent
     */
    public static void bigTextStyle(Application application,Intent intent){

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(application);
        NotificationCompat.Builder  builder =  new NotificationCompat.Builder(application, "chat");
        builder.setContentTitle("BigTextStyle");
        builder.setContentText("BigTextStyle演示示例");
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(application.getResources(),R.drawable.icon));
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle("点击后的标题");
        //SummaryText没什么用 可以不设置
        style.setSummaryText("末尾只一行的文字内容");
        builder.setStyle(style);
        builder.setAutoCancel(true);
//        Intent intent = new Intent(this,SettingsActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(application,1,intent,0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manager.notify(NotificationConstant.BIG_TEXT_NOTIFICATION,notification);
    }


    /**
     * 大图 通知
     * @param application
     * @param intent
     */
    public static void bigPictureStyle(Application application,Intent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(application,"subscribe");
        builder.setContentTitle("BigPictureStyle");
        builder.setContentText("BigPicture演示示例");
        builder.setSmallIcon(R.drawable.icon);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(application.getResources(),R.drawable.icon));
        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(application.getResources(),R.drawable.icon));
        builder.setStyle(style);
        builder.setAutoCancel(true);
//        Intent intent = new Intent(this,ImageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(application,1,intent,0);
        //设置点击大图后跳转
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        manager.notify(NotificationConstant.BIG_PICTURE_NOTIFICATION,notification);
    }


    /**
     * 横幅通知
     * @param application
     * @param intent
     */
    public static void hangupStyle(Application application,Intent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(application,"chat");
        builder.setContentTitle("横幅通知");
        builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(application.getResources(),R.drawable.icon));
//        Intent intent = new Intent(this,ImageActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(application,1,intent,0);
        builder.setContentIntent(pIntent);
        //这句是重点
        builder.setFullScreenIntent(pIntent,false);
        builder.setAutoCancel(true);
//        builder.setWhen(0);
        Notification notification = builder.build();
        manager.notify(99,notification);
    }

    /**
     * 下载进度，通知
     */
    public static void downProgress(Application application,int progress){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(application,"subscribe");
        builder.setSmallIcon(R.drawable.icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(application.getResources(),R.drawable.icon));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //取消右上角的时间显示
        builder.setShowWhen(false);
        builder.setContentTitle("下载中..."+progress+"%");
        builder.setContentText("lallala");
        builder.setProgress(100,progress,false);
//        builder.setContentInfo(progress+"%");
        builder.setOngoing(true);
//        builder.setShowWhen(false);
//        Intent intent = new Intent(this,DownloadService.class);
//        intent.putExtra("command",1);
        Notification notification = builder.build();
        manager.notify(NotificationConstant.DOWN_NOTIFICATION,notification);

    }

    /**
     * 移除指定id的通知
     * @param id
     */
    public static void removeNotification(int id){
        if(manager!=null){
            manager.cancel(id);
        }

    }
}
