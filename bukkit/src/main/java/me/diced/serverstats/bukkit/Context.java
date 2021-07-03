package me.diced.serverstats.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Context {
    private CommandSender sender;
    private Command command;

    public Context(CommandSender sender, Command command) {
        this.sender = sender;
        this.command = command;
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
