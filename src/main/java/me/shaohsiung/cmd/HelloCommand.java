package me.shaohsiung.cmd;

import me.shaohsiung.ext.AbstractCommand;
import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.HelloMessageContext;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

public class HelloCommand extends AbstractCommand {
    public HelloCommand(SilentSender silent) {
        super(silent);
    }

    @Override
    public void action(ExtMessageContext c) {
        HelloMessageContext ctx = (HelloMessageContext) c;
        silent.send("🍫 fine!", ctx.getChatId());
        ctx.debug();
    }

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getInfo() {
        return "Just say hello";
    }

    @Override
    public Locality getLocality() {
        return Locality.ALL;
    }

    @Override
    public Privacy getPrivacy() {
        return Privacy.PUBLIC;
    }
}
