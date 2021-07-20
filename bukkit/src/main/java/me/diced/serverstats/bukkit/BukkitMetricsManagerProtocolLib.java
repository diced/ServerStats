package me.diced.serverstats.bukkit;

public class BukkitMetricsManagerProtocolLib extends BukkitMetricsManager {
    public BukkitMetricsManagerProtocolLib(BukkitServerStats platform) {
        super(platform);
    }

    @Override
    public long getPacketRx() {
        return BukkitPackets.packetsRx;
    }

    @Override
    public long getPacketTx() {
        return BukkitPackets.packetsTx;
    }
}
