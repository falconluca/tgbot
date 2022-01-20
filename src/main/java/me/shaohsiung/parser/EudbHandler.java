package me.shaohsiung.parser;

import me.shaohsiung.model.EudbResponse;
import me.shaohsiung.util.JsonUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class EudbHandler {
    private final EudbResponse response;

    public EudbHandler(EudbResponse response) {
        Assert.notNull(response, "response");
        this.response = response;
    }

    public static EudbHandler of(String json) {
        Assert.hasText(json, "html must not be blank.");

        EudbResponse eudbResponse = JsonUtils.toObject(json, EudbResponse.class);
        return new EudbHandler(eudbResponse);
    }
    
    public boolean izSuccess() {
        String msg = response.getMessage();
        if (!StringUtils.hasText(msg)) {
            return false;
        }
        
        return msg.contains("单词导入成功");
    }
    
    public Integer getImportCount() {
        if (!izSuccess()) {
            return 0;
        }
        
        String msg = response.getMessage();
        int idx = msg.indexOf(" : ");
        return Integer.parseInt(msg.substring(idx + 3));
    }
    
    public EudbResponse getEntity() {
        return response;
    }
}
