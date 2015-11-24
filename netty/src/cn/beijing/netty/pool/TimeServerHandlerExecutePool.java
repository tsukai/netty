/**
 * 
 */
package cn.beijing.netty.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zukai 2015-11-24
 */
public class TimeServerHandlerExecutePool {
	private ExecutorService executor;
	public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize){
		executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<java.lang.Runnable>(queueSize));
	}
	
	public void execute(Runnable task){
		executor.execute(task);
	}
}
