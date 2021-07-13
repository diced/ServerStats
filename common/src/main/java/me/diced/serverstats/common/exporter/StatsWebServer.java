package me.diced.serverstats.common.exporter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;

public class StatsWebServer {
    private final HttpServer server;
    public final InetSocketAddress addr;
    private final StatsGauges gauges;

    public StatsWebServer(InetSocketAddress addr, String route, StatsGauges gauges) throws IOException {
        this.server = HttpServer.create(addr, 0);
        this.addr = addr;
        this.gauges = gauges;

        this.server.createContext(route, new StatsMetricsHandler());
        this.server.setExecutor(null);
    }

    public void start() {
        this.server.start();
    }

    private class StatsMetricsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringWriter writer = new StringWriter();

            writer.write(gauges.playerCounter()); // custom

            TextFormat.writeOpenMetrics100(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            String metrics = writer.toString();

            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, metrics.length());

            os.write(metrics.getBytes());
            os.flush();
            os.close();
        }
    }
}
