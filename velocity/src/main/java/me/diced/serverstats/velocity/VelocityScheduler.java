package me.diced.serverstats.velocity;

import com.velocitypowered.api.scheduler.ScheduledTask;
import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class VelocityScheduler implements Scheduler {
    private final VelocityServerStats platform;
    private final com.velocitypowered.api.scheduler.Scheduler scheduler;

    public VelocityScheduler(VelocityServerStats platform) {
        this.platform = platform;
        this.scheduler = platform.server.getScheduler();
    }

    @Override
    public Task scheduleRepeatingTask(Runnable task, long interval, TimeUnit unit) {
        ScheduledTask sct = this.scheduler.buildTask(this.platform, task).repeat(interval, unit).schedule();
        return () -> sct.cancel();
    }

    @Override
    public Task schedule(Runnable task) {
        ScheduledTask sct = this.scheduler.buildTask(this.platform, task).schedule();
        return () -> sct.cancel();
    }

    @Override
    public void stop() {

    }
}
