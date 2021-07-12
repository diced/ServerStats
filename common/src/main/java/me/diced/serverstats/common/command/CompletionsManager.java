package me.diced.serverstats.common.command;

public abstract class CompletionsManager<S> {
    public final S suggester;
    private final Context ctx;

    public CompletionsManager(S s, Context ctx) {
        this.suggester = s;
        this.ctx = ctx;
    }

    public void register() {
        this.suggest("get");
        this.suggest("help");

        if (this.ctx.isOp()) {
            this.suggest("push");
            this.suggest("toggle");
        }
    }

    public void registerProxy() {
        this.suggest("get");
        this.suggest("push");
        this.suggest("toggle");
        this.suggest("help");
    }

    public abstract void suggest(String cmd);
}
