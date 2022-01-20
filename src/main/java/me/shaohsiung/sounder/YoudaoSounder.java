package me.shaohsiung.sounder;

/**
 * 有道单词发音
 */
public class YoudaoSounder {
    private static final Integer AMERICAN_PRONUNCIATION = 0;
    private static final Integer BRITISH_PRONUNCIATION = 1;

    public String usa(String word) {
        return String.format("http://dict.youdao.com/dictvoice?type=%s&audio=%s", AMERICAN_PRONUNCIATION, word.trim());
    }

    public String british(String word) {
        return String.format("http://dict.youdao.com/dictvoice?type=%s&audio=%s", BRITISH_PRONUNCIATION, word.trim());
    }
}
