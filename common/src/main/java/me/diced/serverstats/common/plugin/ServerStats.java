package me.diced.serverstats.common.plugin;

import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.commands.GetCommand;
import me.diced.serverstats.common.commands.HelpCommand;
import me.diced.serverstats.common.commands.ToggleCommand;
import me.diced.serverstats.common.commands.PushCommand;
import me.diced.serverstats.common.config.ConfigLoader;
import me.diced.serverstats.common.config.ServerStatsConfig;
import me.diced.serverstats.common.prometheus.MetricsHttpServer;
import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerStats {
    public MetricsHttpServer webServer;
    public ServerStatsConfig config;
    public ServerStatsTasks tasks;
    public ServerStatsPlatform platform;
    public LogWrapper logger;
    public Map<String, Command> commands = new HashMap<>();

    private boolean intervalRunning = true;

    public ServerStats(ServerStatsPlatform platform, LogWrapper logger) throws IOException {
        this.platform = platform;
        this.logger = logger;

        ConfigLoader<ServerStatsConfig> configLoader = new ConfigLoader<>(ServerStatsConfig.class, this.platform.getConfigPath());

        this.config = configLoader.load();

        this.webServer = new MetricsHttpServer(new InetSocketAddress(this.config.webServer.hostname, this.config.webServer.port), this.config.webServer.route);

        this.tasks = new ServerStatsTasks(this, platform.getScheduler());

        this.commands.put("help", new HelpCommand(this));
        this.commands.put("get", new GetCommand(this));
        this.commands.put("push", new PushCommand(this));
        this.commands.put("toggle", new ToggleCommand(this));
    }

    public void pushStats() {
        if (this.intervalRunning) {
            this.platform.getMetricsManager().push();

            if (this.config.logs.writeLogs) {
                this.logger.info(this.config.logs.writeLog);
            }
        }
    }

    public void stop() {
        this.logger.info("Stopping ServerStats-Worker");
        this.tasks.stop();
    }

    public boolean toggleInterval() {
        this.intervalRunning = !this.intervalRunning;
        return this.intervalRunning;
    }

    public Command getCommand(String name) {
        Command cmd = this.commands.get(name);
        if (cmd == null) cmd = this.commands.get("help");

        return cmd;
    }

    public static class ServerStatsTasks {
        private final List<Task> tasks = new ArrayList<>();
        private final ServerStats serverStats;
        private final Scheduler scheduler;

        public ServerStatsTasks(ServerStats serverStats, Scheduler scheduler) {
            this.serverStats = serverStats;
            this.scheduler = scheduler;
        }

        public void register() {
            this.serverStats.logger.info("Starting ServerStats-Worker tasks");

            var webTask = this.scheduler.schedule(() -> {
                this.serverStats.webServer.start();

                String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
                this.serverStats.logger.info("Started Prometheus Exporter on " + address);
            });

            this.serverStats.pushStats();
            var statsTask = this.scheduler.scheduleRepeatingTask(() -> this.serverStats.pushStats(), this.serverStats.config.interval, TimeUnit.MILLISECONDS);

            this.tasks.add(webTask);
            this.tasks.add(statsTask);
        }

        public void stop() {
            this.tasks.forEach(t -> t.cancel());
            this.scheduler.stop();
        }
    }
}
