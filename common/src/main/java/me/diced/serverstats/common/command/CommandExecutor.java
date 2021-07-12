package me.diced.serverstats.common.command;

import me.diced.serverstats.common.plugin.ServerStats;

import java.util.List;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface CommandExecutor{
    ServerStats getPlatform();

    default void executeCommand(List<String> args, Context ctx) {
        ServerStats serverStats = this.getPlatform();

        if (args.size() == 0) {
            serverStats.getCommand("help").execute(ctx);
        } else {
            String cmd = args.get(0).toLowerCase();
            Command command = serverStats.getCommand(cmd);

            if (command.op && !ctx.isOp()) {
                ctx.sendMessage(
                        join(
                                newline(),
                                text("Not an operator...", RED)
                        )
                );
            }

            command.execute(ctx);
        }
    }
}
