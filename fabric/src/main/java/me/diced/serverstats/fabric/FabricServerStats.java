package me.diced.serverstats.fabric;

import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsPlatform;
import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.ThreadScheduler;
import me.diced.serverstats.fabric.command.FabricCommandExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class FabricServerStats implements ModInitializer, ServerStatsPlatform {
    public MinecraftServer server;
    private ServerStats serverStats;
    private ThreadScheduler scheduler;
    private final FabricMetadata meta = new FabricMetadata();
    private FabricMetricsManager metricsManager;
    private final Logger logger = LoggerFactory.getLogger("ServerStats");

    @Override
    public void onInitialize() {
        try {
            this.scheduler = new ThreadScheduler();
            this.serverStats = new ServerStats(this);
            this.metricsManager = new FabricMetricsManager(this);
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
    public void infoLog(String msg) {
        this.logger.info(msg);
    }


    @Override
    public void start() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;

            this.serverStats.tasks.register();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(s -> this.serverStats.stop());
    }

    public String getLevelName(ServerWorld world) {
        return switch (world.getRegistryKey().getValue().getPath()) {
            case "the_nether" -> "world_the_nether";
            case "the_end" -> "world_the_end";
            default -> "world";
        };
    }
}
