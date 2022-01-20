package me.shaohsiung.config;

import org.telegram.telegrambots.bots.DefaultBotOptions;

public class BotConfig {
    public static String BOT_TOKEN = "XXX";
    public static String BOT_USERNAME = "XXX";
    
    public static Long CREATOR_ID = 0L;
    
    private static final String HTTP_PROXY_HOST = "127.0.0.1";
    private static final Integer HTTP_PROXY_PORT = 8010;
    
    public static DefaultBotOptions getBotOptions() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(HTTP_PROXY_HOST);
        botOptions.setProxyPort(HTTP_PROXY_PORT);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        return botOptions;
    }
}
