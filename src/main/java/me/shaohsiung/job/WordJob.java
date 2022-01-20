package me.shaohsiung.job;

import org.apache.http.client.methods.HttpUriRequest;

public abstract class WordJob {
    protected String key;
    protected String url;

    public WordJob(String key, String url) {
        this.key = key;
        this.url = url;
    }

    protected String meta() {
        return "key: " + key + ", url: " + url;
    }

    public abstract HttpUriRequest prepareRequest(String word);

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public abstract void handleResponse(String body);
}
