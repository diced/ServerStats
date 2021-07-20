package me.diced.serverstats.common.prometheus.metrics.server;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.util.concurrent.atomic.AtomicLong;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class PacketTX extends Metric<AtomicLong> {

    public PacketTX(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicLong());
    }

    @Override
    public void run() {
        this.collector.set(this.manager.getPacketTx());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %d", this.name, this.collector.longValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("Packets Sent: ", GOLD)).append(text(String.format("%d", this.collector.longValue()), WHITE)).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.packets;
    }
}
