package me.diced.serverstats.bungee.command;

import me.diced.serverstats.common.command.CompletionsManager;
import me.diced.serverstats.common.command.Context;

import java.util.List;

public class BungeeCompletionsManager extends CompletionsManager<List<String>> {
    public BungeeCompletionsManager(List<String> strings, Context ctx) {
        super(strings, ctx);
    }

    @Override
    public void suggest(String cmd) {
        this.suggester.add(cmd);
    }
}
