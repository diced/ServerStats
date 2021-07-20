package me.diced.serverstats.bukkit;

import me.diced.serverstats.bukkit.command.BukkitCommandExecutor;
import me.diced.serverstats.common.plugin.LogWrapper;
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
    private MetricsManager metricsManager;
    private final Logger logger = LoggerFactory.getLogger("ServerStats");

    @Override
    public final void onEnable() {
        try {
            LogWrapper logger = new LogWrapper() {
                private final Logger logger = LoggerFactory.getLogger("ServerStats");

                @Override
                public void info(String msg) {
                    this.logger.info(msg);
                }

                @Override
                public void error(String msg) {
                    this.logger.error(msg);
                }
            };

            this.scheduler = new BukkitScheduler(this);
            this.serverStats = new ServerStats(this, logger);
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
        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            this.serverStats.logger.info("Enabling Packet Metrics since ProtocolLib exists");
            new BukkitPackets(this);
            this.metricsManager = new BukkitMetricsManagerProtocolLib(this);
        } else {
            this.serverStats.logger.info("Disabling Packet Metrics since ProtocolLib doesn't exist");
            this.metricsManager = new BukkitMetricsManager(this);
        }
        this.serverStats.tasks.register();
    }
}
