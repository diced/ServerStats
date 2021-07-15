package me.diced.serverstats.common.prometheus.metrics;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class LoadedChunks extends Metric<AtomicInteger> {

    public LoadedChunks(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicInteger());
    }

    @Override
    public void run() {
        this.collector.set(this.manager.getLoadedChunks());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %d", this.name, this.collector.intValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("Loaded Chunks: ", GOLD)).append(text(String.format("%d", this.collector.intValue()), WHITE)).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.loadedChunks;
    }
}
