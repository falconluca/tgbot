package me.shaohsiung.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
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
}
