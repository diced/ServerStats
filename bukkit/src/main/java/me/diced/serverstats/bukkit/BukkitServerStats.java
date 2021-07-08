package me.diced.serverstats.bukkit;

import me.diced.serverstats.bukkit.command.BukkitCommandExecutor;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class BukkitServerStats extends JavaPlugin implements ServerStatsPlatform {
    private ServerStats serverStats;
    private Logger logger = LoggerFactory.getLogger("ServerStats");
    private PluginDescriptionFile meta = this.getDescription();
    private BukkitTask webTask;
    private BukkitTask statsTask;

    @Override
    public final void onEnable() {
        try {
            this.serverStats = new ServerStats(this);
            new BukkitCommandExecutor(this);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void onDisable() {
        this.stop();
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
    public ServerStatsType getType() {
        return ServerStatsType.BUKKIT;
    }

    @Override
    public String getVersion() {
        return this.meta.getVersion();
    }

    @Override
    public String getAuthor() {
        return this.meta.getAuthors().get(0);
    }

    @Override
    public ServerStats getServerStats() {
        return this.serverStats;
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
        this.webTask = Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            this.serverStats.webServer.start();

            String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
            this.infoLog("Started Prometheus Exporter on " + address);
        });

        long time = (this.serverStats.config.interval / 1000) * 20;

        this.statsTask = Bukkit.getScheduler().runTaskTimer(this, () -> this.serverStats.pushStats(), 0L, time);
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
