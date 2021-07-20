package me.diced.serverstats.common.prometheus.metrics.jvm;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;

import java.io.StringWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.TreeMap;

public class GC extends Metric<TreeMap<String, Long>> {

    public GC(String name, MetricsManager manager) {
        super(name, "summary", manager, new TreeMap<>());
    }

    @Override
    public void run() {
        for (final GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            this.collector.put(gc.getName(), gc.getCollectionTime());
        }
    }

    @Override
    public String formatPrometheus() {
        StringWriter writer = new StringWriter();

        var last = this.collector.lastEntry();

        for (Map.Entry<String, Long> entry : this.collector.entrySet()) {
            writer.write(this.name + "{name=\"" + entry.getKey() + "\"} " + entry.getValue() + (last.getKey().equals(entry.getKey()) ? "" : '\n'));
        }

        return writer.toString();
    }

    @Override
    public boolean isExemplar() {
        return true;
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.gc;
    }
}
