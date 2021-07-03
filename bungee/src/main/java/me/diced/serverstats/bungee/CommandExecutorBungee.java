package me.diced.serverstats.bungee;

import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutorBungee extends Command implements TabExecutor {
    private ServerStatsBungee platform;

    public CommandExecutorBungee(ServerStatsBungee platform) {
        super("stats", null);

        this.platform = platform;
        this.platform.getProxy().getPluginManager().registerCommand(this.platform, this);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> res = new ArrayList<>();

        res.add("get");
        res.add("push");

        return res;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> arguments = new QuotedStringTokenizer(String.join(" ", args)).tokenize(true);

        Context ctx = new Context(sender);

        if (arguments.size() == 0) {
            this.helpCommand(ctx);
        } else {
            if (arguments.get(0).equalsIgnoreCase("get")) {
                this.getCommand(ctx);
            } else if (arguments.get(0).equalsIgnoreCase("push")) {
                this.pushCommand(ctx);
            } else {
                this.helpCommand(ctx);
            }
        }
    }

    private void helpCommand(Context ctx) {
        PluginDescription meta = this.platform.getDescription();
        String author = meta.getAuthor();
        String version = meta.getVersion();

        List<String> msgs = new ArrayList<>();
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD + "ServerStats Bukkit by " + author);
        msgs.add("" + ChatColor.GRAY + "ServerStats Version: " + version);
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD + "Commands: ");
        msgs.add("/stats get - View current stats");
        msgs.add("/stats push - Update stats to exporter");

        ctx.sendMessage(msgs);
    }

    private void getCommand(Context ctx) {
        Stats stats = this.platform.getStats();
        List<String> msgs = new ArrayList<>();
        ChatColor color = Context.heatmapColor(stats.tps, 50.0);

        msgs.add(goldTextBold("Stats:"));
        msgs.add(goldText("TPS: " + String.format("%s %.1f", color, stats.tps)));
        msgs.add(goldText("MSPT: " + String.format("%s %.1f ms", color, stats.mspt)));
        msgs.add(goldText("Players: " + String.format("%s %d ", ChatColor.WHITE, stats.playerCount)));
        msgs.add(goldText("Free Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.freeMemory / 1024 / 1024)));
        msgs.add(goldText("Total Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.totalMemory / 1024 / 1024)));
        msgs.add(goldText("Used Memory: " + String.format("%s %d MB", ChatColor.WHITE, (stats.totalMemory - stats.freeMemory) / 1024 / 1024)));
        msgs.add(goldText("Loaded Chunks: " + String.format("%s %d chunks", ChatColor.WHITE, stats.loadedChunks.intValue())));
        msgs.add(goldText("Entities: " + String.format("%s %d entities", ChatColor.WHITE, stats.entityCount.intValue())));

        ctx.sendMessage(msgs);
    }

    private void pushCommand(Context ctx) {
        List<String> msgs = new ArrayList<>();

        msgs.add("" + ChatColor.AQUA + "Pushed Stats");

        ctx.sendMessage(msgs);
    }

    private static String goldText(String text) {
        return "" + ChatColor.GOLD + text;
    }

    private static String goldTextBold(String text) {
        return "" + ChatColor.GOLD + ChatColor.BOLD + text;
    }
}
