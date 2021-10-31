package me.diced.serverstats.common.prometheus;

import java.io.StringWriter;

public class MetricsExporter {
    public static String export() {
        StringWriter writer = new StringWriter();

        for (Metric<?> metric : MetricsManager.registeredMetrics) {
            if (metric.enabled()) {
                writer.write("# HELP " + metric.name + '\n');
                writer.write("# TYPE " + metric.name + " " + metric.type + '\n');
                writer.write(metric.formatPrometheus() + '\n');
            }
        }

        return writer.toString();
    }
}
