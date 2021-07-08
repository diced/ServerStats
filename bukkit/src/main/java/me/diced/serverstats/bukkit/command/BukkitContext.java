package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.bukkit.BukkitServerStats;
import me.diced.serverstats.common.command.Context;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class BukkitContext implements Context {
    private final CommandSender sender;
    private final BukkitAudiences audiences;

    public BukkitContext(CommandSender sender, BukkitServerStats plugin) {
        this.sender = sender;
        this.audiences = BukkitAudiences.create(plugin);
    }

    public void sendMessage(Component message) {
        this.audiences.sender(this.sender).sendMessage(message);
    }

    public boolean isOp() {
        return this.sender.isOp();
    }
}
