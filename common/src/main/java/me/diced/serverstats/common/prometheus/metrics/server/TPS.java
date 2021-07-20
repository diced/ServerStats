package me.diced.serverstats.common.prometheus.metrics.server;

import com.google.common.util.concurrent.AtomicDouble;
import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import static me.diced.serverstats.common.plugin.Util.heatmapColor;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class TPS extends Metric<AtomicDouble> {

    public TPS(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicDouble());
    }

    @Override
    public void run() {
        this.collector.set(this.manager.getTps());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %f", this.name, this.collector.doubleValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("TPS: ", GOLD)).append(text(String.format("%.1f", this.collector.doubleValue()), heatmapColor(this.manager.getMspt(), 50))).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.tps;
    }
}
