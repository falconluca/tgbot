package me.shaohsiung.job;

import lombok.extern.slf4j.Slf4j;
import me.shaohsiung.parser.EudbHandler;
import me.shaohsiung.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.Collections;
import java.util.List;

@Slf4j
public class EudbJob extends WordJob {
    private String dictionaryId;
    
    private String accessToken;
    
    private final static String DEFAULT_DICTIONARY_ID = "0";
    
    public EudbJob(String key, String url) {
        super(key, url);
    }

    @Override
    public HttpUriRequest prepareRequest(String word) {
        final HttpPost httPost = new HttpPost(url);
        httPost.setHeader("Authorization", accessToken);
        httPost.setHeader("Content-Type", "application/json");
        
        String id = StringUtils.isBlank(dictionaryId) ? DEFAULT_DICTIONARY_ID : dictionaryId;
        String body = "{\n" +
                "    \"id\": \""+  id +"\",\n" +
                "    \"language\": \"en\",\n" +
                "    \"words\": [\n" +
                "        \"" + word + "\"\n" +
                "    ]\n" +
                "}";
        HttpEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
        httPost.setEntity(stringEntity);
        return httPost;
    }

    @Override
    public List<BaseResponse> handleResponse(String body) {
        EudbHandler eudbHandler = EudbHandler.of(body);
        if (!eudbHandler.izSuccess()) {
            return Collections.emptyList();
        }
        
        Integer importCount = eudbHandler.getImportCount();
        log.info("eudb import word count: " + importCount);
        return Collections.emptyList();
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
