package me.shaohsiung.response;

import me.shaohsiung.model.WordSpec;
import me.shaohsiung.util.UuidUtils;

import java.time.LocalDateTime;

/**
 * 欧陆词典开放接口的 HTTP 响应
 */
public class EudbResponse extends BaseResponse {
    private String message;

    public EudbResponse() {
        super(UuidUtils.uuid(), LocalDateTime.now());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void attach(WordSpec wordSpec) {
        throw new UnsupportedOperationException("EudbResponse unsupport attach to word spec");
    }
}
