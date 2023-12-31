package com.simon.utils;

import android.os.Looper;
import android.view.View;
import android.view.ViewConfiguration;


import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Soli on 2016/10/24.
 */

public class RxJavaUtil {

    /**
     * @param delayTime 单位毫秒
     * @param consumer
     */
    public static void delayAction(int delayTime, final Consumer consumer) {
        Observable.timer(delayTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (consumer != null) {
                            consumer.accept(aLong);
                        }
                    }
                });
    }

    /**
     * 可以传递数据
     *
     * @param delayTime
     * @param object
     * @param consumer
     */
    public static void delayAction(int delayTime, final Object object, final Consumer consumer) {
        Observable.timer(delayTime, TimeUnit.MILLISECONDS)
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) throws Exception {
                        return object;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (consumer != null) {
                            consumer.accept(o);
                        }
                    }
                });
    }

    /**
     * @param view
     * @param listener
     */
    public static void click(final View view, final View.OnClickListener listener) {
        RxView.clicks(view)
                .throttleFirst(ViewConfiguration.getDoubleTapTimeout(), TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (listener != null)
                            listener.onClick(view);
                    }
                });
    }

    /**
     * 运行于UI线程
     *
     * @param action
     */
    public static void runOnUiThread(Action action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            try {
                if (action != null)
                    action.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            runThread(AndroidSchedulers.mainThread(), action);
        }
    }

    /**
     * 运行于那个线程
     *
     * @param scheduler
     * @param action
     */
    public static void runThread(Scheduler scheduler, Action action) {
        Observable.empty()
                .observeOn(scheduler)
                .doOnComplete(action)
                .subscribe();
    }

    /**
     * 运行于非ui线程中
     *
     * @param action
     */
    public static void runOnThread(Action action) {
        runThread(Schedulers.io(), action);
    }
}
