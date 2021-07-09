package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.common.command.CompletionsManager;
import me.diced.serverstats.common.command.Context;

import java.util.List;

public class BukkitCompletionsManager extends CompletionsManager<List<String>> {
    public BukkitCompletionsManager(List<String> strings, Context ctx) {
        super(strings, ctx);
    }

    @Override
    public void suggest(String cmd) {
        this.suggestor.add(cmd);
    }
}
