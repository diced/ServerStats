package me.diced.serverstats.bungee;

import me.diced.serverstats.bungee.command.BungeeCommandExecutor;
import me.diced.serverstats.common.plugin.LogWrapper;
import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.scheduler.Scheduler;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginLogger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class BungeeServerStats extends Plugin implements ServerStatsPlatform, Listener {
    private ServerStats serverStats;
    private BungeeScheduler scheduler;
    private final BungeeMetadata meta = new BungeeMetadata(this.getDescription());
    private BungeeMetricsManager metricsManager;

    @Override
    public void onEnable() {
        try {
            LogWrapper logger = new LogWrapper() {
                private final Logger logger = PluginLogger.getLogger("ServerStats");

                @Override
                public void info(String msg) {
                    this.logger.info(msg);
                }

                @Override
                public void error(String msg) {
                    this.logger.severe(msg);
                }
            };

            this.scheduler = new BungeeScheduler(this);
            this.serverStats = new ServerStats(this, logger);
            this.metricsManager = new BungeeMetricsManager(this);

            new BungeeCommandExecutor(this);

            this.getProxy().getPluginManager().registerListener(this, this);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.serverStats.stop();
    }

    @Override
    public Path getConfigPath() {
        return this.getDataFolder().toPath().resolve("serverstats.conf");
    }

    @Override
    public ServerStatsMetadata getMetadata() {
        return this.meta;
    }

    @Override
    public MetricsManager getMetricsManager() {
        return this.metricsManager;
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
    public void start() {
        this.serverStats.tasks.register();
    }
}