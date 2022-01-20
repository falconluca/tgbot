package me.shaohsiung.ext;

import org.telegram.abilitybots.api.objects.MessageContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IdiomMessageContext implements ExtMessageContext {
    private final MessageContext ctx;

    private final List<Argument> arguments;
    
    private final static String ADD_SUB_COMMAND = "add";
    
    private final static String DUMP_SUB_COMMAND = "dump";
    
    private final Set<String> subCommands = Set.of(ADD_SUB_COMMAND, DUMP_SUB_COMMAND);

    public IdiomMessageContext(MessageContext ctx) {
        this.ctx = ctx;

        this.arguments = Stream
                .of(ctx.arguments())
                .map(Argument::new)
                .collect(Collectors.toList());
    }

    public static IdiomMessageContext of(MessageContext ctx) {
        return new IdiomMessageContext(ctx);
    }
    
    public Long getChatId() {
        return ctx.chatId();
    }
    
    public boolean isLegalCommand() {
        if (arguments.size() == 0) {
            return false;
        }
        
        return subCommands
                .stream()
                .anyMatch(s -> s.equals(getSubCommand()));
    }
    
    public boolean isAddCommand() {
        return ADD_SUB_COMMAND.equals(getSubCommand());
    }

    public boolean isDumpCommand() {
        return DUMP_SUB_COMMAND.equals(getSubCommand());
    }

    private String getSubCommand() {
        return ctx.firstArg();
    }
}
