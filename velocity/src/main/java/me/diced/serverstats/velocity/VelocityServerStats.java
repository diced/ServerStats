package me.diced.serverstats.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.scheduler.Scheduler;

import java.io.IOException;
import java.nio.file.Path;

import me.diced.serverstats.velocity.command.VelocityCommandExecutor;
import org.slf4j.Logger;

@Plugin(id = "serverstats",
        name = "ServerStats",
        version = "${version}",
        url = "https://serverstats.diced.me",
        description = "Visualize your Minecraft server statistics in realtime",
        authors = {"dicedtomato"}
)
public class VelocityServerStats implements ServerStatsPlatform {
    private ServerStats serverStats;
    private VelocityScheduler scheduler;
    private VelocityMetadata meta;
    private VelocityMetricsManager metricsManager;
    private final Logger logger;
    private final Path dataDirectory;
    public final ProxyServer server;

    @Inject
    public VelocityServerStats(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.meta = new VelocityMetadata(server.getPluginManager().getPlugin("serverstats").get().getDescription());
        this.metricsManager = new VelocityMetricsManager(this);

        try {
            this.scheduler = new VelocityScheduler(this);
            this.serverStats = new ServerStats(this);
            new VelocityCommandExecutor(this);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.serverStats.stop();
    }

    @Override
    public Path getConfigPath() {
        return this.dataDirectory.resolve("serverstats.conf");
    }

    @Override
    public ServerStatsMetadata getMetadata() {
        return this.meta;
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
    public void infoLog(String msg) {
        this.logger.info(msg);
    }

    @Override
    public ServerStats getServerStats() {
        return this.serverStats;
    }

    @Override
    public void start() {
        this.serverStats.tasks.register();
    }
}
