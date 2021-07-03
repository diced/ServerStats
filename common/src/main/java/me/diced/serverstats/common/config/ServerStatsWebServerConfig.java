package me.diced.serverstats.common.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerStatsWebServerConfig {
    public int port = 8000;
    public String hostname = "0.0.0.0";
    public String route = "/metrics";
}
