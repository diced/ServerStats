package me.diced.serverstats.common.command;

import me.diced.serverstats.common.ServerStats;

public abstract class Command {
    public ServerStats serverStats;
    public Command(ServerStats serverStats) {
        this.serverStats = serverStats;
    }

    public abstract void execute(Context sender);
}
