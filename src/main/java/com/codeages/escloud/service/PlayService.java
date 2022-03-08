package com.codeages.escloud.service;

import com.codeages.escloud.Auth;

import java.util.Date;
import java.util.Map;

/**
 * 播放服务
 */
public class PlayService extends BaseService {
    protected String defaultHost = "play.qiqiuyun.net";

    public PlayService(Auth auth, Map<String, String> options) {
        options.put("host", options.containsKey("host") ? options.get("host") : this.defaultHost);
        init(auth, options);
    }

    public String makePlayToken(String no, int lifetime, Map<String, Object> payload) {
        payload.put("no", no);
        payload.put("exp", new Date(System.currentTimeMillis() + lifetime * 1000));
        return auth.makeJwtToken(payload);
    }
}
