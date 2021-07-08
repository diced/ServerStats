package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import static net.kyori.adventure.text.format.Style.style;

public class HelpCommand extends Command {
    public HelpCommand(ServerStats serverStats) {
        super(serverStats);
    }

    @Override
    public void execute(Context ctx) {
        ctx.sendMessage(
                join(
                        newline(),
                        text("ServerStats " + this.serverStats.platform.getType().toString() + " by " + this.serverStats.platform.getAuthor(), style(WHITE, BOLD)),
                        text("ServerStats Version: " + this.serverStats.platform.getVersion(), GRAY),
                        text("/stats get - View current stats"),
                        text("/stats push - Push current stats"),
                        text("/stats toggle - Toggle stats interval")
                )
        );
    }
}
