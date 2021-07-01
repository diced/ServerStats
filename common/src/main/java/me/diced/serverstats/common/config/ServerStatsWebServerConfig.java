package me.diced.serverstats.common.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerStatsWebServerConfig {
    int port = 8000;
    String address = "0.0.0.0";
    String route = "/metrics";
}
