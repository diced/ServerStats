package me.diced.serverstats.common;

import me.diced.serverstats.common.exporter.Stats;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public interface ServerStatsPlatform {
    Path getConfigPath();
    Stats getStats();
    ServerStatsType getType();
    void infoLog(String msg);

    void start();
    void stop();
}
