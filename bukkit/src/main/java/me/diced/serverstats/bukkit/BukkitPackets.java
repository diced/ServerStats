package me.diced.serverstats.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;

public class BukkitPackets {
    private final BukkitServerStats platform;

    public static long packetsRx = 0;
    public static long packetsTx = 0;

    public BukkitPackets(BukkitServerStats platform) {
        this.platform = platform;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.platform, ListenerPriority.MONITOR, PacketType.values()) {
            @Override
            public void onPacketSending(PacketEvent event) {
                packetsTx++;
            }

            @Override
            public void onPacketReceiving(PacketEvent event) {
                packetsRx++;
            }
        });
    }
}
