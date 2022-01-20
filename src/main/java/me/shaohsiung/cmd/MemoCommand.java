package me.shaohsiung.cmd;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.dingding.DingEndpoint;
import me.shaohsiung.ext.AbstractCommand;
import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.MemoMessageContext;
import me.shaohsiung.handler.CommandHandler;
import me.shaohsiung.handler.MemoAddHandler;
import me.shaohsiung.handler.MemoDeleteHandler;
import me.shaohsiung.handler.MemoListHandler;
import me.shaohsiung.model.ArModel;
import me.shaohsiung.util.RestClient;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MemoCommand extends AbstractCommand {
    private final static String usageDocument = "/memo add 备忘📜内容 链接 #标签1 #标签2\n" +
            "/memo delete --code=d3267eb4 --id=2\n" +
            "/memo list --page=1 --size=10";
    
    private final Map<String, CommandHandler> handlers;
    
    private final DingEndpoint dingEndpoint;
    
    public MemoCommand(SilentSender silent) {
        super(silent);
        handlers = new HashMap<>();
        handlers.put("add", new MemoAddHandler());
        handlers.put("delete", new MemoDeleteHandler());
        handlers.put("list", new MemoListHandler());
        
        dingEndpoint = new DingEndpoint(new RestClient());
    }
    
    @Override
    public void action(ExtMessageContext c) {
        MemoMessageContext ctx = (MemoMessageContext) c;
        if (!ctx.hasArguments() || !ctx.isLegalCommand(handlers.keySet())) {
            silent.send("🌚 Oops! Something wrong:\n " +
                    "please enter the sub command of memo\n\n🎯 Usage: \n" + usageDocument, ctx.getChatId());
            return;
        }
        
        CommandHandler handler = handlers.get(ctx.subCommand());
        ArModel memo = handler.handle(ctx);
        sendMessage2Telegram(ctx.getChatId(), memo);
        if (memo.shouldSendMessage2DingTalk()) {
            sendMessage2DingTalk(memo);
        }
    }

    private void sendMessage2DingTalk(ArModel memo) {
        String dingResp = dingEndpoint.sendMessage(memo.dingTalkActionCard());
        log.debug("ding response: {}", dingResp);
    }

    private void sendMessage2Telegram(Long chatId, ArModel memo) {
        String telegramMessage = memo.telegramMessage();
        silent.send(telegramMessage, chatId);
        if (log.isInfoEnabled()) {
            log.info("memo command response: {}", telegramMessage);
        }
    }

    @Override
    public String getName() {
        return "memo";
    }

    @Override
    public String getInfo() {
        return "📝 备忘录";
    }

    @Override
    public Locality getLocality() {
        return Locality.ALL;
    }

    @Override
    public Privacy getPrivacy() { 
        return Privacy.GROUP_ADMIN;
    }
}
