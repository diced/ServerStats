package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.bukkit.BukkitServerStats;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandExecutor implements CommandExecutor, TabExecutor, Listener {
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

        BukkitContext ctx = new BukkitContext(sender, this.platform);

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
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }
}
