package me.diced.serverstats.bungee;

import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsType;
import net.md_5.bungee.api.plugin.PluginDescription;

public class BungeeMetadata implements ServerStatsMetadata {
    private final PluginDescription meta;

    public BungeeMetadata(PluginDescription meta) {
        this.meta = meta;
    }

    @Override
    public ServerStatsType getType() {
        return ServerStatsType.BUNGEE;
    }

    @Override
    public String getVersion() {
        return this.meta.getVersion();
    }

    @Override
    public String getAuthor() {
        return this.meta.getAuthor();
    }
}
