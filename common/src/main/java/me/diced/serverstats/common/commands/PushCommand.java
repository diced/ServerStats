package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class PushCommand extends Command {
    public PushCommand(ServerStats serverStats) {
        super(serverStats);
    }

    @Override
    public void execute(Context ctx) {
        if (ctx.isOp()) {
            ctx.sendMessage(
                    join(
                            newline(),
                            text("Not an operator...", RED)
                    )
            );
        } else {
            ctx.sendMessage(
                    join(
                            newline(),
                            text("Pushed stats", BLUE)
                    )
            );
        }
    }
}
