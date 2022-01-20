package me.shaohsiung.model;

import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;
import java.util.List;

public class LongmanWord extends BaseModel {
    private String define;
    private List<String> sentences;

    public LongmanWord() {
        super(UuidUtils.uuid(), LocalDateTime.now());
    }

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }
}
