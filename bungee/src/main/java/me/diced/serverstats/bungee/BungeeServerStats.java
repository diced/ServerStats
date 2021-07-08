package me.diced.serverstats.bungee;

import me.diced.serverstats.bungee.command.BungeeCommandExecutor;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.PluginLogger;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class BungeeServerStats extends Plugin implements ServerStatsPlatform {
    private ServerStats serverStats;
    private Logger logger = PluginLogger.getLogger("ServerStats");
    private PluginDescription meta = this.getDescription();
    private ScheduledTask webTask;
    private ScheduledTask statsTask;

    @Override
    public void onEnable() {
        try {
            this.serverStats = new ServerStats(this);
            new BungeeCommandExecutor(this);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        this.stop();
    }

    @Override
    public Path getConfigPath() {
        return this.getDataFolder().toPath().resolve("serverstats.conf");
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
        AtomicInteger loadedChunks = new AtomicInteger(0);
        AtomicInteger entityCount = new AtomicInteger(0);

        return new Stats(playerCount, freeMemory, maxMemory, totalMemory, tps, mspt, loadedChunks, entityCount);
    }

    @Override
    public ServerStatsType getType() {
        return ServerStatsType.BUNGEE;
    }

    @Override
    public String getVersion() {
        return this.meta.getVersion();
    }

    @Override
    public String getAuthor() {
        return this.meta.getAuthor();
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
        TaskScheduler scheduler = this.getProxy().getScheduler();

        this.webTask = scheduler.runAsync(this, () -> {
            this.serverStats.webServer.start();

            String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
            this.infoLog("Started Prometheus Exporter on " + address);
        });

        this.serverStats.pushStats();
        this.statsTask = scheduler.schedule(this, () -> this.serverStats.pushStats(), this.serverStats.config.interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        this.webTask.cancel();
        this.statsTask.cancel();
    }

    public boolean toggleInterval() {
        return this.serverStats.toggleInterval();
    }
}