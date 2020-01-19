package com.escloud.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Client {

    public String request(String method, String uri, Map<String, String> params, Map<String, String> headers) throws UnsupportedEncodingException, URISyntaxException, JSONException {
        HttpUriRequest request = null;
        if (method.equals(HttpMethodType.GET)) {
            HttpGet get = new HttpGet();
            if (!params.isEmpty()) {
                StringBuilder uriBuilder = new StringBuilder(uri.indexOf("?") > 0 ? uri + "&" : uri + "?");
                for (String key : params.keySet()) {
                    uriBuilder.append(params.get(key)).append("=");
                }
                uri = uriBuilder.toString();

                uri = uri.substring(0, uri.length() - 1);
            }
            get.setURI(new URI(uri));
            request = get;
        } else if (method.equals(HttpMethodType.POST)) {
            HttpPost post = new HttpPost();
            post.setURI(new URI(uri));
            post.setEntity(new StringEntity(prepareParams(params), "UTF-8"));
            request = post;
        } else if (method.equals(HttpMethodType.PUT)) {
            HttpPut put = new HttpPut();
            put.setURI(new URI(uri));
            put.setEntity(new StringEntity(prepareParams(params), "UTF-8"));
            request = put;
        } else if (method.equals(HttpMethodType.PATCH)) {
            HttpPatch patch = new HttpPatch();
            patch.setURI(new URI(uri));
            patch.setEntity(new StringEntity(prepareParams(params), "UTF-8"));
            request = patch;
        } else if (method.equals(HttpMethodType.DELETE)) {
            HttpDelete delete = new HttpDelete();
            delete.setURI(new URI(uri));
            request = delete;
        }

        if (request != null) {
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.setHeader(key, headers.get(key));
                }
            }
        }
        CloseableHttpResponse httpResponse = null;

        try {
            CloseableHttpClient httpclient = null;
            httpclient = HttpClients.createDefault();

            httpResponse = httpclient.execute(request);
            HttpEntity responseEntity = httpResponse.getEntity();

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new Exception(EntityUtils.toString(responseEntity));
            }
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String prepareParams(Map<String, String> params) throws JSONException {
        JSONObject json = new JSONObject();
        for (String key : params.keySet()) {
            json.put(key, params.get(key));
        }

        return json.toString();
    }

    private static class HttpMethodType {
        private static final String GET = "GET";

        private static final String PUT = "PUT";

        private static final String POST = "POST";

        private static final String DELETE = "DELETE";

        private static final String PATCH = "PATCH";
    }
}
