package me.diced.serverstats.common.command;

import java.util.List;

public interface CommandExecutor<C> {
    void helpCommand(C ctx);
    void pushCommand(C ctx);
    void getCommand(C ctx);
    void toggleCommand(C ctx);

    default void executeCommand(List<String> args, C ctx) {
        if (args.size() == 0) {
            this.helpCommand(ctx);
        } else {
            String arg = args.get(0).toLowerCase();
            Command cmd = Command.fromString(arg);

            switch (cmd) {
                case PUSH -> this.pushCommand(ctx);
                case GET -> this.getCommand(ctx);
                case TOGGLE -> this.toggleCommand(ctx);
                default -> this.helpCommand(ctx);
            }
        }
    }
}
