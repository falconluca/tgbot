package me.shaohsiung.bot;

import me.shaohsiung.cmd.WordCommand;
import me.shaohsiung.config.WordBotConfig;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

// https://github.com/rubenlagus/TelegramBots/wiki/Simple-Example
public class WordBot extends AbilityBot {
    protected Long creatorId;
    
    protected String eudbAccessToken;
    
    public WordBot(WordBotConfig config) {
        super(config.getBotToken(), config.getBotUserName(), config.getBotOptions());
        this.creatorId = config.getBotCreatorId();
        this.eudbAccessToken = config.getEudbAccessToken();
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
                .action(ctx -> new WordCommand(silent, eudbAccessToken).action(ctx))
                .build();
    }


//    public Ability eudb() {
//        return null;
//    }
}
