package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.plugin.ServerStatsMetadata;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import static net.kyori.adventure.text.format.Style.style;

public class HelpCommand extends Command {
    public HelpCommand(ServerStats serverStats) {
        super(serverStats, "help", "See info about the plugin & see all commands", false);
    }

    @Override
    public void execute(Context ctx) {
        ServerStatsMetadata meta = this.serverStats.platform.getMetadata();

        List<Component> components = new ArrayList<>();

        for (Map.Entry<String, Command> cmd : this.serverStats.commands.entrySet()) {
           components.add(text("/stats " + cmd.getValue().name + " - " + cmd.getValue().desc));
        }


        ctx.sendMessage(
                join(
                        newline(),
                        text("ServerStats " + meta.getType().toString() + " by " + meta.getAuthor(), style(WHITE, BOLD)),
                        text("ServerStats Version: " + meta.getVersion(), GRAY),
                        join(
                                newline(),
                                components
                        )
                )
        );
    }
}
