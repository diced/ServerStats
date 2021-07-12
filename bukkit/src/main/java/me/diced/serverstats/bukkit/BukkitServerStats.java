package me.diced.serverstats.bukkit;

import me.diced.serverstats.bukkit.command.BukkitCommandExecutor;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class BukkitServerStats extends JavaPlugin implements ServerStatsPlatform {
    private ServerStats serverStats;
    private BukkitScheduler scheduler;
    private final Logger logger = LoggerFactory.getLogger("ServerStats");
    private final BukkitMetadata meta = new BukkitMetadata(this.getDescription());

    @Override
    public final void onEnable() {
        try {
            this.scheduler = new BukkitScheduler(this);
            this.serverStats = new ServerStats(this);
            new BukkitCommandExecutor(this);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onDisable() {
        this.serverStats.stop();
    }

    @Override
    public Path getConfigPath() {
        return getDataFolder().toPath().resolve("serverstats.conf");
    }

    @Override
    public void infoLog(String msg) {
        this.logger.info(msg);
    }

    @Override
    public ServerStatsMetadata getMetadata() {
        return this.meta;
    }

    @Override
    public ServerStats getServerStats() {
        return this.serverStats;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public Stats getStats() {
        Runtime runtime = Runtime.getRuntime();

        int playerCount = Bukkit.getOnlinePlayers().size();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();

        double mspt = Bukkit.getAverageTickTime();
        double tps = Bukkit.getTPS()[0];

        AtomicInteger loadedChunks = new AtomicInteger();
        AtomicInteger entityCount = new AtomicInteger();

        Bukkit.getWorlds().forEach(sw -> {
            loadedChunks.addAndGet(sw.getLoadedChunks().length);
            sw.getEntities().forEach(entity -> entityCount.getAndIncrement());
        });

        return new Stats(playerCount, freeMemory, maxMemory, totalMemory, tps, mspt, loadedChunks, entityCount);
    }

    @Override
    public void start() {
        this.serverStats.tasks.register();
    }
}
