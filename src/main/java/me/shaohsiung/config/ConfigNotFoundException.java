package me.shaohsiung.config;

public class ConfigNotFoundException extends RuntimeException {
    public ConfigNotFoundException(String errMsg) {
        super(errMsg);
    }
}
