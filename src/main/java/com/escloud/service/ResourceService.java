package com.escloud.service;

import com.escloud.Auth;
import com.escloud.domain.Resource;
import com.escloud.util.Json;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源服务
 */
public class ResourceService extends BaseService {
    protected String host = "resource-service.qiqiuyun.net";

    public ResourceService(Auth auth, Map<String, String> options) {
        options.put("host", this.host);
        init(auth, options);
    }

    public Resource get(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObject(this.request("GET", "/resources/" + no, new HashMap<>(), new HashMap<>(), "root"), Resource.class);
    }

    public List<Resource> search(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObjectList(this.request("GET", "/resources", params, new HashMap<>(), "root"), Resource.class);
    }

    public Map getDownloadUrl(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObject(this.request("GET", "/resources/" + no + "/downloadUrl", new HashMap<>(), new HashMap<>(), "root"), Map.class);
    }

    public Resource rename(String no, String name) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map<String, String> params = new HashMap();
        params.put("name", name);
        return Json.jsonToObject(this.request("PUT", "/resources/" + no + "/name", params, new HashMap<>(), "root"), Resource.class);
    }

    public String delete(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return this.request("DELETE", "/resources/" + no, new HashMap<>(), new HashMap<>(), "root");
    }
}
