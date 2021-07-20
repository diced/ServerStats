package me.diced.serverstats.common.prometheus.metrics.jvm;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Threads extends Metric<AtomicInteger> {

    public Threads(String name, MetricsManager manager) {
        super(name, "gauge", manager, new AtomicInteger());
    }

    @Override
    public void run() {
        this.collector.set(ManagementFactory.getThreadMXBean().getThreadCount());
    }

    @Override
    public String formatPrometheus() {
        return String.format("%s %f", this.name, this.collector.doubleValue());
    }

    @Override
    public Component formatComponent() {
        return text().append(text("Theads: ", GOLD)).append(text(this.collector.intValue(), WHITE)).asComponent();
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.threads;
    }
}
