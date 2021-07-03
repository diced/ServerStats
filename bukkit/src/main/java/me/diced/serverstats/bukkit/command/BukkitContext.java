package me.diced.serverstats.bukkit.command;

import me.diced.serverstats.common.command.Context;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BukkitContext implements Context<String> {
    private CommandSender sender;

    public BukkitContext(CommandSender sender) {
        this.sender = sender;
    }

    public void sendMessage(List<String> messages) {
        this.sender.sendMessage(messages.toArray(new String[0]));
    }

    public boolean isOp() {
        return this.sender.isOp();
    }

    public static ChatColor heatmapColor(double actual, double reference)
    {
        ChatColor color = ChatColor.GRAY;
        if (actual >= 0.0D) color = ChatColor.DARK_GREEN;
        if (actual > 0.5D * reference) color = ChatColor.YELLOW;
        if (actual > 0.8D * reference) color = ChatColor.RED;
        if (actual > reference) color = ChatColor.LIGHT_PURPLE;
        return color;
    }
}
