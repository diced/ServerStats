package me.diced.serverstats.bukkit;

import me.diced.serverstats.common.prometheus.MetricsManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BukkitMetricsManager extends MetricsManager {
    private final BukkitServerStats platform;

    public BukkitMetricsManager(BukkitServerStats platform) {
        super(platform.getServerStats());
        this.platform = platform;
    }

    @Override
    public int getPlayerCount() {
        return this.platform.getServer().getOnlinePlayers().size();
    }

    @Override
    public double getMspt() {
        return this.platform.getServer().getAverageTickTime();
    }

    @Override
    public double getTps() {
        return this.platform.getServer().getTPS()[0];
    }

    @Override
    public int getLoadedChunks() {
        AtomicInteger loadedChunks = new AtomicInteger();

        this.platform.getServer().getWorlds().forEach(w -> loadedChunks.addAndGet(w.getChunkCount()));

        return loadedChunks.intValue();
    }

    @Override
    public int getEntityCount() {
        AtomicInteger entityCount = new AtomicInteger();

        this.platform.getServer().getWorlds().forEach(w -> w.getEntityCount());

        return entityCount.intValue();
    }

    @Override
    public TreeMap<String, Integer> getLoadedChunksPerWorld() {
        TreeMap<String, Integer> worlds = new TreeMap<>();

        this.platform.getServer().getWorlds().forEach(w -> worlds.put(w.getName(), w.getLoadedChunks().length));
        return worlds;
    }

    @Override
    public TreeMap<String, Integer> getEntityCountPerWorld() {
        TreeMap<String, Integer> entities = new TreeMap<>();

        this.platform.getServer().getWorlds().forEach(w -> entities.put(w.getName(), w.getEntityCount()));

        return entities;
    }

    @Override
    public TreeMap<String, Path> getWorldPaths() {
        TreeMap<String, Path> worlds = new TreeMap<>();

        this.platform.getServer().getWorlds().forEach(w -> {
            String name = w.getName();

            worlds.put(name, Paths.get(".", name));
        });

        return worlds;
    }
}
