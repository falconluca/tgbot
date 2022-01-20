package me.shaohsiung.bot;

import me.shaohsiung.cmd.HelloCommand;
import me.shaohsiung.cmd.IdiomCommand;
import me.shaohsiung.cmd.MemoCommand;
import me.shaohsiung.config.BotConfig;
import me.shaohsiung.ext.HelloMessageContext;
import me.shaohsiung.ext.IdiomMessageContext;
import me.shaohsiung.ext.MemoMessageContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

// https://github.com/rubenlagus/TelegramBots/wiki/Simple-Example
public class WatchdogBot extends AbilityBot {
    public WatchdogBot() {
        super(BotConfig.BOT_TOKEN, BotConfig.BOT_USERNAME, BotConfig.getBotOptions());
    }

    @Override
    public long creatorId() {
        return BotConfig.CREATOR_ID;
    }
    
    public Ability hello() {
        HelloCommand hello = new HelloCommand(silent);
        return Ability
                .builder()
                .name(hello.getName())
                .info(hello.getInfo())
                .locality(hello.getLocality())
                .privacy(hello.getPrivacy())
                .action(ctx -> hello.action(HelloMessageContext.of(ctx)))
                .build();
    }
    
    public Ability memo() {
        MemoCommand memo = new MemoCommand(silent);
        return Ability
                .builder()
                .name(memo.getName())
                .info(memo.getInfo())
                .locality(memo.getLocality())
                .privacy(memo.getPrivacy())
                .action(ctx -> memo.action(MemoMessageContext.of(ctx)))
                .build();
    }

    public Ability idiom() {
        IdiomCommand idiom = new IdiomCommand(silent);
        return Ability
                .builder()
                .name(idiom.getName())
                .info(idiom.getInfo())
                .locality(idiom.getLocality())
                .privacy(idiom.getPrivacy())
                .action(ctx -> idiom.action(IdiomMessageContext.of(ctx)))
                .build();
    }
}
