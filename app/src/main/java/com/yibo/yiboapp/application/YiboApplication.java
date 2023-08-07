package com.yibo.yiboapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.smtt.sdk.QbSdk;
import com.wanjian.cockroach.Cockroach;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.ExceptionHandler;
import com.yibo.yiboapp.views.gestureview.DefaultPatternCheckingActivity;

/**
 * Created by johnson on 2017/11/15.
 */

public class YiboApplication extends Application {

    private static final String TAG = "YiboApplication";

    private static YiboApplication app;
    private Activity app_activity = null;
    private int activityCount = 0;
    public boolean isScreenOff = false;
    private boolean isForeground = false;
    private long backgroundStamp;
    private long lastBackgroundStamp;
    private ExceptionHandler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        handler = ExceptionHandler.getInstance(this);
        initGlobeActivity();
        Fresco.initialize(this);
        install();
//        handler.init(this);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);


    }
    public static YiboApplication getInstance() {
        return app;
    }



    private void install() {
        Cockroach.install(new Cockroach.ExceptionHandler() {
            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            Log.e("AndroidRuntime","--->CockroachException:"+thread+"<---",throwable);
//                            Toast.makeText(YiboApplication.this, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
                            handler.collectDeviceInfo(YiboApplication.this);
                            handler.saveCrashInfo2File(throwable);
//                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });


    }

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                app_activity = activity;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                app_activity = activity;

            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
                app_activity = activity;
                activityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
                SysConfig configFromJson = UsualMethod.getConfigFromJson(activity);


                String gesture_pwd_switch = "on";
                if (configFromJson!=null){
                    gesture_pwd_switch = configFromJson.getGesture_pwd_switch();
                }

                if (gesture_pwd_switch.equalsIgnoreCase("off")){
                    return;
                }

                if (activity.getLocalClassName().equals(DefaultPatternCheckingActivity.class.getCanonicalName())) {
                    return;
                }

                backgroundStamp = System.currentTimeMillis();
                String sptimeLock = YiboPreference.instance(activity).getSptimeLock();
                long timeDiff = backgroundStamp - lastBackgroundStamp;
//
//                Log.e(TAG, "activityCount:" + activityCount);
//                Log.e(TAG, "activityName:" + activity.getLocalClassName());
//                Log.e(TAG, "timeDiff:" + timeDiff + "");
//                Log.e(TAG, "sptimeLock:" + sptimeLock);
                //activityCount=1 前台
                if (activityCount == 1) {
                    //登录并且不是试玩
                    if (YiboPreference.instance(activity).isLogin() &&
                            YiboPreference.instance(activity).getAccountMode() != Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                        //时间大于后台设置时间
                        if (timeDiff > Long.parseLong(sptimeLock)) {
                            //开启了手势密码，并且有内容
                            if (YiboPreference.instance(activity).userIsCheckedGesture() &&
                                    YiboPreference.instance(activity).userIsSetGesture()) {
                                activity.startActivity(new Intent(activity, DefaultPatternCheckingActivity.class).
                                        putExtra("type", DefaultPatternCheckingActivity.BACKGROUND_GESTURE));
                            }
                        }
                    }

                }

                backgroundStamp = 0L;
                lastBackgroundStamp = 0L;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                app_activity = activity;

            }

            @Override
            public void onActivityStopped(Activity activity) {
                app_activity = activity;
                activityCount--;
                if (activityCount == 0) {
                    lastBackgroundStamp = System.currentTimeMillis();
                    isForeground = false;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return app_activity;
    }



}
