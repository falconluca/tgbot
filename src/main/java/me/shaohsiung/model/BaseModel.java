package me.shaohsiung.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseModel {
    private String id;
    private LocalDateTime createAt;

    public BaseModel(String id, LocalDateTime createAt) {
        this.id = id;
        this.createAt = createAt;
    }
}
