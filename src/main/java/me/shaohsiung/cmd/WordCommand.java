package me.shaohsiung.cmd;

import me.shaohsiung.ext.AbstractCommand;
import me.shaohsiung.ext.ExtMessageContext;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

public class WordCommand extends AbstractCommand {
    public WordCommand(SilentSender silent) {
        super(silent);
    }

    @Override
    public void action(ExtMessageContext ctx) {
        
    }

    @Override
    public String getName() {
        return "en";
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Locality getLocality() {
        return null;
    }

    @Override
    public Privacy getPrivacy() {
        return null;
    }
}
