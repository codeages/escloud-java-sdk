package com.escloud.service;

import com.escloud.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import com.escloud.httpClient.Client;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService {

    private static Logger logger = LoggerFactory.getLogger(BaseService.class);


    protected Auth auth;

    private Client client;
    /**
     * API hosts
     *
     * @var string
     */
    protected String host;

    /**
     * API leaf host
     *
     * @var string
     */
    protected String leafHost;

    public void init(Auth auth, Map<String, String> options) {
        this.auth = auth;
        if (options.containsKey("host")) {
            this.host = options.get("host");
        }

        if (options.containsKey("leafHost")) {
            this.leafHost = options.get("leafHost");
        }
    }

    protected Client createClient() {
        if (this.client != null) {
            return this.client;
        }
        this.client = new Client();

        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    protected String request(String method, String uri, Map<String, String> params, Map<String, String> headers, String node) throws URISyntaxException, UnsupportedEncodingException, JSONException {
        String authorization = this.auth.makeRequestAuthorization(uri, params, 600, true);
        System.out.println(authorization);
        headers.put("Authorization", authorization);
        headers.put("Content-Type", "application/json");

        return this.createClient().request(method, getRequestUri(uri, node), params, headers);
    }

    private String getRequestUri(String uri, String node) {
        host = getHostByNode(node);

        if (host.isEmpty()) {
            throw new RuntimeException("API host is not exist or invalid.");
        }

        return "http://" + host + uri;
    }

    private String getHostByNode(String node) {
        if (node.equals("leaf")) {
            return this.leafHost.isEmpty() ? this.host : this.leafHost;
        }

        return this.host;
    }
}
