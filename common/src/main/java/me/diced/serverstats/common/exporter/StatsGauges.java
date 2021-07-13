package me.diced.serverstats.common.exporter;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.config.ServerStatsPushableConfig;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsGauges {
    private final ServerStats serverStats;

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
    private final Gauge cpuGauge = Gauge.build()
            .name("cpu")
            .help("server process cpu percentage")
            .register();

    private final Map<String, AtomicInteger> playerLogins = new HashMap<>();

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
        if (conf.cpu) this.cpuGauge.set(stats.cpu);
    }

    public String playerCounter() {
        StringWriter writer = new StringWriter();

        writer.write("# TYPE player_logins counter\n");
        writer.write("# HELP player_logins player logins\n");
        for (Map.Entry<String, AtomicInteger> entry : this.playerLogins.entrySet()) {
            writer.write(String.format("player_logins{player=\"%s\"} %d", entry.getKey(), entry.getValue().intValue()) + "\n");
        }

        return writer.toString();
    }

    public void incPlayer(String player) {
        if (!this.playerLogins.containsKey(player)) {
            this.playerLogins.put(player, new AtomicInteger(1));
        } else {
            AtomicInteger in = this.playerLogins.get(player);
            in.getAndIncrement();
        }
    }
}
