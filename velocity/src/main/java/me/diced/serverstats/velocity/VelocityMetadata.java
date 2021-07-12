package me.diced.serverstats.velocity;

import com.velocitypowered.api.plugin.PluginDescription;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsType;

public class VelocityMetadata implements ServerStatsMetadata {
    private final PluginDescription meta;

    public VelocityMetadata(PluginDescription meta) {
        this.meta = meta;
    }

    @Override
    public ServerStatsType getType() {
        return ServerStatsType.VELOCITY;
    }

    @Override
    public String getVersion() {
        return this.meta.getVersion().get();
    }

    @Override
    public String getAuthor() {
        return this.meta.getAuthors().get(0);
    }
}
