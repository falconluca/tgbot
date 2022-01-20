package me.shaohsiung.response;

import me.shaohsiung.model.WordSpec;

import java.time.LocalDateTime;

public abstract class BaseResponse {
    private String id;
    
    private LocalDateTime generateAt;
    
    public BaseResponse(String id, LocalDateTime generateAt) {
        this.id = id;
        this.generateAt = generateAt;
    }
    
    public abstract void attach(WordSpec wordSpec);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return generateAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.generateAt = createAt;
    }
}
