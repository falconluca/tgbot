package me.shaohsiung.model;

import lombok.Data;

import java.util.List;

@Data
public class LongmanWord {
    private String define;
    private List<String> sentences;
}
