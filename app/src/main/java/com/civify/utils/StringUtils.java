package com.civify.utils;

public abstract class StringUtils {

    public static String capitalize(final String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
