package me.diced.serverstats.common.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadScheduler implements Scheduler {
    private final ScheduledThreadPoolExecutor scheduler;

    public ThreadScheduler() {
        this.scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ServerStats-Worker").build());
    }

    public Task scheduleRepeatingTask(Runnable task, long interval, TimeUnit unit) {
        ScheduledFuture<?> future = this.scheduler.scheduleAtFixedRate(task, interval, interval, unit);
        return () -> future.cancel(false);
    }

    public Task schedule(Runnable task) {
        ScheduledFuture<?> future = this.scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);
        return () -> future.cancel(false);
    }

    public void stop() {
        this.scheduler.shutdown();
    }
}
