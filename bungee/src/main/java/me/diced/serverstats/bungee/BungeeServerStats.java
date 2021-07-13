package me.diced.serverstats.bungee;

import me.diced.serverstats.bungee.command.BungeeCommandExecutor;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.plugin.Util;
import me.diced.serverstats.common.scheduler.Scheduler;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginLogger;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class BungeeServerStats extends Plugin implements ServerStatsPlatform, Listener {
    private ServerStats serverStats;
    private BungeeScheduler scheduler;
    private final Logger logger = PluginLogger.getLogger("ServerStats");
    private final BungeeMetadata meta = new BungeeMetadata(this.getDescription());

    @Override
    public void onEnable() {
        try {
            this.scheduler = new BungeeScheduler(this);
            this.serverStats = new ServerStats(this);
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
    public Stats getStats() {
        Runtime runtime = Runtime.getRuntime();

        int playerCount = this.getProxy().getOnlineCount();
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

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        this.serverStats.gauges.incPlayer(event.getPlayer().getDisplayName());
    }
}