package me.shaohsiung;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.bot.WordBot;
import me.shaohsiung.config.ConfigFactory;
import me.shaohsiung.config.WordBotConfig;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class Main {
    public static void main(String[] args) {
        TelegramBotsApi bots = null;
        try {
            bots = new TelegramBotsApi(DefaultBotSession.class);
        } 
        catch (TelegramApiException e) {
            log.error("🤖 bot startup failed", e);
            System.exit(-1);
        }
        
        WordBotConfig config = ConfigFactory.loadWordBotConfig();
        WordBot wordBot = new WordBot(config);
        try {
            bots.registerBot(wordBot);
        } 
        catch (TelegramApiException e) {
            log.error("🤖 bot register bot failed", e);
        }
    }
}
