package me.diced.serverstats.bungee.command;

import me.diced.serverstats.bungee.BungeeServerStats;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

import static me.diced.serverstats.common.plugin.Util.tokenize;

public class BungeeCommandExecutor extends Command implements CommandExecutor, TabExecutor {
    private final BungeeServerStats platform;

    public BungeeCommandExecutor(BungeeServerStats platform) {
        super("stats", null);

        this.platform = platform;
        this.platform.getProxy().getPluginManager().registerCommand(this.platform, this);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> res = new ArrayList<>();

        BungeeContext ctx = new BungeeContext(sender, this.platform);
        BungeeCompletionsManager completions = new BungeeCompletionsManager(res, ctx);

        completions.registerProxy();

        return res;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> arguments = tokenize(args);

        BungeeContext ctx = new BungeeContext(sender, this.platform);

        this.executeCommand(arguments, ctx);
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }
}
