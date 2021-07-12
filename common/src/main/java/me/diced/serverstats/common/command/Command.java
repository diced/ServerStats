package me.diced.serverstats.common.command;

import me.diced.serverstats.common.plugin.ServerStats;

public abstract class Command {
    public ServerStats serverStats;
    public String name;
    public String desc;
    public boolean op;

    public Command(ServerStats serverStats, String name, String desc, boolean op) {
        this.serverStats = serverStats;
        this.name = name;
        this.desc = desc;
        this.op = op;
    }

    public abstract void execute(Context sender);
}
