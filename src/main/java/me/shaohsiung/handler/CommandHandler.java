package me.shaohsiung.handler;

import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.model.ArModel;

public interface CommandHandler {
    ArModel handle(ExtMessageContext ctx);
}
