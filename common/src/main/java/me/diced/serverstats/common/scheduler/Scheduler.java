package me.diced.serverstats.common.scheduler;

import java.util.concurrent.TimeUnit;

public interface Scheduler {
    Task scheduleRepeatingTask(Runnable task, long interval, TimeUnit unit);
    Task schedule(Runnable task);
    void stop();
}
