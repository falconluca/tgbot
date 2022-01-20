package me.shaohsiung.handler;

import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.IdiomMessageContext;
import me.shaohsiung.model.ArModel;

public class IdiomDumpHandler implements CommandHandler {
    @Override
    public ArModel handle(ExtMessageContext c) {
        IdiomMessageContext ctx = (IdiomMessageContext) c;
        // 获取数据库的数据
        // group by
        return null;
    }
}
