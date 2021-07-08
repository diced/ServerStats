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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class ServerStats {
    public StatsWebServer webServer;
    public ServerStatsPlatform platform;
    public ServerStatsConfig config;

    private HashMap<String, Command> commands = new HashMap<>();
    private long next;
    private boolean intervalRunning = true;

    public final StatsGauges gauges = new StatsGauges(this);

    public ServerStats(ServerStatsPlatform platform) throws IOException {
        this.platform = platform;

        ConfigLoader<ServerStatsConfig> configLoader = new ConfigLoader<>(ServerStatsConfig.class, this.platform.getConfigPath());
        this.config = configLoader.load();

        this.webServer = new StatsWebServer(new InetSocketAddress(this.config.webServer.hostname, this.config.webServer.port), this.config.webServer.route);

        this.commands.put("help", new HelpCommand(this));
        this.commands.put("get", new GetCommand(this));
        this.commands.put("push", new PushCommand(this));
        this.commands.put("toggle", new ToggleCommand(this));
    }

    public void startInterval() {
        while(true) {
            if (System.currentTimeMillis() > this.next) {
                this.pushStats();
            }
        }
    }

    public void pushStats() {
        if (this.intervalRunning) {
            this.gauges.setValues(this.config.pushable);
            this.next = System.currentTimeMillis() + this.config.interval;

            if (this.config.logs.writeLogs) {
                this.platform.infoLog(this.config.logs.writeLog);
            }
        }
    }

    public boolean toggleInterval() {
        this.intervalRunning = !this.intervalRunning;
        return this.intervalRunning;
    }

    public Command getCommand(String name) {
        return this.commands.get(name);
    }
}
