package me.diced.serverstats.bungee.command;

import me.diced.serverstats.bungee.BungeeServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class BungeeCommandExecutor extends Command implements CommandExecutor<BungeeContext>, TabExecutor {
    private BungeeServerStats platform;

    public BungeeCommandExecutor(BungeeServerStats platform) {
        super("stats", null);

        this.platform = platform;
        this.platform.getProxy().getPluginManager().registerCommand(this.platform, this);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> res = new ArrayList<>();

        res.add("get");
        res.add("push");
        res.add("toggle");

        return res;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> arguments = new QuotedStringTokenizer(String.join(" ", args)).tokenize(true);

        BungeeContext ctx = new BungeeContext(sender);

        this.executeCommand(arguments, ctx);
    }

    @Override
    public void helpCommand(BungeeContext ctx) {
        List<String> msgs = new ArrayList<>();
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD + "ServerStats " + this.platform.getType().toString() + " by " + this.platform.getAuthor());
        msgs.add("" + ChatColor.GRAY + "ServerStats Version: " + this.platform.getVersion());
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD + "Commands: ");
        msgs.add("/stats get - View current stats");
        msgs.add("/stats push - Update stats to exporter");
        msgs.add("/stats toggle - Toggle the interval");

        ctx.sendMessage(msgs);
    }

    @Override
    public void getCommand(BungeeContext ctx) {
        Stats stats = this.platform.getStats();
        List<String> msgs = new ArrayList<>();
        ChatColor color = BungeeContext.heatmapColor(stats.tps, 50.0);

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

    @Override
    public void pushCommand(BungeeContext ctx) {
        List<String> msgs = new ArrayList<>();

        msgs.add("" + ChatColor.AQUA + "Pushed Stats");

        ctx.sendMessage(msgs);
    }

    @Override
    public void toggleCommand(BungeeContext ctx) {
        List<String> msgs = new ArrayList<>();
        boolean toggled = this.platform.toggleInterval();
        if (toggled) {
            msgs.add("" + ChatColor.AQUA + "Interval is now running.");
        } else {
            msgs.add("" + ChatColor.AQUA + "Interval is no longer running.");
        }

        ctx.sendMessage(msgs);
    }

    private static String goldText(String text) {
        return "" + ChatColor.GOLD + text;
    }

    private static String goldTextBold(String text) {
        return "" + ChatColor.GOLD + ChatColor.BOLD + text;
    }
}
