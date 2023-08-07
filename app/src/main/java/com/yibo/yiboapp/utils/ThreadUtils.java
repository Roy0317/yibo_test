package com.yibo.yiboapp.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThreadUtils {

    //主线程做操作
    public static void doOnUIThread(UITask uiTask) {
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UITask>() {
                    @Override
                    public void accept(UITask uiTask){
                        uiTask.doOnUI();
                    }
                }).isDisposed();

    }

    //io线程做操作
    public static void doOnThread(ThreadTask threadTask) {
        Observable.just(threadTask)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ThreadTask>() {
                    @Override
                    public void accept(ThreadTask threadTask) {
                        threadTask.doOnThread();
                    }
                }).isDisposed();
    }

    public interface ThreadTask {
        void doOnThread();
    }

    public interface UITask {
        void doOnUI();
    }
}
