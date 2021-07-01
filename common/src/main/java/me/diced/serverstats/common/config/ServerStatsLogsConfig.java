package me.diced.serverstats.common.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerStatsLogsConfig {
    public boolean writeLogs = false;
    public String writeLog = "Wrote stats";
}
