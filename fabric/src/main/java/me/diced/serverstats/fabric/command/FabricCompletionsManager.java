package me.diced.serverstats.fabric.command;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.diced.serverstats.common.command.CompletionsManager;
import me.diced.serverstats.common.command.Context;

public class FabricCompletionsManager extends CompletionsManager<SuggestionsBuilder> {
    public FabricCompletionsManager(SuggestionsBuilder suggestionsBuilder, Context ctx) {
        super(suggestionsBuilder, ctx);
    }

    @Override
    public void suggest(String cmd) {
        this.suggestor.suggest(cmd);
    }
}
