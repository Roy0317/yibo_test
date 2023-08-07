package crazy_wrapper.Crazy.network;

import crazy_wrapper.Crazy.Utils.Utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程管理类
 * 目前以单线程单并发方式进行组文件上传管理
 * @author zhangy
 *
 */
public class LoadDataManager {

	public static final String TAG = "LoadDataManager";
	private static LoadDataManager manager;
	static Executor pool = null;
	public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY -2;
	public static final int DEFAULT_THREAD_POOL_SIZE = 5;

	public static LoadDataManager getInstance(){
		if(manager == null){
			manager = new LoadDataManager();
		}
		return manager;
	}   
	
	public LoadDataManager(){
		pool = createExecutor(DEFAULT_THREAD_POOL_SIZE,DEFAULT_THREAD_PRIORITY);
	}

	/** Creates default implementation of task executor */
	public Executor createExecutor(int threadPoolSize, int threadPriority) {
		BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue,
			createThreadFactory(threadPriority, "uil-pool-"));
	}

	/** Creates default implementation of task distributor */
//	public static Executor createTaskDistributor() {
//		return Executors.newCachedThreadPool(createThreadFactory(Thread.NORM_PRIORITY, "uil-pool-d-"));
//	}

	public void addTask(Runnable task){
		pool.execute(task);
	}
	
	public void release(){
		if(null != pool){
			((ExecutorService)pool).shutdownNow();
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
			Utils.writeLogToFile(TAG,"create new thread from pool ,thread name = "+tName+"priority = "+threadPriority);
			Thread t = new Thread(group, r, tName, 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}

	}
	
}
