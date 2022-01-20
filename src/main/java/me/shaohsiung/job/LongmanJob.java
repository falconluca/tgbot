package me.shaohsiung.job;

import me.shaohsiung.util.HTTPUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;

public class LongmanJob extends WordJob {
    public LongmanJob(String key, String url) {
        super(key, url);
    }

    @Override
    public HttpUriRequest prepareRequest(String word) {
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.USER_AGENT, HTTPUtils.userAgent());
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        return httpGet;
    }

    @Override
    public void handleResponse(String body) {
        
    }
}
