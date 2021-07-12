package me.diced.serverstats.fabric;

import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.plugin.ServerStatsType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricMetadata implements ServerStatsMetadata {
    private final ModMetadata meta = FabricLoader.getInstance().getModContainer("serverstats").get().getMetadata();

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
}
