package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import static net.kyori.adventure.text.format.Style.style;

public class ToggleCommand extends Command {
    public ToggleCommand(ServerStats serverStats) {
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
            boolean toggled = this.serverStats.toggleInterval();

            if (toggled) {
                ctx.sendMessage(
                        join(
                                newline(),
                                text("Interval is now running", GREEN)
                        )
                );
            } else {
                ctx.sendMessage(
                        join(
                                newline(),
                                text("Interval is no longer running", RED)
                        )
                );
            }
        }
    }
}
