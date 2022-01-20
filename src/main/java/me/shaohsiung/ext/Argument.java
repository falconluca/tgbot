package me.shaohsiung.ext;

import java.util.Set;

public class Argument {
    private final String value;
    
    private final Set<String> urlPrefixList = Set.of("http://", "https://", "www."); // FIXME 正则表达式

    public Argument(String value) {
        this.value = value;
    }
    
    public boolean isTag() {
        return value.startsWith("#");
    }
    
    public String tagString() {
        if (!isTag()) {
            throw new IllegalStateException("Argument: " + value + " is not a tag");
        }
        return value.replace("#", "");
    }
    
    public boolean isUrl() {
        return urlPrefixList
                .stream()
                .anyMatch(value::startsWith);
    }

    public boolean isEquals(String arg) {
        return value.equals(arg);
    }
    
    public String toString() {
        return value;
    }
    
    public boolean isConfirmCode() {
        return value.startsWith("--code=");
    }
    
    public String confirmCode() {
        if (!isConfirmCode()) {
            throw new IllegalStateException("Argument: " + value + " is not a confirmCode");
        }
        return value.replace("--code=", "");
    }

    public boolean isId() {
        return value.startsWith("--id=");
    }

    public String id() {
        if (!isId()) {
            throw new IllegalStateException("Argument: " + value + " is not a id");
        }
        return value.replace("--id=", "");
    }

    public boolean isPage() {
        return value.startsWith("--page=");
    }

    public int page() {
        if (!isPage()) {
            throw new IllegalStateException("Argument: " + value + " is not a page");
        }
        String pageString = value.replace("--page=", "");
        return Integer.parseInt(pageString);
    }

    public boolean isSize() {
        return value.startsWith("--size=");
    }

    public int size() {
        if (!isSize()) {
            throw new IllegalStateException("Argument: " + value + " is not a size");
        }
        String sizeString = value.replace("--size=", "");
        return Integer.parseInt(sizeString);
    }
}
