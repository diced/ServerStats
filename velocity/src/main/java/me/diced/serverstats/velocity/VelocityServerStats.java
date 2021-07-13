package me.diced.serverstats.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.plugin.Util;
import me.diced.serverstats.common.scheduler.Scheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Subscribe
    public void onLogin(ServerConnectedEvent event) {
        this.serverStats.gauges.incPlayer(event.getPlayer().getUsername());
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
    public Stats getStats() {
        Runtime runtime = Runtime.getRuntime();

        int playerCount = this.server.getPlayerCount();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();

        // Proxy server does not have a tick system so we set it as 0
        double mspt = 0;
        double tps = 0;
        double cpu = Util.cpuPercent();

        AtomicInteger loadedChunks = new AtomicInteger(0);
        AtomicInteger entityCount = new AtomicInteger(0);

        return new Stats(playerCount, freeMemory, maxMemory, totalMemory, tps, mspt, cpu, loadedChunks, entityCount);
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
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public void start() {
        this.serverStats.tasks.register();
    }
}
