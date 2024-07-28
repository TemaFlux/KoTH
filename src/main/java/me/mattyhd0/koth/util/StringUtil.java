package me.mattyhd0.koth.util;

public class StringUtil {
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }
}