package me.diced.serverstats.common.command;

public abstract class CompletionsManager<S> {
    public final S suggestor;
    private final Context ctx;

    public CompletionsManager(S s, Context ctx) {
        this.suggestor = s;
        this.ctx = ctx;
    }

    public void register() {
        this.suggest("get");

        if (this.ctx.isOp()) {
            this.suggest("push");
            this.suggest("toggle");
        }
    }

    public abstract void suggest(String cmd);
}
