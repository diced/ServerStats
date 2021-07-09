package me.diced.serverstats.fabric;

import carpet.helpers.TickSpeed;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.ServerStatsPlatform;
import me.diced.serverstats.common.ServerStatsType;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.ThreadScheduler;
import me.diced.serverstats.fabric.command.FabricCommandExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class FabricServerStats implements ModInitializer, ServerStatsPlatform {
    private MinecraftServer server;
    private ServerStats serverStats;
    private ThreadScheduler scheduler;
    private ModMetadata meta = FabricLoader.getInstance().getModContainer("serverstats").get().getMetadata();
    private final Logger logger = LoggerFactory.getLogger("ServerStats");

    @Override
    public void onInitialize() {
        try {
            this.scheduler = new ThreadScheduler();
            this.serverStats = new ServerStats(this);
            new FabricCommandExecutor(this);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("serverstats.conf");
    }

    @Override
    public ServerStatsType getType() {
        return ServerStatsType.FABRIC;
    }

    @Override
    public String getVersion() {
        return this.meta.getVersion().getFriendlyString();
    }

    @Override
    public String getAuthor() {
        return this.meta.getAuthors().stream().findFirst().get().getName();
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
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public void start() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;

            this.serverStats.tasks.register();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(s -> this.serverStats.stop());
    }
}
