package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.prometheus.Metric;
import me.diced.serverstats.common.prometheus.MetricsManager;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static me.diced.serverstats.common.plugin.Util.formatBytes;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class GetCommand extends Command {
    public GetCommand(ServerStats serverStats) {
        super(serverStats, "get", "View current stats", false);
    }

    @Override
    public void execute(Context ctx) {
        Runtime rt = Runtime.getRuntime();
        List<Component> components = new ArrayList<>();

        for (Metric<?> metric : MetricsManager.registeredMetrics) {
            if (!metric.isExemplar()) components.add(metric.formatComponent());
        }

        ctx.sendMessage(
                join(
                        newline(),
                        text().append(text("Stats: ", style(GOLD, BOLD))),
                        join(
                                newline(),
                                components
                        ),
                        text().append(text("Used Memory: ", GOLD)).append(text(formatBytes(rt.totalMemory() - rt.freeMemory()), WHITE))
                )
        );
    }
}
