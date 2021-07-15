package me.diced.serverstats.bukkit;

import me.diced.serverstats.bukkit.command.BukkitCommandExecutor;
import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.scheduler.Scheduler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class BukkitServerStats extends JavaPlugin implements ServerStatsPlatform, Listener {
    private ServerStats serverStats;
    private BukkitScheduler scheduler;
    private final BukkitMetadata meta = new BukkitMetadata(this.getDescription());
    private BukkitMetricsManager metricsManager;
    private final Logger logger = LoggerFactory.getLogger("ServerStats");


    @Override
    public final void onEnable() {
        try {
            this.scheduler = new BukkitScheduler(this);
            this.serverStats = new ServerStats(this);
            this.metricsManager = new BukkitMetricsManager(this);
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
    public MetricsManager getMetricsManager() {
        return this.metricsManager;
    }

    @Override
    public void start() {
        this.serverStats.tasks.register();
    }
}
