package me.diced.serverstats.fabric.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import me.diced.serverstats.common.command.Context;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.util.List;

public class FabricContext implements Context<BaseText> {
    private CommandContext<ServerCommandSource> sender;

    public FabricContext(CommandContext<ServerCommandSource> sender) {
        this.sender = sender;
    }


    public void sendMessage(List<BaseText> messages) {
        Messenger.send(this.sender.getSource(), messages);
    }

    public boolean isOp() {
        return this.sender.getSource().hasPermissionLevel(4);
    }
}
