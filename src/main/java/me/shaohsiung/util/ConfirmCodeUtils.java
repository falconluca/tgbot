package me.shaohsiung.util;

import java.util.UUID;

public class ConfirmCodeUtils {
    public static String randomConfirmCode() {
        return UUID.randomUUID().toString().replace("-","").substring(0,8);
    }
}
