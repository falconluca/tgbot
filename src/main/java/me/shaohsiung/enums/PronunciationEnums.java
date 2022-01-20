package me.shaohsiung.enums;

public enum PronunciationEnums {
    AMERICAN(0), 
    BRITISH(1)
    ;
    
    private final Integer value;

    PronunciationEnums(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
