package me.shaohsiung.config;

import lombok.Data;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Data
public class WordBotConfig {
    private ProxyConfig proxy;
    private TelegramConfig telegram;
    private DingdingConfig dingding;
    private EudbConfig eudb;
    
    public String getBotToken() {
        TelegramConfig.BotConfig bot = telegram.getBot();
        if (bot == null) {
            throw new ConfigNotFoundException("cannot found telegram bot");
        }
        
        return bot.getToken();
    }

    public String getBotUserName() {
        TelegramConfig.BotConfig bot = telegram.getBot();
        if (bot == null) {
            throw new ConfigNotFoundException("cannot found telegram bot");
        }

        return bot.getUsername();
    }
    
    public DefaultBotOptions getBotOptions() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(proxy.getHTTPHost());
        botOptions.setProxyPort(proxy.getHTTPPort());
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        return botOptions;
    }

    public Long getBotCreatorId() {
        TelegramConfig.BotConfig bot = telegram.getBot();
        if (bot == null) {
            throw new ConfigNotFoundException("cannot found telegram bot");
        }

        return bot.getCreatorId();
    }

    public String getEudbAccessToken() {
        return eudb.getAccessToken();
    }

    @Data
    static class TelegramConfig {
        private BotConfig bot;
        
        @Data
        static class BotConfig {
            private String token;
            private String username;
            private Long creatorId;
        }
    }

    @Data
    static class DingdingConfig {
        private String secret;
    }

    @Data
    static class EudbConfig {
        private String accessToken;
    }
}
