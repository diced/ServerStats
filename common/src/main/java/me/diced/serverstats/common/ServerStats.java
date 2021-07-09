package me.diced.serverstats.common;

import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.commands.GetCommand;
import me.diced.serverstats.common.commands.HelpCommand;
import me.diced.serverstats.common.commands.ToggleCommand;
import me.diced.serverstats.common.commands.PushCommand;
import me.diced.serverstats.common.config.ConfigLoader;
import me.diced.serverstats.common.config.ServerStatsConfig;
import me.diced.serverstats.common.exporter.StatsGauges;
import me.diced.serverstats.common.exporter.StatsWebServer;
import me.diced.serverstats.common.scheduler.Scheduler;
import me.diced.serverstats.common.scheduler.Task;
import me.diced.serverstats.common.scheduler.ThreadScheduler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServerStats {
    public StatsWebServer webServer;
    public ServerStatsConfig config;
    public ServerStatsTasks tasks;
    public ServerStatsPlatform platform;

    private HashMap<String, Command> commands = new HashMap<>();
    private boolean intervalRunning = true;

    public final StatsGauges gauges = new StatsGauges(this);

    public ServerStats(ServerStatsPlatform platform) throws IOException {
        this.platform = platform;

        ConfigLoader<ServerStatsConfig> configLoader = new ConfigLoader<>(ServerStatsConfig.class, this.platform.getConfigPath());
        this.config = configLoader.load();

        this.webServer = new StatsWebServer(new InetSocketAddress(this.config.webServer.hostname, this.config.webServer.port), this.config.webServer.route);

        this.tasks = new ServerStatsTasks(this, platform.getScheduler());

        this.commands.put("help", new HelpCommand(this));
        this.commands.put("get", new GetCommand(this));
        this.commands.put("push", new PushCommand(this));
        this.commands.put("toggle", new ToggleCommand(this));
    }

    public void pushStats() {
        if (this.intervalRunning) {
            this.gauges.setValues(this.config.pushable);

            if (this.config.logs.writeLogs) {
                this.platform.infoLog(this.config.logs.writeLog);
            }
        }
    }

    public void stop() {
        this.platform.infoLog("Stopping ServerStats-Worker");
        this.tasks.stop();
    }

    public boolean toggleInterval() {
        this.intervalRunning = !this.intervalRunning;
        return this.intervalRunning;
    }

    public Command getCommand(String name) {
        return this.commands.get(name);
    }

    public static class ServerStatsTasks {
        private Task webTask;
        private Task statsTask;
        private final ServerStats serverStats;
        private final Scheduler scheduler;

        public ServerStatsTasks(ServerStats serverStats, Scheduler scheduler) {
            this.serverStats = serverStats;
            this.scheduler = scheduler;
        }

        public void register() {
            this.webTask = this.scheduler.schedule(() -> {
                this.serverStats.webServer.start();

                String address = this.serverStats.webServer.addr.getHostName() + ":" + this.serverStats.webServer.addr.getPort();
                this.serverStats.platform.infoLog("Started Prometheus Exporter on " + address);
            });

            this.serverStats.pushStats();
            this.statsTask = this.scheduler.scheduleRepeatingTask(() -> this.serverStats.pushStats(), this.serverStats.config.interval, TimeUnit.MILLISECONDS);
        }

        public void stop() {
            this.webTask.cancel();
            this.statsTask.cancel();
            this.scheduler.stop();
        }
    }
}
