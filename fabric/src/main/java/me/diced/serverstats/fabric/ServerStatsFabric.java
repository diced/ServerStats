package me.diced.serverstats.fabric;

import carpet.helpers.TickSpeed;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStatsFabric implements ModInitializer, ServerStatsPlatform {
    private MinecraftServer server;
    private ServerStats serverStats;
    private Thread statsThread;
    private Thread webThread;
    private final Logger logger = LoggerFactory.getLogger("ServerStats");
    private long next;

    @Override
    public void onInitialize() {
        try {
            this.serverStats = new ServerStats(this);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("serverstats.yml");
    }

    @Override
    public ServerStatsType getType() {
        return ServerStatsType.FABRIC;
    }

    @Override
    public void infoLog(String msg) {
        this.logger.info(msg);
    }

    @Override
    public Stats getStats() {
        Runtime runtime = Runtime.getRuntime();

        int playerCount = this.server.getCurrentPlayerCount();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();

        double mspt = MathHelper.average(this.server.lastTickLengths) * 1.0E-6D;
        double tps = 1000.0D / Math.max((TickSpeed.time_warp_start_time != 0) ? 0.0 : TickSpeed.mspt, mspt);

        AtomicInteger loadedChunks = new AtomicInteger();
        AtomicInteger entityCount = new AtomicInteger();

        this.server.getWorlds().forEach(sw -> {
            loadedChunks.addAndGet(sw.getChunkManager().getLoadedChunkCount());
            sw.iterateEntities().forEach(entity -> entityCount.getAndIncrement());
        });

        return new Stats(playerCount, freeMemory, maxMemory, totalMemory, tps, mspt, loadedChunks, entityCount);
    }

    @Override
    public void start() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;

            this.webThread = new Thread(() -> {
                this.serverStats.webServer.start();

                String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
                this.infoLog("Started Prometheus Exporter on " + address);
            });
            this.webThread.setName("ServerStats Exporter");
            this.webThread.start();

            this.statsThread = new Thread(() -> this.serverStats.startInterval());
            this.statsThread.setName("ServerStats");
            this.statsThread.start();
        });
    }

    @Override
    public void stop() {

    }
}
