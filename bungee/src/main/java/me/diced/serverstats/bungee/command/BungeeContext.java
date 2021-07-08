package me.diced.serverstats.bungee.command;

import me.diced.serverstats.bungee.BungeeServerStats;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.CommandSender;

public class BungeeContext implements Context {
    private final CommandSender sender;
    private final BungeeAudiences audiences;

    public BungeeContext(CommandSender sender, BungeeServerStats plugin) {
        this.sender = sender;
        this.audiences = BungeeAudiences.create(plugin);
    }

    public void sendMessage(Component message) {
        this.audiences.sender(this.sender).sendMessage(message);
    }

    public boolean isOp() {
        return true;
    }
}
