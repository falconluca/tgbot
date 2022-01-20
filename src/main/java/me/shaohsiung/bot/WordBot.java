package me.shaohsiung.bot;

import me.shaohsiung.config.WordBotConfig;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

// https://github.com/rubenlagus/TelegramBots/wiki/Simple-Example
public class WordBot extends AbilityBot {
    protected Long creatorId;
    
    public WordBot(WordBotConfig config) {
        super(config.getBotToken(), config.getBotUserName(), config.getBotOptions());
        this.creatorId = config.getBotCreatorId();
    }

    @Override
    public long creatorId() {
        return creatorId;
    }
    
    public Ability words() {
        return Ability
                .builder()
                .name("word")
                .info("explore the meaning of word")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    silent.send("pong", ctx.chatId());
                })
                .build();
    }
}
