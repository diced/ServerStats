package me.diced.serverstats.common.prometheus;

import net.kyori.adventure.text.Component;

public abstract class Metric<T> {
    public String name;
    public String type;
    public final MetricsManager manager;
    public T collector;

    public Metric(String name, String type, MetricsManager manager, T collector) {
        this.name = name;
        this.type = type;
        this.manager = manager;
        this.collector = collector;

        MetricsManager.registeredMetrics.add(this);
    }

    public abstract void run();

    public String formatPrometheus() {
        return "";
    }
    public Component formatComponent() {
        return Component.text(""); // default for exemplar stuff, but overwritten for non exemplar
    }
    public boolean enabled() {
        return true;
    }
    public boolean isExemplar() {
        return false;
    }
}
