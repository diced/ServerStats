package me.diced.serverstats.bungee;

import me.diced.serverstats.bungee.command.BungeeCommandExecutor;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.scheduler.Scheduler;
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
    private BungeeScheduler scheduler;
    private Logger logger = PluginLogger.getLogger("ServerStats");
    private PluginDescription meta = this.getDescription();

    @Override
    public void onEnable() {
        try {
            this.scheduler = new BungeeScheduler(this);
            this.serverStats = new ServerStats(this);
            new BungeeCommandExecutor(this);

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
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public void start() {
        this.serverStats.tasks.register();
    }
}