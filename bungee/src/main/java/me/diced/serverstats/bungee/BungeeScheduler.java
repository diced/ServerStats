package me.diced.serverstats.bungee;

import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.Task;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;

public class BungeeScheduler implements Scheduler {
    private final BungeeServerStats platform;
    private final TaskScheduler scheduler;
    public BungeeScheduler(BungeeServerStats platform) {
        this.platform = platform;
        this.scheduler = platform.getProxy().getScheduler();
    }

    @Override
    public Task scheduleRepeatingTask(Runnable task, long interval, TimeUnit unit) {
        ScheduledTask sct = this.scheduler.schedule(this.platform, task, 0, interval, unit);
        return () -> sct.cancel();
    }

    @Override
    public Task schedule(Runnable task) {
        ScheduledTask sct = this.scheduler.runAsync(this.platform, task);
        return () -> sct.cancel();
    }

    @Override
    public void stop() {

    }
}
