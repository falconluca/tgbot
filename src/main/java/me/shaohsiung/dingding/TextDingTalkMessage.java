package me.shaohsiung.dingding;

public class TextDingTalkMessage extends AbstractDingMessage {
    private String content;

    public TextDingTalkMessage(String content) {
        super("text");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
