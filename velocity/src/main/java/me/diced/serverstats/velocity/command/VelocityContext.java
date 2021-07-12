package me.diced.serverstats.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.text.Component;

public class VelocityContext implements Context {
    private final CommandSource sender;

    public VelocityContext(CommandSource sender) {
        this.sender = sender;
    }

    public void sendMessage(Component message) {
        this.sender.sendMessage(message);
    }

    public boolean isOp() {
        return this.sender instanceof ConsoleCommandSource;
    }
}
