package me.diced.serverstats.bukkit;

import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.Task;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class BukkitScheduler implements Scheduler {
    private final BukkitServerStats platform;
    private final org.bukkit.scheduler.BukkitScheduler scheduler;

    public BukkitScheduler(BukkitServerStats platform) {
        this.platform = platform;
        this.scheduler = platform.getServer().getScheduler();
    }

    @Override
    public Task scheduleRepeatingTask(Runnable task, long interval, TimeUnit unit) {
        long time = 0;

        if (unit == TimeUnit.MILLISECONDS) {
            time = (interval / 1000) * 20;
        }

        BukkitTask sct = this.scheduler.runTaskTimer(this.platform, task, 0, time);
        return () -> sct.cancel();
    }

    @Override
    public Task schedule(Runnable task) {
        BukkitTask sct = this.scheduler.runTaskAsynchronously(this.platform, task);
        return () -> sct.cancel();
    }

    @Override
    public void stop() {

    }
}
