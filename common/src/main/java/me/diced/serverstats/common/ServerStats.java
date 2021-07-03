package me.diced.serverstats.common;

import me.diced.serverstats.common.config.ConfigLoader;
import me.diced.serverstats.common.config.ServerStatsConfig;
import me.diced.serverstats.common.exporter.StatsGauges;
import me.diced.serverstats.common.exporter.StatsWebServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerStats {
    public StatsWebServer webServer;
    public ServerStatsPlatform platform;
    public ServerStatsConfig config;
    private long next;

    public final StatsGauges gauges = new StatsGauges(this);

    public ServerStats(ServerStatsPlatform platform) throws IOException {
        this.platform = platform;
        this.webServer = new StatsWebServer(this, new InetSocketAddress("0.0.0.0", 8000), "/metrics");

        ConfigLoader<ServerStatsConfig> configLoader = new ConfigLoader<>(ServerStatsConfig.class, this.platform.getConfigPath());
        this.config = configLoader.load();
    }

    public void startInterval() {
        while(true) {
            if (System.currentTimeMillis() > this.next) {
                this.pushStats();
            }
        }
    }

    public void pushStats() {
        this.gauges.setValues(this.config.pushable);
        this.next = System.currentTimeMillis() + this.config.interval;

        if (this.config.logs.writeLogs) {
            this.platform.infoLog(this.config.logs.writeLog);
        }
    }
}
