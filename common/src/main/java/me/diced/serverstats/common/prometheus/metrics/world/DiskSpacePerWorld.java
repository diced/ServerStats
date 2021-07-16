package me.diced.serverstats.common.prometheus.metrics.world;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import static me.diced.serverstats.common.plugin.Util.getDirectorySize;

public class DiskSpacePerWorld extends Metric<TreeMap<String, Long>> {

    public DiskSpacePerWorld(String name, MetricsManager manager) {
        super(name, "counter", manager, new TreeMap<>());
    }

    @Override
    public void run() {
        for (Map.Entry<String, Path> entry : this.manager.getWorldPaths().entrySet()) {
            try {
                this.collector.put(entry.getKey(), getDirectorySize(entry.getValue()));
            } catch (IOException e) {
                this.collector.put(entry.getKey(), 0L);
            }
        }
    }

    @Override
    public String formatPrometheus() {
        StringWriter writer = new StringWriter();

        var last = this.collector.lastEntry();

        for (Map.Entry<String, Long> entry : this.collector.entrySet()) {
            writer.write(this.name + "{world=\"" + entry.getKey() + "\"} " + entry.getValue() + (last.getKey().equals(entry.getKey()) ? "" : '\n'));
        }

        return writer.toString();
    }

    @Override
    public boolean isExemplar() {
        return true;
    }

    @Override
    public boolean enabled() {
        return this.manager.config.pushable.diskSpace;
    }
}
