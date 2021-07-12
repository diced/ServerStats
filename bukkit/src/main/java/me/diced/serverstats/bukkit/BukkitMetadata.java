package me.diced.serverstats.bukkit;

import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsType;
import org.bukkit.plugin.PluginDescriptionFile;

public class BukkitMetadata implements ServerStatsMetadata {
    private final PluginDescriptionFile meta;

    public BukkitMetadata(PluginDescriptionFile meta) {
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
        return this.meta.getAuthors().get(0);
    }
}
