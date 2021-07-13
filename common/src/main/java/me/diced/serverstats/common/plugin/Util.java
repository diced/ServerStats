package me.diced.serverstats.common.plugin;

import com.sun.management.OperatingSystemMXBean;
import net.kyori.adventure.text.format.NamedTextColor;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static List<String> tokenize(String str) {
        return Arrays.asList(str.split(" "));
    }
    public static List<String> tokenize(String[] strings) {
        return Arrays.asList(strings);
    }
    public static double cpuPercent() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad() * 100;
    }
    public static NamedTextColor heatmapColor(double actual, double reference) { // from carpet mod Messenger class
        NamedTextColor color = NamedTextColor.GRAY;

        if (actual >= 0.0D) color = NamedTextColor.DARK_GREEN;
        if (actual > 0.5D * reference) color = NamedTextColor.YELLOW;
        if (actual > 0.8D * reference) color = NamedTextColor.RED;
        if (actual > reference) color = NamedTextColor.LIGHT_PURPLE;

        return color;
    }
}
