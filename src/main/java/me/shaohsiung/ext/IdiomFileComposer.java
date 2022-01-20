package me.shaohsiung.ext;

import me.shaohsiung.model.Idiom;

import java.io.File;
import java.util.Map;

public class IdiomFileComposer {
    private final Map<String, Idiom> idiomsByCategory;
    
    public IdiomFileComposer(Map<String, Idiom> idioms) {
        idiomsByCategory = idioms;
    }

    public File composePythonFile() {
        System.out.println(idiomsByCategory);
        return null;
    }

    public File composeJsonFile() {
        return null;
    }
}
