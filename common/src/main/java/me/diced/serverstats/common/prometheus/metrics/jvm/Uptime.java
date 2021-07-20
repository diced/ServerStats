package me.diced.serverstats.common.prometheus.metrics.jvm;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

import static me.diced.serverstats.common.plugin.Util.formatDuration;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Uptime extends Metric<AtomicLong> {

    public Uptime(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicLong());
    }

    @Override
    public void run() {
        this.collector.set(ManagementFactory.getRuntimeMXBean().getUptime());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %d", this.name, this.collector.longValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("Uptime: ", GOLD)).append(text(formatDuration(this.collector.longValue()), WHITE)).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.uptime;
    }
}
