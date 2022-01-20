package me.shaohsiung.model;

import lombok.Data;

/**
 * 备忘录的标签
 */
@Data
public class MemoTag {
    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 内容
     */
    private String value;
    
    /**
     * 十六进制颜色 例如 #e3e3e3
     */
    private String color;

    public MemoTag(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public MemoTag(String value) {
        this.value = value;
    }
}
