package me.diced.serverstats.common.command;

import java.util.List;

public interface Context<C> {
    void sendMessage(List<C> messages);
}
