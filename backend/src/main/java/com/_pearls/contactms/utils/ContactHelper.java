package com._pearls.contactms.utils;

import java.util.regex.Pattern;

public class ContactHelper {

    private ContactHelper() {}

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private static final Pattern PHONE_PATTERN =
                Pattern.compile("^\\+?\\d{7,15}$");

    public static boolean isEmail(String input) {
            return EMAIL_PATTERN.matcher(input).matches();
        }

    public static boolean isPhoneNo(String input) {
            return PHONE_PATTERN.matcher(input).matches();
        }

    public static String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[\n\r\t]", "_");
    }
}