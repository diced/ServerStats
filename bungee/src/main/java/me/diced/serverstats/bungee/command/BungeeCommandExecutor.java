package me.diced.serverstats.bungee.command;

import me.diced.serverstats.bungee.BungeeServerStats;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.exporter.Stats;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class BungeeCommandExecutor extends Command implements CommandExecutor, TabExecutor {
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

        BungeeContext ctx = new BungeeContext(sender, this.platform);

        this.executeCommand(arguments, ctx);
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }
}
