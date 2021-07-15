package me.diced.serverstats.velocity;

import me.diced.serverstats.common.prometheus.MetricsManager;

public class VelocityMetricsManager extends MetricsManager {
    private final VelocityServerStats platform;

    public VelocityMetricsManager(VelocityServerStats platform) {
        super(platform.getServerStats().config);
        this.platform = platform;
    }

    @Override
    public int getPlayerCount() {
        return this.platform.server.getPlayerCount();
    }

}
