package me.diced.serverstats.fabric;

import carpet.helpers.TickSpeed;
import carpet.utils.Messenger;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.diced.serverstats.common.exporter.Stats;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandExecutorFabric implements Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {
    public ServerStatsFabric platform;

    public CommandExecutorFabric(ServerStatsFabric platform) {
        this.platform = platform;

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            LiteralCommandNode<ServerCommandSource> cmd = literal("stats")
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
    public int run(CommandContext<ServerCommandSource> ctx) {
        int start = ctx.getRange().getStart();
        List<String> arguments = new QuotedStringTokenizer(ctx.getInput().substring(start)).tokenize(true);

        String label = arguments.remove(0);

        if (arguments.size() == 0) {
            this.helpCommand(ctx);
        } else {
            if (arguments.get(0).equalsIgnoreCase("get")) {
                this.getCommand(ctx);
            } else if (arguments.get(0).equalsIgnoreCase("push")) {
                this.pushCommand(ctx);
            } else {
                this.helpCommand(ctx);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        builder.suggest("push");
        builder.suggest("get");
        return builder.buildFuture();
    }

    private void helpCommand(CommandContext<ServerCommandSource> ctx) {
        ModMetadata meta = FabricLoader.getInstance().getModContainer("serverstats").get().getMetadata();
        String author = meta.getAuthors().stream().findFirst().get().getName();
        String version = meta.getVersion().getFriendlyString();

        List<BaseText> msgs = new ArrayList<>();
        msgs.add(Messenger.c("wb ServerStats Fabric by " + author));
        msgs.add(Messenger.c("g ServerStats Version: " + version));
        msgs.add(Messenger.c("wb Commands: "));
        msgs.add(Messenger.s("/stats get - View current stats"));
        msgs.add(Messenger.s("/stats push - Update stats to exporter"));

        Messenger.send(ctx.getSource(), msgs);
    }

    private void getCommand(CommandContext<ServerCommandSource> ctx) {
        Stats stats = this.platform.getStats();
        List<BaseText> msgs = new ArrayList<>();
        String color = Messenger.heatmap_color(stats.tps, TickSpeed.mspt);

        msgs.add(Messenger.c("db Stats:"));
        msgs.add(Messenger.c("d TPS: ", String.format("%s %.1f", color, stats.tps)));
        msgs.add(Messenger.c("d MSPT: ", String.format("%s %.1f ms", color, stats.mspt)));
        msgs.add(Messenger.c("d Players: ", String.format("%s %d ", "wi", stats.playerCount)));
        msgs.add(Messenger.c("d Free Memory: ", String.format("%s %d MB", "wi", stats.freeMemory / 1024 / 1024)));
        msgs.add(Messenger.c("d Total Memory: ", String.format("%s %d MB", "wi", stats.totalMemory / 1024 / 1024)));
        msgs.add(Messenger.c("d Used Memory: ", String.format("%s %d MB", "wi", (stats.totalMemory - stats.freeMemory) / 1024 / 1024)));
        msgs.add(Messenger.c("d Loaded Chunks: ", String.format("%s %d chunks", "wi", stats.loadedChunks.intValue())));
        msgs.add(Messenger.c("d Entities: ", String.format("%s %d entities", "wi", stats.entityCount.intValue())));

        Messenger.send(ctx.getSource(), msgs);
    }

    private void pushCommand(CommandContext<ServerCommandSource> ctx) {
        boolean isOp = ctx.getSource().hasPermissionLevel(4);
        if (!isOp) {
            ctx.getSource().sendError(new LiteralText("Not an operator..."));
        } else {
            List<BaseText> msgs = new ArrayList<>();

            msgs.add(Messenger.c("l Pushed Stats"));

            Messenger.send(ctx.getSource(), msgs);
        }
    }

}
