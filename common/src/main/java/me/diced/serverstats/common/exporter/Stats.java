package me.diced.serverstats.common.exporter;

import java.util.concurrent.atomic.AtomicInteger;

public class Stats {
    public int playerCount;
    public long freeMemory;
    public long maxMemory;
    public long totalMemory;
    public double tps;
    public double mspt;
    public double cpu;

    public AtomicInteger loadedChunks;
    public AtomicInteger entityCount;

    public Stats(
            int playerCount,
            long freeMemory,
            long maxMemory,
            long totalMemory,
            double tps,
            double mspt,
            double cpu,
            AtomicInteger loadedChunks,
            AtomicInteger entityCount
    ) {
        this.playerCount = playerCount;
        this.freeMemory = freeMemory;
        this.maxMemory = maxMemory;
        this.totalMemory = totalMemory;
        this.tps = tps;
        this.mspt = mspt;
        this.cpu = cpu;
        this.loadedChunks = loadedChunks;
        this.entityCount = entityCount;
    }
}
