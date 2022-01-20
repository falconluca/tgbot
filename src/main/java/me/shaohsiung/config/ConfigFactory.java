package me.shaohsiung.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ConfigFactory {
    public static WordBotConfig loadWordBotConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        
        WordBotConfig config;
        try {
            config = mapper.readValue(new File("src/main/resources/bot.yml"), WordBotConfig.class);
        } 
        catch (IOException e) {
            throw new RuntimeException("yaml config parse failed", e);
        }
        return config;
    }
}
