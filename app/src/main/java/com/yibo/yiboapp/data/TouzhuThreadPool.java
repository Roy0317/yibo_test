package com.yibo.yiboapp.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import crazy_wrapper.Crazy.Utils.Utils;

/**
 * 线程管理类
 *
 * @author johnson
 */
public class TouzhuThreadPool {

    public static final String TAG = "TouzhuThreadPool";
    private static TouzhuThreadPool manager;
    static Executor pool = null;
    public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 2;
    public static final int DEFAULT_THREAD_POOL_SIZE = 5;

    public static TouzhuThreadPool getInstance() {
        if (manager == null) {
            manager = new TouzhuThreadPool();
        }
        return manager;
    }

    public TouzhuThreadPool() {
        pool = createExecutor(DEFAULT_THREAD_POOL_SIZE, DEFAULT_THREAD_PRIORITY);
    }

    /**
     * Creates default implementation of task executor
     */
    public Executor createExecutor(int threadPoolSize, int threadPriority) {
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue,
                createThreadFactory(threadPriority, "uil-pool-"));
    }

    /**
     * Creates default implementation of task distributor
     */
//	public static Executor createTaskDistributor() {
//		return Executors.newCachedThreadPool(createThreadFactory(Thread.NORM_PRIORITY, "uil-pool-d-"));
//	}
    public void addTask(Runnable task) {
        pool.execute(task);
    }

    public void release() {
        if (null != pool) {
            ((ExecutorService) pool).shutdownNow();
        }
        manager = null;
    }

    private ThreadFactory createThreadFactory(int threadPriority, String threadNamePrefix) {
        return new DefaultThreadFactory(threadPriority, threadNamePrefix);
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final int threadPriority;

        DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
            this.threadPriority = threadPriority;
            group = Thread.currentThread().getThreadGroup();
            namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            String tName = namePrefix + threadNumber.getAndIncrement();
            Utils.writeLogToFile(TAG, "create new thread from pool ,thread name = " + tName + "priority = " + threadPriority);
            Thread t = new Thread(group, r, tName, 0);
            if (t.isDaemon()) t.setDaemon(false);
            t.setPriority(threadPriority);
            return t;
        }

    }

}
