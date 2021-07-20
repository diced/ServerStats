package me.diced.serverstats.common.prometheus.metrics.jvm;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.util.concurrent.atomic.AtomicLong;

import static me.diced.serverstats.common.plugin.Util.formatBytes;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class MaxMemory extends Metric<AtomicLong> {

    public MaxMemory(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicLong());
    }

    @Override
    public void run() {
        Runtime runtime = Runtime.getRuntime();

        this.collector.set(runtime.maxMemory());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %d", this.name, this.collector.longValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("Max Memory: ", GOLD)).append(text(formatBytes(this.collector.longValue()), WHITE)).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.maxMemory;
    }
}
