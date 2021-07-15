package me.diced.serverstats.common.prometheus;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MetricsHttpServer {
    private final HttpServer server;
    public final InetSocketAddress addr;

    public MetricsHttpServer(InetSocketAddress addr, String route) throws IOException {
        this.server = HttpServer.create(addr, 0);
        this.addr = addr;

        this.server.createContext(route, new StatsMetricsHandler());
        this.server.setExecutor(null);
    }

    public void start() {
        this.server.start();
    }

    private static class StatsMetricsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String metrics = MetricsExporter.export();

            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, metrics.length());

            os.write(metrics.getBytes());
            os.flush();
            os.close();
        }
    }
}
