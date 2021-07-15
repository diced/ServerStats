package me.diced.serverstats.common.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ServerStatsPushableConfig {
    public boolean playerCount = true;
    public boolean freeMemory = true;
    public boolean maxMemory = true;
    public boolean totalMemory = true;
    public boolean tps = true;
    public boolean mspt = true;
    public boolean cpu = true;
    public boolean loadedChunks = true;
    public boolean entityCount = true;
    public boolean diskSpace = true;
}
