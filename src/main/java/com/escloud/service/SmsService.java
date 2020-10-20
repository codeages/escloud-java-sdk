package com.escloud.service;

import com.escloud.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmsService extends BaseService {
    protected String host = "sms-service.cloud-test.edusoho.cn";

    protected String[] leafHost = new String[]{"sms-service-leaf1.qiqiuyun.net", "sms-service-leaf2.qiqiuyun.net"};

    public SmsService(Auth auth, Map<String, String> options) {
        String host = options.put("host", this.host);
        init(auth, options);
    }

    /**
     * 单发短信接口
     *
     * @param params
     * @return
     */
    public String sendToOne(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException {
        return this.request("POST", "/messages", params, new HashMap<>(), "root");
    }

    /**
     * 群发文本短信
     *
     * @param params
     * @return
     */
    public String sendToMany(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException {
        return this.request("POST", "/messages/batch_messages", params, new HashMap<>(), "root");
    }

    /**
     * 添加签名
     *
     * @param params
     * @return
     */
    public String addSign(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException {
        return this.request("POST", "/signs", params, new HashMap<>(), "root");
    }

    /**
     * 添加模板
     *
     * @param params
     * @return
     */
    public String addTemplate(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException {
        return this.request("POST", "/templates", params, new HashMap<>(), "root");
    }
}
