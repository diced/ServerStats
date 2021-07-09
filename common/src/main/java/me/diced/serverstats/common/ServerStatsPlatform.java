package me.diced.serverstats.common;

import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.scheduler.Scheduler;

import java.nio.file.Path;

public interface ServerStatsPlatform {
    Path getConfigPath();
    Stats getStats();
    ServerStatsType getType();
    String getVersion();
    String getAuthor();

    ServerStats getServerStats();
    Scheduler getScheduler();
    void infoLog(String msg);

    void start();
}
