package com.escloud.service;

import com.escloud.Auth;
import com.escloud.service.upload.FileRecorder;
import com.escloud.service.upload.Recorder;
import com.escloud.service.upload.UploadManager;
import com.escloud.util.Json;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源服务
 */
public class UploadService extends BaseService {
    protected String host = "resource-service.qiqiuyun.net";

    public UploadService(Auth auth, Map<String, String> options) {
        options.put("host", this.host);
        init(auth, options);
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

        String recordPath = "logs/tmp/";
        Recorder recorder = new FileRecorder(recordPath);
        UploadManager uploadManager = new UploadManager(recorder);
        uploadManager.upload(file, token.get("reskey").toString(), token.get("uploadToken").toString());

        return finishUpload(token.get("no").toString());
    }

    public Map resumeUpload(File file, String extno, String resumeNo) throws IOException, URISyntaxException {
        Map params = new HashMap();
        params.put("name", file.getName());
        params.put("extno", extno);
        params.put("resumeNo", resumeNo);
        Map token = startUpload(params);

        String recordPath = "logs/tmp/";
        Recorder recorder = new FileRecorder(recordPath);
        UploadManager uploadManager = new UploadManager(recorder);
        uploadManager.upload(file, token.get("reskey").toString(), token.get("uploadToken").toString());

        return finishUpload(token.get("no").toString());
    }
}
