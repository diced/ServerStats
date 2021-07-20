package me.diced.serverstats.common.plugin;

import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.scheduler.Scheduler;

import java.nio.file.Path;

public interface ServerStatsPlatform {
    Path getConfigPath();
    Scheduler getScheduler();
    ServerStatsMetadata getMetadata();
    MetricsManager getMetricsManager();

    ServerStats getServerStats();
    void start();
}
