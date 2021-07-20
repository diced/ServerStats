package me.diced.serverstats.common.prometheus;

import me.diced.serverstats.common.config.ServerStatsConfig;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.prometheus.metrics.jvm.*;
import me.diced.serverstats.common.prometheus.metrics.server.*;
import me.diced.serverstats.common.prometheus.metrics.world.DiskSpacePerWorld;
import me.diced.serverstats.common.prometheus.metrics.world.EntityCountPerWorld;
import me.diced.serverstats.common.prometheus.metrics.world.LoadedChunksPerWorld;

import java.nio.file.Path;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public abstract class MetricsManager {

    public static List<Metric<?>> registeredMetrics = new ArrayList<>();

    private final Map<String, Metric<?>> metrics = new HashMap<>();
    public final ServerStatsConfig config;

    public MetricsManager(ServerStats serverStats) {
        this.config = serverStats.config;

        this.metrics.put("player_count", new PlayerCount("player_count", this));
        this.metrics.put("cpu", new CPU("cpu", this));
        this.metrics.put("uptime", new Uptime("uptime", this));
        this.metrics.put("threads", new Threads("threads", this));
        this.metrics.put("gc", new GC("gc", this));

        if (!serverStats.platform.getMetadata().getType().isProxy()) { // only add game server stuff if not a proxy server
            this.metrics.put("tps", new TPS("tps", this));
            this.metrics.put("mspt", new MSPT("mspt", this));
            this.metrics.put("loaded_chunks", new LoadedChunks("loaded_chunks", this));
            this.metrics.put("entity_count", new EntityCount("entity_count", this));

            this.metrics.put("packets_rx", new PacketRX("packets_rx", this));
            this.metrics.put("packets_tx", new PacketTX("packets_tx", this));

            this.metrics.put("disk_space_world", new DiskSpacePerWorld("disk_space_world", this));
            this.metrics.put("loaded_chunks_world", new LoadedChunksPerWorld("loaded_chunks_world", this));
            this.metrics.put("entity_count_world", new EntityCountPerWorld("entity_count_world", this));
        }


        this.metrics.put("free_memory", new FreeMemory("free_memory", this));
        this.metrics.put("total_memory", new TotalMemory("total_memory", this));
        this.metrics.put("max_memory", new MaxMemory("max_memory", this));
    }

    public void push() {
        this.metrics.values().forEach(m -> {
            if (m.enabled()) m.run();
        });
    }

    public int getPlayerCount() {
        return 0;
    }

    public double getMspt() {
        return 0;
    }

    public double getTps(){
        return 0;
    }

    public int getLoadedChunks() {
        return 0;
    }

    public int getEntityCount() {
        return 0;
    }

    public long getPacketRx() { return 0; }

    public long getPacketTx() { return 0; }

    public TreeMap<String, Integer> getLoadedChunksPerWorld() {
        return new TreeMap<>();
    }

    public TreeMap<String, Integer> getEntityCountPerWorld() {
        return new TreeMap<>();
    }

    public TreeMap<String, Path> getWorldPaths() {
        return new TreeMap<>();
    }
}
