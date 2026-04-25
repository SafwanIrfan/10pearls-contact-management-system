package com._pearls.contactms.utils;

public class AuthHelper {

    public static boolean isEmail(String input) {
        return input.contains("@");
    }

    public static boolean isPhoneNo(String input) {
        return input.matches("\\d{10,15}");
    }
}
