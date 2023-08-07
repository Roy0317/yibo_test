package com.yibo.yiboapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.Nullable;

import android.util.SparseArray;
import android.view.View;

import com.yibo.yiboapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewCache {

    private static final String TAG = "ViewCache";


    private final ExecutorService service;


    private final SparseArray<List<View>> mStack;


    public ViewCache() {
        service = Executors.newSingleThreadExecutor();
        mStack = new SparseArray<>(2);
    }

//
    private static class LazyHolder {
        private static final ViewCache INSTANCE = new ViewCache();
    }
//
//
    public static ViewCache getInstance() {
        return LazyHolder.INSTANCE;
    }


    /**
     * 获取缓存View
     *
     * @return View
     */


    @Nullable
    public synchronized View popAndAquire(Configuration configuration, Context activity) {
        List<View> views = mStack.get(configuration.orientation);
        push(configuration, activity);

        if (views != null && views.size() > 0) {
            return views.remove(views.size() - 1);
        }

        return null;
    }


    private int type = 0 ;
    /**
     * 在其他线程进行View初始化
     *
     * @param configuration Configuration
     */
    public void push(final Configuration configuration, final Context activity) {

        service.execute(new Runnable() {
            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                long startTime = SystemClock.uptimeMillis();
                View view ;
                if (type==0){
                     view = View.inflate(activity, R.layout.table_layout_simple, null);
                }else if (type==1){
                    //1为聊天室
                    view = View.inflate(activity,R.layout.chat_table_layout_simple,null);
                }else if (type==2){
                    //2为经典版
                    view = View.inflate(activity,R.layout.table_layout,null);
                }else{
                    view = View.inflate(activity, R.layout.table_layout_simple, null);
                }
                long endTime = SystemClock.uptimeMillis() - startTime;
                List<View> views = mStack.get(configuration.orientation);
                if (views == null) {
                    views = new ArrayList<>();
                    mStack.put(configuration.orientation, views);
                }
                views.add(view);
//                Log.i(TAG, "push finished, inflate time=" + endTime + " size=" + views.size());
            }
        });
    }

    public void setType(int type){
        this.type = type;
    }

    public void push(Configuration configuration, boolean onlyNotExist, Context context) {
        if (onlyNotExist && isInstanceExisted(configuration)) {
            return;
        }
        push(configuration, context);
    }


    public synchronized void push(Configuration configuration, int maxStackSize, Context context) {
        if (maxStackSize <= 0) {
            return;
        }
        List<View> views = mStack.get(configuration.orientation);
        if (views != null && views.size() >= maxStackSize) {
            return;
        }
        push(configuration, context);
    }


    public synchronized boolean isInstanceExisted(Configuration configuration) {
        if (configuration == null) {
            return false;
        }
        List<View> views = mStack.get(configuration.orientation);
        return views != null && views.size() != 0;
    }
}
