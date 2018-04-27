package com.sony.threadPool;

import java.util.concurrent.*;

/**
 * @author wyj
 * @date 4/27/2018
 */
public class SessionThreadPool {

    public static final SessionThreadPool instance = new SessionThreadPool();

    private ThreadPoolExecutor sessionStartThreadPool = null;

    private ScheduledExecutorService sessionStopThreadPool = null;

    private SessionThreadPool(){}

    /**
     * 获取进行定时任务的线程池
     *
     * @return
     */
    public ScheduledExecutorService getSendStopSessionThreadPool(){
        synchronized (SessionThreadPool.class){
            if (this.sessionStopThreadPool == null){
                this.sessionStopThreadPool = new ScheduledThreadPoolExecutor(0);
            }
            return this.sessionStopThreadPool;
        }
    }

    public ThreadPoolExecutor getSendStartSessionThreadPool(){
        synchronized (SessionThreadPool.class){
            if (this.sessionStartThreadPool == null){
                this.sessionStartThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,0, TimeUnit.SECONDS, new SynchronousQueue<>());
            }
            return this.sessionStartThreadPool;
        }
    }
}
