package me.diced.serverstats.common.command;

import net.kyori.adventure.text.Component;

public interface Context {
    void sendMessage(Component messages);
    boolean isOp();
}
