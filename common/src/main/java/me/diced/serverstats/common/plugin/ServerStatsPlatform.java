package me.diced.serverstats.common.plugin;

import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.scheduler.Scheduler;

import java.nio.file.Path;

public interface ServerStatsPlatform {
    Path getConfigPath();
    Scheduler getScheduler();
    ServerStatsMetadata getMetadata();

    Stats getStats();
    ServerStats getServerStats();

    void infoLog(String msg);
    void start();
}
