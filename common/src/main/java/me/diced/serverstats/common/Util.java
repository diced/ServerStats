package me.diced.serverstats.common;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<String> tokenize(String str) {
        return new QuotedStringTokenizer(str).tokenize(true);
    }
    public static List<String> tokenize(String[] str) {
        return new QuotedStringTokenizer(String.join(" ", str)).tokenize(true);
    }
    public static NamedTextColor heatmapColor(double actual, double reference) { // from carpet mod Messenger class
        NamedTextColor color = NamedTextColor.GRAY;

        if (actual >= 0.0D) color = NamedTextColor.DARK_GREEN;
        if (actual > 0.5D * reference) color = NamedTextColor.YELLOW;
        if (actual > 0.8D * reference) color = NamedTextColor.RED;
        if (actual > reference) color = NamedTextColor.LIGHT_PURPLE;

        return color;
    }

    // https://github.com/lucko/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/command/utils/QuotedStringTokenizer.java
    static private class QuotedStringTokenizer {
        private final String string;
        private int cursor;

        public QuotedStringTokenizer(String string) {
            this.string = string;
        }

        public List<String> tokenize(boolean omitEmptyStringAtEnd) {
            List<String> output = new ArrayList<>();
            while (hasNext()) {
                output.add(readString());
            }
            if (!omitEmptyStringAtEnd && this.cursor > 0 && isWhitespace(peek(-1))) {
                output.add("");
            }
            return output;
        }

        private static boolean isQuoteCharacter(char c) {
            // return c == '"' || c == '“' || c == '”';
            return c == '\u0022' || c == '\u201C' || c == '\u201D';
        }

        private static boolean isWhitespace(char c) {
            return c == ' ';
        }

        private String readString() {
            if (isQuoteCharacter(peek())) {
                return readQuotedString();
            } else {
                return readUnquotedString();
            }
        }

        private String readUnquotedString() {
            final int start = this.cursor;
            while (hasNext() && !isWhitespace(peek())) {
                skip();
            }
            final int end = this.cursor;

            if (hasNext()) {
                skip(); // skip whitespace
            }

            return this.string.substring(start, end);
        }

        private String readQuotedString() {
            skip(); // skip start quote

            final int start = this.cursor;
            while (hasNext() && !isQuoteCharacter(peek())) {
                skip();
            }
            final int end = this.cursor;

            if (hasNext()) {
                skip(); // skip end quote
            }
            if (hasNext() && isWhitespace(peek())) {
                skip(); // skip whitespace
            }

            return this.string.substring(start, end);
        }

        private boolean hasNext() {
            return this.cursor + 1 <= this.string.length();
        }

        private char peek() {
            return this.string.charAt(this.cursor);
        }

        private char peek(int offset) {
            return this.string.charAt(this.cursor + offset);
        }

        private void skip() {
            this.cursor++;
        }

    }
}
