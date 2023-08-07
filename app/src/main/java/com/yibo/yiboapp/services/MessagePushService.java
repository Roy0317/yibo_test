package com.yibo.yiboapp.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.yibo.yiboapp.activity.ActivePageActivity;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.activity.MessageCenterActivity;
import com.yibo.yiboapp.activity.NoticesPageActivity;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.DiscountNotificationBean;
import com.yibo.yiboapp.entify.PushInboxBean;
import com.yibo.yiboapp.entify.WebSiteNotificationBean;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.PushUtil;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.services
 * @description: 推送信息的服务
 * @date: 2019/2/18
 * @time: 1:52 PM
 */
public class MessagePushService extends Service {


    private YiboPreference yiboPreference;
    private Timer timer;
    private TimerTask timerTask;

    /**
     * 站内信
     */
    public static final int INBOX_CODE = 10;

    /**
     * 网站公告
     */
    public static final int WEBSITE_CODE = 11;

    /**
     * 优惠活动
     */
    public static final int DISCOUNT_CODE = 12;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        yiboPreference = YiboPreference.instance(this);

        //Android 8.0以上需要单独适配通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "normal";
            String channelName = "消息推送";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }

        String interval ;

        try {
            interval = UsualMethod.getConfigFromJson(this).getSwitch_push_interval();
        } catch (Exception e) {
            e.printStackTrace();
            interval = "1";
        }



        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getMessagePush();
            }
        };
        timer.schedule(timerTask, 0, (long) (Float.parseFloat(interval) * 1000 * 60));


    }


    //后台轮训获取本地消息推送
    private void getMessagePush() {
        ApiParams params = new ApiParams();
        params.put("curVersion", Utils.getVersionName(this));
        params.put("appID", BuildConfig.APPLICATION_ID);
        params.put("platform", "android");

        HttpUtil.get(this, Urls.PUSH_DATAS_V2, params, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    YiboPreference.instance(MessagePushService.this).setToken(result.getAccessToken());
                    String title = null;
                    String text = null;
                    String url = null;

                    PushInboxBean pushInboxBean ;
                    WebSiteNotificationBean webSiteNotificationBean;
                    DiscountNotificationBean discountNotificationBean;

                    try {
                        JSONArray jsonArray = new JSONArray(result.getContent());
                        Log.e("123123", "receive: "+jsonArray.length());
                        a:
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            int code = jsonObject.optInt("code");
                            switch (code) {
                                case INBOX_CODE: {
                                    pushInboxBean = new Gson().fromJson(jsonObject.toString(), PushInboxBean.class);
                                    if (null == pushInboxBean.getData()) {
                                        continue a;
                                    }
                                    if (null == pushInboxBean.getData().getPush()) {
                                        continue a;
                                    }
                                    int typeId = pushInboxBean.getData().getPush().getId();
                                    title = "你有新的站内信";
                                    text = pushInboxBean.getData().getPush().getTitle();
                                    String id = yiboPreference.getInboxMessageId();
                                    if (id.contains(",")) {
                                        String[] ids = id.split(",");
                                        for (String s : ids) {
                                            if (Integer.parseInt(s) == typeId) {
                                                continue a;
                                            }
                                        }
                                    }
                                    ShortcutBadger.applyCount(getApplicationContext(),++PushUtil.pushNumb);
                                    yiboPreference.saveInboxMessageId(id + typeId + ",");
                                    break;
                                }
                                case WEBSITE_CODE: {
                                    webSiteNotificationBean = new Gson().fromJson(jsonObject.toString(), WebSiteNotificationBean.class);

                                    if (null == webSiteNotificationBean.getData()) {
                                        continue a;
                                    }
                                    if (null == webSiteNotificationBean.getData().getPush()) {
                                        continue a;
                                    }
                                    int typeId = webSiteNotificationBean.getData().getPush().getId();
                                    title = "你有新的公告";
                                    text = webSiteNotificationBean.getData().getPush().getTitle();


                                    String id = yiboPreference.getWebsiteNoticeMessageId();
                                    if (id.contains(",")) {
                                        String[] ids = id.split(",");
                                        for (String s : ids) {
                                            if (Integer.parseInt(s) == typeId) {
                                                continue a;
                                            }
                                        }
                                    }
                                    ShortcutBadger.applyCount(getApplicationContext(),++PushUtil.pushNumb);
                                    yiboPreference.saveWebsiteNotice(id + typeId + ",");
                                    break;
                                }
                                case DISCOUNT_CODE: {
                                    discountNotificationBean = new Gson().fromJson(jsonObject.toString(), DiscountNotificationBean.class);
                                    if (null == discountNotificationBean.getData()) {
                                        continue a;
                                    }
                                    if (null == discountNotificationBean.getData().getPush()) {
                                        continue a;
                                    }
                                    int typeId = discountNotificationBean.getData().getPush().getId();

                                    title = "你有新的优惠活动";
                                    text = discountNotificationBean.getData().getPush().getTitle();

                                    String id = yiboPreference.getDiscoutMessageId();
                                    if (id.contains(",")) {
                                        String[] ids = id.split(",");
                                        for (String s : ids) {
                                            if (Integer.parseInt(s) == typeId) {
                                                continue a;
                                            }
                                        }
                                    }
                                    ShortcutBadger.applyCount(getApplicationContext(),++PushUtil.pushNumb);
                                    yiboPreference.saveDiscountMessageId(id + typeId + ",");
                                    break;
                                }

                            }

                            sendNotification(code, title, text, url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
            }
        });


    }




    /**
     * 发起本地通知
     */
    public void sendNotification(int id, String title, String message, String updateWebsite) {
        Intent intent = null;
        PendingIntent contentIntent;
        Log.e("123", "sendNotification: ");
        switch (id) {
            //站内信
            case INBOX_CODE:
                intent = new Intent(getApplicationContext(), MessageCenterActivity.class);
                break;
                //最新公告
            case WEBSITE_CODE:
                intent = new Intent(getApplicationContext(), NoticesPageActivity.class);
                break;
                //优惠活动
            case DISCOUNT_CODE:
                intent = new Intent(getApplicationContext(), ActivePageActivity.class);
                break;
//            case VERSION_UPDATE_CODE:
//                IntentFilter filter = new IntentFilter(updateWebsite);
//                intent = new Intent(updateWebsite);
//                myBroadCastReceiver = new MyBroadCastReceiver();
//                registerReceiver(myBroadCastReceiver, filter);
//                isRegisterBroadcast = true;
//                break;
        }

        contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "normal")
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .build();
        if (manager != null) {
            Random random=new Random();
            random.nextInt();
            manager.notify(new Random().nextInt(), notification);
        }
    }


    /**
     * Android 8.0以上的版本需要设置通知渠道
     *
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
