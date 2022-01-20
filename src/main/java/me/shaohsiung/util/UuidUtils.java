package me.shaohsiung.util;

import java.util.UUID;

public class UuidUtils {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","").substring(0,8);
    }
}
