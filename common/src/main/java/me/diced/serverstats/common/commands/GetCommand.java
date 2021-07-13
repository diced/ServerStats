package me.diced.serverstats.common.commands;

import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.Command;
import me.diced.serverstats.common.command.Context;
import me.diced.serverstats.common.exporter.Stats;
import net.kyori.adventure.text.format.NamedTextColor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static me.diced.serverstats.common.plugin.Util.heatmapColor;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import static net.kyori.adventure.text.format.Style.style;

public class GetCommand extends Command {
    public GetCommand(ServerStats serverStats) {
        super(serverStats, "get", "View current stats", false);
    }

    @Override
    public void execute(Context ctx) {
        Stats stats = this.serverStats.platform.getStats();
        NamedTextColor color = heatmapColor(stats.tps, 50.0f);

        ctx.sendMessage(
                join(
                        newline(),
                        text().append(text("Stats: ", style(GOLD, BOLD))),
                        text().append(text("TPS: ", GOLD)).append(text(String.format("%.1f", stats.tps), color)),
                        text().append(text("MSPT: ", GOLD)).append(text(String.format("%.1f ms", stats.mspt), color)),
                        text().append(text("CPU: ", GOLD)).append(text(new BigDecimal(stats.cpu).setScale(2, RoundingMode.HALF_UP) + "%", heatmapColor(stats.cpu, 100.0f))),
                        text().append(text("Players: ", GOLD)).append(text(String.format("%d", stats.playerCount), WHITE)),
                        text().append(text("Free Memory: ", GOLD)).append(text(String.format("%d MB", stats.freeMemory / 1024 / 1024), WHITE)),
                        text().append(text("Total Memory: ", GOLD)).append(text(String.format("%d MB", stats.totalMemory / 1024 / 1024), WHITE)),
                        text().append(text("Used Memory: ", GOLD)).append(text(String.format("%d MB", (stats.totalMemory - stats.freeMemory) / 1024 / 1024), WHITE)),
                        text().append(text("Loaded Chunks: ", GOLD)).append(text(String.format("%d", stats.loadedChunks.intValue()), WHITE)),
                        text().append(text("Entities: ", GOLD)).append(text(String.format("%d", stats.entityCount.intValue()), WHITE))
                )
        );
    }
}
