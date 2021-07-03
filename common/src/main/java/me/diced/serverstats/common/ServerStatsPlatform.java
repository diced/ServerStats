package me.diced.serverstats.common;

import me.diced.serverstats.common.exporter.Stats;

import java.nio.file.Path;

public interface ServerStatsPlatform {
    Path getConfigPath();
    Stats getStats();
    ServerStatsType getType();
    String getVersion();
    String getAuthor();
    void infoLog(String msg);

    void start();
    void stop();
}
