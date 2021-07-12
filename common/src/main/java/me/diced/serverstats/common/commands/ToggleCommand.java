package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.Style.style;

public class ToggleCommand extends Command {
    public ToggleCommand(ServerStats serverStats) {
        super(serverStats, "toggle", "Toggle the interval from running", true);
    }

    @Override
    public void execute(Context ctx) {
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
