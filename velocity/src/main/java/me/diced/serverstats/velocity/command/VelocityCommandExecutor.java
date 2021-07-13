package me.diced.serverstats.velocity.command;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.velocity.VelocityServerStats;

import java.util.ArrayList;
import java.util.List;

import static me.diced.serverstats.common.plugin.Util.tokenize;

public class VelocityCommandExecutor implements CommandExecutor, RawCommand {
    private final VelocityServerStats platform;

    public VelocityCommandExecutor(VelocityServerStats platform) {
        this.platform = platform;
        this.platform.server.getCommandManager().register("stats", this);
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }

    @Override
    public void execute(Invocation invocation) {
        List<String> arguments = tokenize(invocation.arguments());

        VelocityContext ctx = new VelocityContext(invocation.source());

        this.executeCommand(arguments, ctx);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> res = new ArrayList<>();
        VelocityContext ctx = new VelocityContext(invocation.source());
        VelocityCompletionsManager completions = new VelocityCompletionsManager(res, ctx);

        completions.registerProxy();

        return res;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source() instanceof ConsoleCommandSource;
    }
}
