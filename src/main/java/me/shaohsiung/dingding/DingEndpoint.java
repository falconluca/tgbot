package me.shaohsiung.dingding;

import me.shaohsiung.util.DingSignUtils;
import me.shaohsiung.util.RestClient;

// https://developers.dingtalk.com/document/robots
public class DingEndpoint {
    private final RestClient client;
    
    private final static String ACCESS_TOKEN = "284aa3fa0d954fdc037dde3036868904e7572e360964ddac1f57f55eb4db80bc"; // FIXME

    public DingEndpoint(RestClient client) {
        this.client = client;
    }
    
    public String sendMessage(AbstractDingMessage msg) {
        Long timestamp = System.currentTimeMillis();
        String url = String.format("https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s", 
                ACCESS_TOKEN, timestamp, DingSignUtils.sign(timestamp));
        return client.post(url, msg);
    }
}
