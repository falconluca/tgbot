package me.shaohsiung.ext;

import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.objects.MessageContext;

@Slf4j
public class HelloMessageContext implements ExtMessageContext {
    private final MessageContext ctx;
    
    public HelloMessageContext(MessageContext ctx) {
        this.ctx = ctx;
    }

    public static ExtMessageContext of(MessageContext ctx) {
        return new HelloMessageContext(ctx);
    }
    
    public Long getChatId() {
        return ctx.chatId();
    }
    
    public void debug() {
        log.info("hello命令的第一个参数: {}, 第二个参数: {}, 第三个参数: {}, 参数: {}", 
                ctx.firstArg(), ctx.secondArg(), ctx.thirdArg(), ctx.arguments());
    }
}
