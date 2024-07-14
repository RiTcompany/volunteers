package org.example.utils;

import java.util.HashMap;
import java.util.Map;

public class TextUtil {
    private static final Map<String, String> MARKDOWN_SPEC_SYMBOL_MAP = new HashMap<>() {{
        put("\\", "\\\\");
        put("`", "\\`");
        put("*", "\\*");
        put("_", "\\_");
        put("{", "\\{");
        put("}", "\\}");
        put("[", "\\[");
        put("]", "\\]");
        put("<", "\\<");
        put(">", "\\>");
        put("(", "\\(");
        put(")", "\\)");
        put("#", "\\#");
        put("+", "\\+");
        put("-", "\\-");
        put(".", "\\.");
        put("!", "\\!");
        put("|", "\\|");
    }};

    public static String adaptMarkdownText(String input) {
        for (String symbol : MARKDOWN_SPEC_SYMBOL_MAP.keySet()) {
            input = input.replace(symbol, MARKDOWN_SPEC_SYMBOL_MAP.get(symbol));
        }

        return input;
    }
}
