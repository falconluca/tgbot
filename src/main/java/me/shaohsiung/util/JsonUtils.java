package me.shaohsiung.util;

import com.google.gson.Gson;

public class JsonUtils {
    private final static Gson gson;
    
    static {
        gson = new Gson();
    }
    
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
