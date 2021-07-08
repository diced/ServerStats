package me.diced.serverstats.common.command;

import me.diced.serverstats.common.ServerStats;

import java.util.List;

public interface CommandExecutor{
    ServerStats getPlatform();

    default void executeCommand(List<String> args, Context ctx) {
        ServerStats serverStats = this.getPlatform();

        if (args.size() == 0) {
            serverStats.getCommand("help").execute(ctx);
        } else {
            String cmd = args.get(0).toLowerCase();

            switch (cmd) {
                case "push" -> serverStats.getCommand("push").execute(ctx);
                case "get" -> serverStats.getCommand("get").execute(ctx);
                case "toggle" -> serverStats.getCommand("toggle").execute(ctx);
                default -> serverStats.getCommand("help").execute(ctx);
            }
        }
    }
}
