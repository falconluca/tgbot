package me.shaohsiung.model;

import lombok.Data;

import java.util.List;

@Data
public class OxfordWord {
    private String define;
    private List<String> sentences;
}
