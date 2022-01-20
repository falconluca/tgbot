package me.shaohsiung.model;

import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;

public class EudbResponse extends BaseModel {
    private String message;

    public EudbResponse() {
        super(UuidUtils.uuid(), LocalDateTime.now());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
