package me.shaohsiung.sounder;

/**
 * YouTube 单词发音
 */
public class YouTubeSounder {
    public String list(String word) {
        return String.format("https://youglish.com/pronounce/%s/english?", word.trim());
    }
}
