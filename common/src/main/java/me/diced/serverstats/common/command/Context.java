package me.diced.serverstats.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface Context {
    void sendMessage(Component messages);
    boolean isOp();
    default NamedTextColor heatmapColor(double actual, double reference)
    {
        NamedTextColor color = NamedTextColor.GRAY;

        if (actual >= 0.0D) color = NamedTextColor.DARK_GREEN;
        if (actual > 0.5D * reference) color = NamedTextColor.YELLOW;
        if (actual > 0.8D * reference) color = NamedTextColor.RED;
        if (actual > reference) color = NamedTextColor.LIGHT_PURPLE;
        return color;
    }
}
