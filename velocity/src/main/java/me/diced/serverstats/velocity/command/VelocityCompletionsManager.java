package me.diced.serverstats.velocity.command;

import me.diced.serverstats.common.command.CompletionsManager;
import me.diced.serverstats.common.command.Context;

import java.util.List;

public class VelocityCompletionsManager extends CompletionsManager<List<String>> {
    public VelocityCompletionsManager(List<String> strings, Context ctx) {
        super(strings, ctx);
    }

    @Override
    public void suggest(String cmd) {
        this.suggester.add(cmd);
    }
}
