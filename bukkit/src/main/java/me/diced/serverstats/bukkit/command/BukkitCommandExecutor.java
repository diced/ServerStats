package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.bukkit.BukkitServerStats;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.diced.serverstats.common.plugin.Util.tokenize;

public class BukkitCommandExecutor implements CommandExecutor, TabExecutor, Listener {
    private final BukkitServerStats platform;

    public BukkitCommandExecutor(BukkitServerStats platform) {
        this.platform = platform;
        PluginCommand command = platform.getCommand("stats");

        command.setExecutor(this);
        command.setTabCompleter(this);

        this.platform.getServer().getPluginManager().registerEvents(this, this.platform);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arguments = tokenize(args);

        BukkitContext ctx = new BukkitContext(sender, this.platform);

        this.executeCommand(arguments, ctx);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> res = new ArrayList<>();
        BukkitContext ctx = new BukkitContext(sender, this.platform);
        BukkitCompletionsManager completions = new BukkitCompletionsManager(res, ctx);

        completions.register();

        return res;
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }
}
