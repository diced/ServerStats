package me.diced.serverstats.fabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.diced.serverstats.common.ServerStats;
import me.diced.serverstats.common.command.CommandExecutor;
import me.diced.serverstats.common.util.QuotedStringTokenizer;
import me.diced.serverstats.fabric.FabricServerStats;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FabricCommandExecutor implements CommandExecutor, Command<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {
    public FabricServerStats platform;

    public FabricCommandExecutor(FabricServerStats platform) {
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
    public int run(CommandContext<ServerCommandSource> context) {
        int start = context.getRange().getStart();
        List<String> arguments = new QuotedStringTokenizer(context.getInput().substring(start)).tokenize(true);

        arguments.remove(0);

        FabricContext ctx = new FabricContext(context);
        this.executeCommand(arguments, ctx);

        return Command.SINGLE_SUCCESS;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        builder.suggest("get");

        if (context.getSource().hasPermissionLevel(4)) {
            builder.suggest("push");
            builder.suggest("toggle");
        }

        return builder.buildFuture();
    }

    @Override
    public ServerStats getPlatform() {
        return this.platform.getServerStats();
    }

//    @Override
//    public void helpCommand(FabricContext ctx) {
//        List<BaseText> msgs = new ArrayList<>();
//        msgs.add(Messenger.c("wb ServerStats " + this.platform.getType().toString() + " by " + this.platform.getAuthor()));
//        msgs.add(Messenger.c("g ServerStats Version: " + this.platform.getVersion()));
//        msgs.add(Messenger.c("wb Commands: "));
//        msgs.add(Messenger.s("/stats get - View current stats"));
//        msgs.add(Messenger.s("/stats push - Update stats to exporter"));
//        msgs.add(Messenger.s("/stats toggle - Toggle the interval"));
//
//
//        ctx.sendMessage(msgs);
//    }
//
//    @Override
//    public void getCommand(FabricContext ctx) {
//        Stats stats = this.platform.getStats();
//        List<BaseText> msgs = new ArrayList<>();
//        String color = Messenger.heatmap_color(stats.tps, TickSpeed.mspt);
//
//        msgs.add(Messenger.c("db Stats:"));
//        msgs.add(Messenger.c("d TPS: ", String.format("%s %.1f", color, stats.tps)));
//        msgs.add(Messenger.c("d MSPT: ", String.format("%s %.1f ms", color, stats.mspt)));
//        msgs.add(Messenger.c("d Players: ", String.format("%s %d ", "wi", stats.playerCount)));
//        msgs.add(Messenger.c("d Free Memory: ", String.format("%s %d MB", "wi", stats.freeMemory / 1024 / 1024)));
//        msgs.add(Messenger.c("d Total Memory: ", String.format("%s %d MB", "wi", stats.totalMemory / 1024 / 1024)));
//        msgs.add(Messenger.c("d Used Memory: ", String.format("%s %d MB", "wi", (stats.totalMemory - stats.freeMemory) / 1024 / 1024)));
//        msgs.add(Messenger.c("d Loaded Chunks: ", String.format("%s %d chunks", "wi", stats.loadedChunks.intValue())));
//        msgs.add(Messenger.c("d Entities: ", String.format("%s %d entities", "wi", stats.entityCount.intValue())));
//
//        ctx.sendMessage(msgs);
//    }
//
//    @Override
//    public void pushCommand(FabricContext ctx) {
//        List<BaseText> msgs = new ArrayList<>();
//
//        boolean isOp = ctx.isOp();
//        if (!isOp) {
//            msgs.add(Messenger.c("r Not an operator..."));
//        } else {
//            msgs.add(Messenger.c("l Pushed Stats"));
//        }
//
//        ctx.sendMessage(msgs);
//    }
//
//    @Override
//    public void toggleCommand(FabricContext ctx) {
//        List<BaseText> msgs = new ArrayList<>();
//
//        boolean isOp = ctx.isOp();
//        if (!isOp) {
//            msgs.add(Messenger.c("r Not an operator..."));
//        } else {
//            boolean toggled = this.platform.toggleInterval();
//            if (toggled) {
//                msgs.add(Messenger.c("l Interval is now running."));
//            } else {
//                msgs.add(Messenger.c("l Interval is no longer running."));
//            }
//        }
//
//        ctx.sendMessage(msgs);
//    }
}
