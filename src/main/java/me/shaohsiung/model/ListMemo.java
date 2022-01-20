package me.shaohsiung.model;

import me.shaohsiung.dingding.ActionCardDingTalkMessage;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ListMemo extends Memo {
    private final List<Memo> memos;

    private final Integer page;
    
    private final Integer size;
    
    private final Integer total;

    ListMemo(List<Memo> memos, Integer page, Integer size, Integer total) {
        super(null, null, null, null, null, null, null, null);
        this.memos = memos;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public static ListMemo of(List<Memo> memos, Integer page, Integer size, Integer total) {
        return new ListMemo(memos, page, size, total);
    }

    @Override
    public ActionCardDingTalkMessage dingTalkActionCard() {
        // TODO
        return super.dingTalkActionCard();
    }

    @Override
    public String telegramMessage() {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("🚀 total: ").append(total)
                .append(" page: ").append(page)
                .append(" size: ").append(size)
                .append("\n\n################\n\n");
        if (CollectionUtils.isEmpty(memos)) {
            msgBuilder.append("📭 There is no memo yet");
            return msgBuilder.toString();
        }
        
        for (Memo m : memos) {
            msgBuilder.append("🧾 Memo #").append(m.id).append("\n\n")
                    .append("🏴‍☠️ CreatedAt: ").append(m.getCreatedAt()).append("\n\n") // TODO 格式化时间
                    .append("🍩 Content: ").append(m.getContent()).append("\n\n")
                    .append("🔗 URL: ").append(m.getUrl()).append("\n\n") // TODO URL断链
                    .append("😈 ConfirmCode: ").append(m.getConfirmCode()).append("\n\n")
                    .append("################\n\n");
        }
        return msgBuilder.toString();
    }
}
