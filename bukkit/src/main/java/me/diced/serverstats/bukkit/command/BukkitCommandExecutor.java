package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.bukkit.BukkitServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandExecutor implements CommandExecutor<BukkitContext>, TabExecutor, Listener {
    private BukkitServerStats platform;
    private PluginCommand command;

    public BukkitCommandExecutor(BukkitServerStats platform) {
        this.platform = platform;
        this.command = platform.getCommand("stats");

        this.command.setExecutor(this);
        this.command.setTabCompleter(this);

        this.platform.getServer().getPluginManager().registerEvents(this, this.platform);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arguments = new QuotedStringTokenizer(String.join(" ", args)).tokenize(true);

        BukkitContext ctx = new BukkitContext(sender);

        this.executeCommand(arguments, ctx);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> res = new ArrayList<>();

        res.add("get");

        if (sender.isOp()) {
            res.add("push");
            res.add("toggle");
        }

        return res;
    }

    @Override
    public void helpCommand(BukkitContext ctx) {

        List<String> msgs = new ArrayList<>();
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD +  "ServerStats " + this.platform.getType().toString() + " by " + this.platform.getAuthor());
        msgs.add("" + ChatColor.GRAY + "ServerStats Version: " + this.platform.getVersion());
        msgs.add("" + ChatColor.WHITE + ChatColor.BOLD + "Commands: ");
        msgs.add("/stats get - View current stats");
        msgs.add("/stats push - Update stats to exporter");
        msgs.add("/stats toggle - Toggle the interval");

        ctx.sendMessage(msgs);
    }

    @Override
    public void getCommand(BukkitContext ctx) {
        Stats stats = this.platform.getStats();
        List<String> msgs = new ArrayList<>();

        msgs.add(goldTextBold("Stats:"));
        msgs.add(goldText("Players: " + String.format("%s %d ", ChatColor.WHITE, stats.playerCount)));
        msgs.add(goldText("Free Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.freeMemory / 1024 / 1024)));
        msgs.add(goldText("Total Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.totalMemory / 1024 / 1024)));
        msgs.add(goldText("Used Memory: " + String.format("%s %d MB", ChatColor.WHITE, (stats.totalMemory - stats.freeMemory) / 1024 / 1024)));

        ctx.sendMessage(msgs);
    }

    @Override
    public void pushCommand(BukkitContext ctx) {
        boolean isOp = ctx.isOp();
        List<String> msgs = new ArrayList<>();

        if (!isOp) {
            msgs.add("" + ChatColor.RED + "Not an operator...");
        } else {
            msgs.add("" + ChatColor.AQUA + "Pushed Stats");
        }

        ctx.sendMessage(msgs);
    }

    @Override
    public void toggleCommand(BukkitContext ctx) {
        List<String> msgs = new ArrayList<>();
        if (ctx.isOp()) {
            boolean toggled = this.platform.toggleInterval();
            if (toggled) {
                msgs.add("" + ChatColor.AQUA + "Interval is now running.");
            } else {
                msgs.add("" + ChatColor.AQUA + "Interval is no longer running.");
            }
        } else {
            msgs.add("" + ChatColor.RED + "Not an operator...");
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
