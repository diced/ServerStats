package me.diced.serverstats.common.config;


import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerStatsConfig {
    public ServerStatsWebServerConfig webServer;
    public ServerStatsPushableConfig pushable;

    public ServerStatsLogsConfig logs;

    public int interval = 15000;
}
