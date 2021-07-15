package me.diced.serverstats.fabric;

import carpet.helpers.TickSpeed;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.minecraft.util.math.MathHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FabricMetricsManager extends MetricsManager {
    private final FabricServerStats platform;

    public FabricMetricsManager(FabricServerStats platform) {
        super(platform.getServerStats().config);
        this.platform = platform;
    }

    @Override
    public int getPlayerCount() {
        return this.platform.server.getCurrentPlayerCount();
    }

    @Override
    public double getMspt() {
        return MathHelper.average(this.platform.server.lastTickLengths) * 1.0E-6D;
    }

    @Override
    public double getTps() {
        return 1000.0D / Math.max((TickSpeed.time_warp_start_time != 0) ? 0.0 : TickSpeed.mspt, this.getMspt());
    }

    @Override
    public int getLoadedChunks() {
        AtomicInteger loadedChunks = new AtomicInteger();

        this.platform.server.getWorlds().forEach(w -> loadedChunks.addAndGet(w.getChunkManager().getLoadedChunkCount()));

        return loadedChunks.intValue();
    }

    @Override
    public int getEntityCount() {
        AtomicInteger entityCount = new AtomicInteger();

        this.platform.server.getWorlds().forEach(w -> w.iterateEntities().forEach(e -> entityCount.incrementAndGet()));

        return entityCount.intValue();
    }

    @Override
    public TreeMap<String, Integer> getLoadedChunksPerWorld() {
        TreeMap<String, Integer> worlds = new TreeMap<>();

        this.platform.server.getWorlds().forEach(w -> worlds.put(this.platform.getLevelName(w), w.getChunkManager().getLoadedChunkCount()));

        return worlds;
    }

    @Override
    public TreeMap<String, Integer> getEntityCountPerWorld() {
        TreeMap<String, Integer> entities = new TreeMap<>();

        this.platform.server.getWorlds().forEach(w -> {
            AtomicInteger in = new AtomicInteger();
            w.iterateEntities().forEach(e -> in.getAndIncrement());

            entities.put(this.platform.getLevelName(w), in.intValue());
        });

        return entities;
    }

    @Override
    public TreeMap<String, Path> getWorldPaths() {
        TreeMap<String, Path> paths = new TreeMap<>();

        this.platform.server.getWorlds().forEach(w -> {
            String p = this.platform.getLevelName(w);
            paths.put(p, Paths.get(".", p));
        });

        return paths;
    }
}
