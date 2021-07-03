package me.diced.serverstats.common.command;

import java.util.List;

public interface CommandExecutor<C> {
    void helpCommand(C ctx);
    void pushCommand(C ctx);
    void getCommand(C ctx);
    default void executeCommand(List<String> args, C ctx) {
        if (args.size() == 0) {
            this.helpCommand(ctx);
        } else {
            if (args.get(0).equalsIgnoreCase("get")) {
                this.getCommand(ctx);
            } else if (args.get(0).equalsIgnoreCase("push")) {
                this.pushCommand(ctx);
            } else {
                this.helpCommand(ctx);
            }
        }
    }
}
