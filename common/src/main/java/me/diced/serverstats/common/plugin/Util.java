package me.diced.serverstats.common.plugin;

import com.sun.management.OperatingSystemMXBean;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Util {
    public static String[] units = new String[] {"B", "kB", "MB", "GB", "TB", "PB" };

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

    public static long getDirectorySize(Path path) throws IOException {
        DirectoryReader reader = new DirectoryReader();
        Files.walkFileTree(path, reader);

        return reader.size;
    }

    public static String formatBytes(long bytes) {
        int num = 0;

        while (bytes > 1024) {
            bytes /= 1024;
            ++num;
        }

        String d = new BigDecimal(bytes).setScale(0, RoundingMode.HALF_UP).toString();

        return d + " " + units[num];
    }

    private static class DirectoryReader extends SimpleFileVisitor<Path> {
        public long size = 0;

        @Override
        public FileVisitResult visitFile(Path _0, BasicFileAttributes attr) {
            size += attr.size();

            return CONTINUE;
        }
    }
}
