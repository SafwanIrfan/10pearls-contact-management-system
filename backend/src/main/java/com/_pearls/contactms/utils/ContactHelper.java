package com._pearls.contactms.utils;

public class ContactHelper {

    private ContactHelper() {}

    public static boolean isEmail(String input) {
        return input.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
    public static boolean isPhoneNo(String input) {
        return input.matches("\\+?\\d{7,15}");
    }}
