package com.codeages.escloud.service;

import com.codeages.escloud.Auth;

import java.util.Date;
import java.util.Map;

/**
 * 播放服务
 */
public class PlayService extends BaseService {
    protected String host = "play.qiqiuyun.net";

    public PlayService(Auth auth, Map<String, String> options) {
        String host = options.put("host", this.host);
        init(auth, options);
    }
    
    public String makePlayToken(String no, int lifetime, Map<String, Object> payload) {
        payload.put("no", no);
        payload.put("exp", new Date(System.currentTimeMillis() + lifetime * 1000));
        return auth.makeJwtToken(payload);
    }
}
