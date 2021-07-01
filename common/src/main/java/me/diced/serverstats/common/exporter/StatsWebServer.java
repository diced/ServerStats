package me.diced.serverstats.common.exporter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import me.diced.serverstats.common.ServerStats;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;

public class StatsWebServer {
    private final HttpServer server;
    private final ServerStats serverStats;
    public final InetSocketAddress addr;

    public StatsWebServer(ServerStats serverStats, InetSocketAddress addr, String route) throws IOException {
        this.server = HttpServer.create(addr, 0);
        this.addr = addr;

        this.server.createContext(route, new StatsMetricsHandler());
        this.server.setExecutor(null);

        this.serverStats = serverStats;
    }

    public void start() {
        this.server.start();
    }

    private static class StatsMetricsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringWriter writer = new StringWriter();

            TextFormat.writeFormat(TextFormat.CONTENT_TYPE_004, writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            String metrics = writer.toString();

            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, metrics.length());

            os.write(metrics.getBytes());
            os.flush();
            os.close();
        }
    }
}
