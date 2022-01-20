package me.shaohsiung.dingding;

public abstract class AbstractDingMessage {
    protected String msgtype;

    public AbstractDingMessage(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
