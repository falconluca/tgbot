package me.shaohsiung.model;

import me.shaohsiung.dingding.ActionCardDingTalkMessage;

import java.time.LocalDateTime;
import java.util.List;

public class EnglishWord extends ArModel {
    private Long id;
    private List<String> pronunciationExamples;
    private LocalDateTime createdAt;
    private Long creatorId;

    @Override
    public ActionCardDingTalkMessage dingTalkActionCard() {
        return null;
    }

    @Override
    public String telegramMessage() {
        return null;
    }
}
