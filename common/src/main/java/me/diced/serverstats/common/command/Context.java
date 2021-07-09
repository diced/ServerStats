package me.diced.serverstats.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface Context {
    void sendMessage(Component messages);
    boolean isOp();
}
