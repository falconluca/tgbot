package me.shaohsiung.util;

import org.apache.commons.lang3.StringUtils;

public class AssertUtils {
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new RuntimeException(msg);
        }
    }

    public static void hasText(String s, String msg) {
        if (StringUtils.isBlank(s)) {
            throw new RuntimeException(msg);
        }
    }
}
