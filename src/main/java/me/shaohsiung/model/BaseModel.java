package me.shaohsiung.model;

import java.time.LocalDateTime;

public abstract class BaseModel {
    private String id;
    private LocalDateTime createAt;
    
    public BaseModel(String id, LocalDateTime createAt) {
        this.id = id;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
