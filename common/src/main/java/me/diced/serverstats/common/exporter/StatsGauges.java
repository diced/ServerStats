package me.diced.serverstats.common.exporter;

import io.prometheus.client.Gauge;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.config.ServerStatsPushableConfig;

public class StatsGauges {
    private ServerStats serverStats;

    private final Gauge playerCountGauge = Gauge.build()
            .name("player_count")
            .help("player count")
            .register();
    private final Gauge freeMemoryGauge = Gauge.build()
            .name("free_memory")
            .help("free memory jvm")
            .register();
    private final Gauge maxMemoryGauge = Gauge.build()
            .name("max_memory")
            .help("max memory jvm")
            .register();
    private final Gauge totalMemoryGauge = Gauge.build()
            .name("total_memory")
            .help("total memory jvm")
            .register();
    private final Gauge loadedChunksGauge = Gauge.build()
            .name("loaded_chunks")
            .help("loaded chunks")
            .register();
    private final Gauge entityCountGauge = Gauge.build()
            .name("entity_count")
            .help("entity count")
            .register();
    private final Gauge tpsGauge = Gauge.build()
            .name("tps")
            .help("ticks per second")
            .register();
    private final Gauge msptGauge = Gauge.build()
            .name("mspt")
            .help("ms per tick")
            .register();

    public StatsGauges(ServerStats serverStats) {
        this.serverStats = serverStats;
    }

    public void setValues(ServerStatsPushableConfig conf) {
        Stats stats = this.serverStats.platform.getStats();

        if (conf.playerCount) this.playerCountGauge.set(stats.playerCount);
        if (conf.freeMemory) this.freeMemoryGauge.set(stats.freeMemory);
        if (conf.maxMemory) this.maxMemoryGauge.set(stats.maxMemory);
        if (conf.totalMemory) this.totalMemoryGauge.set(stats.totalMemory);
        if (conf.loadedChunks) this.loadedChunksGauge.set(stats.loadedChunks.intValue());
        if (conf.entityCount) this.entityCountGauge.set(stats.entityCount.intValue());
        if (conf.tps) this.tpsGauge.set(stats.tps);
        if (conf.mspt) this.msptGauge.set(stats.mspt);
    }
}
