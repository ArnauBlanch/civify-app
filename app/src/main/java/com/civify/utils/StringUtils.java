package com.civify.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public final class StringUtils {

    private StringUtils() { }

    public static String capitalize(final String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String toString(Map<?, ?> map) {
        StringBuilder builder = new StringBuilder();
        for (Entry<?, ?> e : map.entrySet()) {
            builder.append("K: ").append(e.getKey()).append(", " + "V: ").append(e.getValue())
                    .append('\n');
        }
        return builder.toString();
    }

    public static String toString(Collection<?> collection) {
        StringBuilder builder = new StringBuilder();
        for (Object e : collection) builder.append(e).append('\n');
        return builder.toString();
    }

}
