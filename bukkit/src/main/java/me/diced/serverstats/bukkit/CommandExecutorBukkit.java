package me.diced.serverstats.bukkit;

import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CommandExecutorBukkit implements TabExecutor, Listener {
    private ServerStatsBukkit platform;
    private PluginCommand command;

    public CommandExecutorBukkit(ServerStatsBukkit platform) {
        this.platform = platform;
        this.command = platform.getCommand("stats");

        this.command.setExecutor(this);
        this.command.setTabCompleter(this);

        this.platform.getServer().getPluginManager().registerEvents(this, this.platform);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arguments = new QuotedStringTokenizer(String.join(" ", args)).tokenize(true);

        Context ctx = new Context(sender, command);

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

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> res = new ArrayList<>();

        res.add("get");

        // add push if op
        if (sender.isOp()) res.add("push");

        return res;
    }

    private void helpCommand(Context ctx) {
        PluginDescriptionFile meta = this.platform.getDescription();
        String author = meta.getAuthors().get(0);
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

        msgs.add(goldTextBold("Stats:"));
        msgs.add(goldText("Players: " + String.format("%s %d ", ChatColor.WHITE, stats.playerCount)));
        msgs.add(goldText("Free Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.freeMemory / 1024 / 1024)));
        msgs.add(goldText("Total Memory: " + String.format("%s %d MB", ChatColor.WHITE, stats.totalMemory / 1024 / 1024)));
        msgs.add(goldText("Used Memory: " + String.format("%s %d MB", ChatColor.WHITE, (stats.totalMemory - stats.freeMemory) / 1024 / 1024)));

        ctx.sendMessage(msgs);
    }

    private void pushCommand(Context ctx) {
        boolean isOp = ctx.isOp();
        List<String> msgs = new ArrayList<>();

        if (!isOp) {
            msgs.add("" + ChatColor.RED + "Not an operator...");
        } else {
            msgs.add("" + ChatColor.AQUA + "Pushed Stats");
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
