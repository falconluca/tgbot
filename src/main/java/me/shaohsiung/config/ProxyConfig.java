package me.shaohsiung.config;

import lombok.Data;

@Data
public class ProxyConfig {
    private HTTPConfig http;
    
    @Data
    public static class HTTPConfig {
        private String host;
        private Integer port;
    }
    
    public String getHTTPHost() {
        if (http == null) {
            throw new ConfigNotFoundException("cannot found HTTP proxy");
        }
        return http.getHost();
    }

    public Integer getHTTPPort() {
        if (http == null) {
            throw new ConfigNotFoundException("cannot found HTTP proxy");
        }
        return http.getPort();
    }
}
