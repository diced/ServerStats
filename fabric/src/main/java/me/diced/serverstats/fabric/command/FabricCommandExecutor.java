package me.diced.serverstats.fabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.diced.serverstats.common.plugin.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.fabric.FabricServerStats;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static me.diced.serverstats.common.plugin.Util.tokenize;
import static net.minecraft.server.command.CommandManager.*;

public class FabricCommandExecutor implements CommandExecutor, Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {
    private String CMD_NAME = "stats";
    public FabricServerStats platform;

    public FabricCommandExecutor(FabricServerStats platform) {
        this.platform = platform;

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            LiteralCommandNode<ServerCommandSource> cmd = literal(CMD_NAME)
                    .executes(this)
                    .build();

            ArgumentCommandNode<ServerCommandSource, String> args = argument("args", greedyString())
                    .suggests(this)
                    .executes(this)
                    .build();

            cmd.addChild(args);
            dispatcher.getRoot().addChild(cmd);
        }));
    }


    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        String input = context.getInput();
        int start = context.getRange().getStart();
        int removing = Math.min(start + CMD_NAME.length() + 1, input.length());

        List<String> arguments = tokenize(input.substring(removing));

        FabricContext ctx = new FabricContext(context);
        this.executeCommand(arguments, ctx);
        return Command.SINGLE_SUCCESS;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        FabricContext ctx = new FabricContext(context);
        FabricCompletionsManager completions = new FabricCompletionsManager(builder, ctx);

        completions.register();

        return builder.buildFuture();
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }
}
