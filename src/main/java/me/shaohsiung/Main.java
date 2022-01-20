package me.shaohsiung;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.bot.WatchdogBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // VM options
        String name = System.getProperty("name");
        System.out.println(name);
        
        try {
            TelegramBotsApi bots = new TelegramBotsApi(DefaultBotSession.class);
            bots.registerBot(new WatchdogBot());
        } 
        catch (TelegramApiException e) {
            log.error("🤖 bot failed to start", e);
        }
    }
}
