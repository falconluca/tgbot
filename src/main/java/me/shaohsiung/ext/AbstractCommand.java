package me.shaohsiung.ext;

import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

public abstract class AbstractCommand {
    protected final SilentSender silent;

    public AbstractCommand(SilentSender silent) {
        this.silent = silent;
    }

    public abstract void action(ExtMessageContext ctx);

    public abstract String getName();

    public abstract String getInfo();

    public abstract Locality getLocality();

    public abstract Privacy getPrivacy();
}
