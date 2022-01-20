package me.shaohsiung.ext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MemoMessageContext implements ExtMessageContext { // TODO 使用访问者模式进行重构
    private final static Integer DEFAULT_PAGE = 1;
    
    private final static Integer DEFAULT_SIZE = 5;
    
    private final MessageContext ctx;
    
    private final List<Argument> arguments;
    
    public MemoMessageContext(MessageContext ctx) {
        this.ctx = ctx;
        
        this.arguments = Stream
                .of(ctx.arguments())
                .map(Argument::new)
                .collect(Collectors.toList());
    }

    public static MemoMessageContext of(MessageContext ctx) {
        return new MemoMessageContext(ctx);
    }

    public Message getMessage() {
        Update update = ctx.update();
        if (!update.hasMessage()) {
            throw new IllegalStateException(String.format(
                    "cannot get message for update. chatId: %s, userId: %s", ctx.chatId(), ctx.user().getId()));
        }
        return update.getMessage();
    }
    
    public String getText() {
        return getMessage().getText();
    }
    
    public Long getChatId() {
        return ctx.chatId();
    }
    
    /**
     * @return 检查命令格式是否合法
     */
    public boolean isLegalCommand(Set<String> subCommandKeys) {
        return subCommandKeys
                .stream()
                .anyMatch(subCommand()::startsWith);
    }

    /**
     * @return 返回命令中的标签, 如果没有命令没有任何标签, 那就返回默认标签
     */
    public Set<String> getTags() {
        Set<String> tags = arguments
                .stream()
                .filter(Argument::isTag)
                .map(Argument::tagString)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(tags)) {
            return tags;
        }
        
        return Collections.singleton("默认");
    }
    
    public String getUrl() {
        return arguments
                .stream()
                .filter(Argument::isUrl)
                .map(Argument::toString)
                .findFirst().orElse("没有链接");
    }
    
    public String getContent() {
        return arguments
                .stream()
                .filter(arg -> !arg.isTag())
                .filter(arg -> !arg.isUrl())
                .filter(arg -> !arg.isEquals(subCommand()))
                .map(Argument::toString)
                .findAny().orElse("没有内容");
    }

    public String subCommand() {
        return ctx.firstArg();
    }

    /**
     * @return 返回发起命令的用户ID
     */
    public Long creatorId() {
        return getMessage().getFrom().getId();
    }
    
    public boolean hasArguments() {
        return arguments.size() > 0;
    }

    public String getConfirmCode() {
        return arguments
                .stream()
                .filter(Argument::isConfirmCode)
                .map(Argument::confirmCode)
                .findFirst()
                .orElse("");
    }

    public String getDeleteMemoId() {
        return arguments
                .stream()
                .filter(Argument::isId)
                .map(Argument::id)
                .findFirst()
                .orElse("");
    }

    public int getPage() {
        return arguments
                .stream()
                .filter(Argument::isPage)
                .map(Argument::page)
                .findFirst()
                .orElse(DEFAULT_PAGE);
    }

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

    public int getSize() {
        return arguments
                .stream()
                .filter(Argument::isSize)
                .map(Argument::size)
                .findFirst()
                .orElse(DEFAULT_SIZE);
    }
}
