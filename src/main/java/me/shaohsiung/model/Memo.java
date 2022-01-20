package me.shaohsiung.model;

import lombok.Builder;
import lombok.Data;
import me.shaohsiung.dingding.ActionCardDingTalkMessage;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Memo extends ArModel {
    protected Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long creatorId;
    private List<MemoTag> tags;
    private String originText;
    private String url;
    private String confirmCode;

    @Override
    public ActionCardDingTalkMessage dingTalkActionCard() {
        return new ActionCardDingTalkMessage(title(), content, url);
    }
    
    public String title() {
        return "📋 Memo #" + id;
    }

    @Override
    public String telegramMessage() {
        return "🍾 Congratulations!!!\n\n" + title() + " has collected success\n\n🧋 ConfirmCode is " + confirmCode;
    }
}
