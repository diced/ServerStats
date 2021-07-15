package me.diced.serverstats.bungee;

import me.diced.serverstats.common.prometheus.MetricsManager;

public class BungeeMetricsManager extends MetricsManager {
    private final BungeeServerStats platform;

    public BungeeMetricsManager(BungeeServerStats platform) {
        super(platform.getServerStats().config);
        this.platform = platform;
    }

    @Override
    public int getPlayerCount() {
        return this.platform.getProxy().getOnlineCount();
    }
}
