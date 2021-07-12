package me.diced.serverstats.fabric.command;

import com.mojang.brigadier.context.CommandContext;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class FabricContext implements Context {
    private final CommandContext<ServerCommandSource> sender;

    public FabricContext(CommandContext<ServerCommandSource> sender) {
        this.sender = sender;
    }

    public void sendMessage(Component message) {
        Text txt = Text.Serializer.fromJson(GsonComponentSerializer.gson().serialize(message));

        this.sender.getSource().sendFeedback(txt, false);
    }

    public boolean isOp() {
        return this.sender.getSource().hasPermissionLevel(4);
    }
}
