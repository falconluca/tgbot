package me.shaohsiung.bot;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.config.BotConfig;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class DoomfistBot extends TelegramLongPollingBot {
    public DoomfistBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    // https://core.telegram.org/bots/api#update
    @Override
    public void onUpdateReceived(Update update) {
        log.info("机器人接收到消息, update: {}", update);

        // We check if the update has a message and the message has text
        if (!update.hasMessage()) {
            return;
        }

        Message UpdateMessage = update.getMessage();
        if (!UpdateMessage.hasText()) {
            return;
        }

        SendMessage reply = new SendMessage(); // Create a SendMessage object with mandatory fields
        reply.setChatId(UpdateMessage.getChatId().toString());
        reply.setText(text(UpdateMessage.getText()));
        try {
            execute(reply); // Call method to send the message
        }
        catch (TelegramApiException e) {
            log.error("机器人响应失败", e);
        }
    }

    private String text(String src) {
        if (src.contains("game")) {
            return "投稿阿里云盘盘：\n" +
"多年来从某宝购买、论坛、交流群等收集的各种梦幻西游单机版，各种老版本新版本，各种剧情，各种修改，各种定制，可玩性很强，" +
"自由度极高，彻底放飞自我，哪怕仅仅只是去逛逛风景听听音乐找找童年的感觉，喜欢梦幻的可以收藏。\n" +
"下载后改jpg为zip解压\n" +
"\n" +
"链接：https://www.aliyundrive.com/s/jNBvg3y8kZr";
        }
        return src;
    }
}
