package com.luis.diaz.authentication.shared.utils;

public class ArgumentUtil {

    private ArgumentUtil(){}

    /**
     * If the string is null or empty, return true, otherwise return false.
     *
     * @param s The string to check
     * @return The method isNullOrEmpty returns a boolean value.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * If the argument is false, throw an IllegalArgumentException with the given
     * message.
     *
     * @param arg The boolean expression to evaluate.
     * @param message The message to be displayed if the argument is false.
     */
    public static void checkArgument(boolean arg, String message) {
        if (!arg) {
            throw new IllegalArgumentException(String.valueOf(message));
        }
    }
}
