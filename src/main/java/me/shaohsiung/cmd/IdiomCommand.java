package me.shaohsiung.cmd;

import me.shaohsiung.ext.AbstractCommand;
import me.shaohsiung.ext.ExtMessageContext;
import me.shaohsiung.ext.IdiomFileComposer;
import me.shaohsiung.ext.IdiomMessageContext;
import me.shaohsiung.handler.IdiomDumpHandler;
import me.shaohsiung.model.ArModel;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;

import java.io.File;

public class IdiomCommand extends AbstractCommand {
    private final static String usageDocument = "/idiom add 名句名句名句 来源来源 类目\n" +
            "/idiom dump --json\n" +
            "/idiom dump --py";
    
    public IdiomCommand(SilentSender silent) {
        super(silent);
    }

    @Override
    public void action(ExtMessageContext c) {
        IdiomMessageContext ctx = (IdiomMessageContext) c;
        if (!ctx.isLegalCommand()) {
            silent.send("🌚 Oops! Something wrong:\n " +
                    "please enter the sub command of idiom\n\n🎯 Usage: \n" + usageDocument, ctx.getChatId());
            return;
        }
        
        if (ctx.isAddCommand()) {
            return;
        }
        
        if (ctx.isDumpCommand()) {
            IdiomDumpHandler handler = new IdiomDumpHandler();
            ArModel idioms = handler.handle(c);
            
            // builder by third arg. eg: py
            IdiomFileComposer fileComposer = new IdiomFileComposer(null);
            File py = fileComposer.composePythonFile();
            
            // upload to cloud
            
            // dingTalk
            
            // telegram response
            
            // Happy Path Programming Pattern
            return;
        }
    }

    @Override
    public String getName() {
        return "idiom";
    }

    @Override
    public String getInfo() {
        return "🗂 记录喜欢的句子";
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
