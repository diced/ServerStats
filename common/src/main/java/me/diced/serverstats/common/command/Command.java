package me.diced.serverstats.common.command;

public enum Command {
    HELP,
    PUSH,
    GET,
    TOGGLE;

    public static Command fromString(String cmd) {
        return switch (cmd) {
            case "push" -> PUSH;
            case "get" -> GET;
            case "toggle" -> TOGGLE;
            default -> HELP;
        };
    }
}
