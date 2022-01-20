package me.shaohsiung.model;

import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;

public class SoundData extends BaseModel {
    private String url;
    private String from;

    public SoundData(String url, String from) {
        super(UuidUtils.uuid(), LocalDateTime.now());
        this.url = url;
        this.from = from;
    }

    public static SoundData of(String url, String from) {
        return new SoundData(url, from);
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
}
