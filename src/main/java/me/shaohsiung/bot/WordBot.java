package me.shaohsiung.bot;

import me.shaohsiung.cmd.WordCommand;
import me.shaohsiung.config.EudbConfig;
import me.shaohsiung.config.WordBotConfig;
import me.shaohsiung.enums.WordEnums;
import me.shaohsiung.job.EudbJob;
import me.shaohsiung.job.LongmanJob;
import me.shaohsiung.job.OxfordJob;
import me.shaohsiung.job.WordJob;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

// https://github.com/rubenlagus/TelegramBots/wiki/Simple-Example
public class WordBot extends AbilityBot {
    protected final Long creatorId;
    
    protected final EudbConfig eudbConfig;
    
    public WordBot(WordBotConfig config) {
        super(config.getBotToken(), config.getBotUserName(), config.getBotOptions());
        this.creatorId = config.getBotCreatorId();
        this.eudbConfig = config.getEudb();
    }

    @Override
    public long creatorId() {
        return creatorId;
    }
    
    public Ability word() {
        Map<String, WordJob> jobs = prepareWordJobs();
        return Ability
                .builder()
                .name("word")
                .info("explore the meaning of word")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> new WordCommand(silent, jobs).action(ctx))
                .build();
    }

    private Map<String, WordJob> prepareWordJobs() {
        Map<String, WordJob> jobs = new ConcurrentHashMap<>();
        jobs.put(WordEnums.LONGMAN.name(), new LongmanJob(WordEnums.LONGMAN.name(), 
                "https://www.ldoceonline.com/dictionary/%s"));

        jobs.put(WordEnums.OXFORD.name(), new OxfordJob(WordEnums.OXFORD.name(), 
                "https://www.oxfordlearnersdictionaries.com/definition/american_english/%s"));

        EudbJob eudbJob = new EudbJob(WordEnums.EUDB.name(), "https://api.frdic.com/api/open/v1/studylist/words");
        eudbJob.setAccessToken(eudbConfig.getAccessToken());
        eudbJob.setDictionaryId(eudbConfig.getDictionaryId());
        jobs.put(WordEnums.EUDB.name(), eudbJob);
        return jobs;
    }
}
