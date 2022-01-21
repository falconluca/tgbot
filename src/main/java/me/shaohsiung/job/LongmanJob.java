package me.shaohsiung.job;

import me.shaohsiung.parser.LongmanParser;
import me.shaohsiung.response.BaseResponse;
import me.shaohsiung.util.HTTPUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;

import java.util.List;

public class LongmanJob extends WordJob {
    public LongmanJob(String key, String url) {
        super(key, url);
    }

    @Override
    public HttpUriRequest prepareRequest(String word) {
        final HttpGet httpGet = new HttpGet(String.format(url, word));
        httpGet.setHeader(HTTP.USER_AGENT, HTTPUtils.userAgent());
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        return httpGet;
    }

    @Override
    public List<BaseResponse> handleResponse(String body) {
        LongmanParser longmanParser = LongmanParser.of(body);
        return longmanParser.parse();
    }
}
