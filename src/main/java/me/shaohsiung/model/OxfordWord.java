package me.shaohsiung.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OxfordWord extends BaseModel {
    private String define;
    private List<String> sentences;
}
