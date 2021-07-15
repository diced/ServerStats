package me.diced.serverstats.common.prometheus;

import java.io.StringWriter;

public class MetricsExporter {
    public static String export() {
        StringWriter writer = new StringWriter();

        Metric<?> last = MetricsManager.registeredMetrics.get(MetricsManager.registeredMetrics.size() - 1);

        for (Metric<?> metric : MetricsManager.registeredMetrics) {
            if (metric.enabled()) {
                writer.write("# HELP " + metric.name + '\n');
                writer.write("# TYPE " + metric.name + " " + metric.type + '\n');
                writer.write(metric.formatPrometheus() + (metric.name.equals(last.name) ? "" : '\n'));
            }
        }

        return writer.toString();
    }
}
