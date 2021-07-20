package me.diced.serverstats.common.prometheus.metrics.jvm;

import com.google.common.util.concurrent.AtomicDouble;
import me.diced.serverstats.common.plugin.Util;
import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static me.diced.serverstats.common.plugin.Util.heatmapColor;

public class CPU extends Metric<AtomicDouble> {

    public CPU(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicDouble());
    }

    @Override
    public void run() {
        this.collector.set(Util.cpuPercent());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %f", this.name, this.collector.doubleValue());
    }

    @Override
    public Component formatComponent() {
        String d = BigDecimal.valueOf(Util.cpuPercent()).setScale(2, RoundingMode.HALF_UP).toString();
        return text().append(text("CPU: ", GOLD)).append(text(d, heatmapColor(this.collector.doubleValue(), 100.0f))).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.cpu;
    }
}
