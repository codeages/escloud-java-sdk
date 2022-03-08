package com.codeages.escloud.service;

import com.codeages.escloud.Auth;
import com.codeages.escloud.helper.upload.FileRecorder;
import com.codeages.escloud.helper.upload.Recorder;
import com.codeages.escloud.helper.upload.UploadManager;
import com.codeages.escloud.util.Json;
import com.codeages.escloud.domain.Resource;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源服务
 */
public class ResourceService extends BaseService {
    protected String defaultHost = "resource-service.qiqiuyun.net";
    protected String recordPath = "logs/tmp/";

    public ResourceService(Auth auth, Map<String, String> options) {
        options.put("host", options.containsKey("host") ? options.get("host") : this.defaultHost);
        init(auth, options);
    }

    public Resource get(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObject(this.request("GET", "/resources/" + no, new HashMap<>(), new HashMap<>(), "root"), Resource.class);
    }

    public List<Resource> search(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObjectList(this.request("GET", "/resources", params, new HashMap<>(), "root"), Resource.class);
    }

    public Map getDownloadUrl(String no, Boolean isSsl) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        HashMap<String, String> requestParams = new HashMap<String, String>();
        if (isSsl) {
            requestParams.put("isSsl", "1");
        }
        return Json.jsonToObject(this.request("GET", "/resources/" + no + "/downloadUrl", requestParams, new HashMap<>(), "root"), Map.class);
    }

    public Resource rename(String no, String name) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map<String, String> params = new HashMap();
        params.put("name", name);
        return Json.jsonToObject(this.request("PUT", "/resources/" + no + "/name", params, new HashMap<>(), "root"), Resource.class);
    }

    public String delete(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return this.request("DELETE", "/resources/" + no, new HashMap<>(), new HashMap<>(), "root");
    }

    public Map startUpload(Map<String, String> params) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        return Json.jsonToObject(this.request("POST", "/upload/start", params, new HashMap<>(), "root"), Map.class);
    }

    public Map finishUpload(String no) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map<String, String> params = new HashMap();
        params.put("no", no);
        return Json.jsonToObject(this.request("POST", "/upload/finish", params, new HashMap<>(), "root"), Map.class);
    }

    public Map upload(File file, String extno) throws IOException, URISyntaxException {
        Map params = new HashMap();
        params.put("name", file.getName());
        params.put("extno", extno);
        Map token = startUpload(params);

        Recorder recorder = new FileRecorder(recordPath);
        UploadManager uploadManager = new UploadManager(recorder);
        uploadManager.upload(file, token.get("reskey").toString(), token.get("uploadToken").toString());

        return finishUpload(token.get("no").toString());
    }

    // 续传接口，传入之前失败的云资源no
    public Map upload(File file, String extno, String resumeNo) throws IOException, URISyntaxException {
        Map params = new HashMap();
        params.put("name", file.getName());
        params.put("extno", extno);
        params.put("resumeNo", resumeNo);
        Map token = startUpload(params);

        Recorder recorder = new FileRecorder(recordPath);
        UploadManager uploadManager = new UploadManager(recorder);
        uploadManager.upload(file, token.get("reskey").toString(), token.get("uploadToken").toString());

        return finishUpload(token.get("no").toString());
    }
}
