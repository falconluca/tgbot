package me.shaohsiung.sounder;

import me.shaohsiung.enums.PronunciationEnums;

/**
 * 有道单词发音
 */
public class YoudaoSounder {
    public String usa(String word) {
        return String.format("http://dict.youdao.com/dictvoice?type=%s&audio=%s", 
                PronunciationEnums.AMERICAN.getValue(), word);
    }

    public String british(String word) {
        return String.format("http://dict.youdao.com/dictvoice?type=%s&audio=%s",
                PronunciationEnums.BRITISH.getValue(), word);
    }
}
