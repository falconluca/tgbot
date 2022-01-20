package me.shaohsiung.model;

import me.shaohsiung.enums.PronunciationEnums;

public class Pronunciation {
    private String word;
    private String url;
    private String from;
    private PronunciationEnums type;

    public Pronunciation(String word, String url, String from, PronunciationEnums type) {
        this.word = word;
        this.url = url;
        this.from = from;
        this.type = type;
    }

    public static Pronunciation of(String word, String url, String from, PronunciationEnums type) {
        return new Pronunciation(word, url, from, type);
    }
    
    public void attach(WordSpec wordSpec) {
        if (PronunciationEnums.BRITISH.equals(type)) {
            wordSpec.setBritishPronunciation(url);
        }
        if (PronunciationEnums.AMERICAN.equals(type)) {
            wordSpec.setAmericanPronunciation(url);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public PronunciationEnums getType() {
        return type;
    }

    public void setType(PronunciationEnums type) {
        this.type = type;
    }
}
