package me.shaohsiung.model;

import me.shaohsiung.dingding.ActionCardDingTalkMessage;

public abstract class ArModel {
    protected Boolean sendMessage2DingTalk = false;

    public abstract ActionCardDingTalkMessage dingTalkActionCard();

    public abstract String telegramMessage();

    public void enableSendMessage2DingTalk() {
        this.sendMessage2DingTalk = true;
    }

    public boolean shouldSendMessage2DingTalk() {
        return sendMessage2DingTalk;
    }
}
