package me.diced.serverstats.bungee;

import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginLogger;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class ServerStatsBungee extends Plugin implements ServerStatsPlatform {
    private ServerStats serverStats;
    private Logger logger = PluginLogger.getLogger("ServerStats");
    private CommandExecutorBungee commandExecutor;

    @Override
    public void onEnable() {
        try {
            this.serverStats = new ServerStats(this);
            this.commandExecutor = new CommandExecutorBungee(this);
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
        return this.getDataFolder().toPath().resolve("serverstats.yml");
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
    public void infoLog(String msg) {
        this.logger.info(msg);
    }

    @Override
    public void start() {
        TaskScheduler scheduler = this.getProxy().getScheduler();

        scheduler.runAsync(this, () -> {
            this.serverStats.webServer.start();

            String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
            this.infoLog("Started Prometheus Exporter on " + address);
        });

        this.serverStats.pushStats();
        scheduler.schedule(this, () -> this.serverStats.pushStats(), this.serverStats.config.interval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {

    }
}