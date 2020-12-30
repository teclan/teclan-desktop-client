package com.teclan.desktop.client.utils;

public class Assert {

    public static boolean assertNullString(String value){
        return null==value||"".equals(value.trim());
    }

    public static boolean assertNotNullString(String value){
        return !assertNullString(value);
    }
}
