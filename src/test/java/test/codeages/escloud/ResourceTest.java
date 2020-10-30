package test.codeages.escloud;

import com.codeages.escloud.Auth;
import com.codeages.escloud.domain.Resource;
import com.codeages.escloud.httpClient.Client;
import com.codeages.escloud.service.ResourceService;
import com.codeages.escloud.util.Json;
import com.google.gson.Gson;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ResourceTest {

    private ResourceService resourceService;

    private Client mockClient;

    @Before
    public void setUp() throws Exception {
        String accessKey = "your access key";
        String secretKey = "your secret key";

        Auth auth = new Auth(accessKey, secretKey);
        this.resourceService = new ResourceService(auth, new HashMap<>());
        mockClient = mock(Client.class);
    }

    @Test
    public void getTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map mockResult = mockResource();
        Gson gson = new Gson();

        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(gson.toJson(mockResult));


        this.resourceService.setClient(this.mockClient);
        Resource result = this.resourceService.get("test_no1");

        Assert.assertEquals(mockResult.get("no"), result.getNo());
        Assert.assertEquals(mockResult.get("extno"), result.getExtno());
        Assert.assertEquals(mockResult.get("name"), result.getName());
        Assert.assertEquals(mockResult.get("size"), result.getSize());
        Assert.assertEquals(mockResult.get("length"), result.getLength());
    }

    @Test
    public void searchTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        String mockResult = new String("[{\"no\":\"test_no_1\",\"extno\":\"test_extno_1\",\"name\":\"test.mp4\",\"type\":\"video\",\"size\":\"102400\",\"length\":\"200\",\"thumbnail\":\"\",\"processStatus\":\"ok\",\"isShare\":\"1\",\"processedTime\":\"1543299710\",\"createdTime\":\"1543299685\",\"updatedTime\":\"0\"}]");
        List<Resource> mockResultResources = Json.jsonToObjectList(mockResult, Resource.class);
        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(mockResult);
        this.resourceService.setClient(this.mockClient);

        Map params = new HashMap();
        params.put("type", "video");

        List<Resource> result = this.resourceService.search(params);

        Assert.assertEquals(mockResultResources.get(0).getNo(), result.get(0).getNo());
        Assert.assertEquals(mockResultResources.get(0).getExtno(), result.get(0).getExtno());
        Assert.assertEquals(mockResultResources.get(0).getName(), result.get(0).getName());
        Assert.assertEquals(mockResultResources.get(0).getSize(), result.get(0).getSize());
        Assert.assertEquals(mockResultResources.get(0).getLength(), result.get(0).getLength());
    }

    @Test
    public void getDownloadUrlTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map mockResult = new HashMap();
        mockResult.put("downloadUrl", "http://www.test.com/xxxx");

        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(new Gson().toJson(mockResult));
        this.resourceService.setClient(this.mockClient);

        Map params = new HashMap();
        params.put("type", "video");

        Map result = this.resourceService.getDownloadUrl("test_no_1");

        Assert.assertEquals(mockResult.get("downloadUrl"), result.get("downloadUrl"));
    }

    @Test
    public void renameTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map mockResult = mockResource();

        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(new Gson().toJson(mockResult));
        this.resourceService.setClient(this.mockClient);

        Map params = new HashMap();
        params.put("type", "video");

        Resource result = this.resourceService.rename("test_no_1", "test.mp4");

        Assert.assertEquals(mockResult.get("name"), result.getName());
    }

    @Test
    public void deleteTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(new Gson().toJson(true));
        this.resourceService.setClient(this.mockClient);

        Map params = new HashMap();
        params.put("type", "video");

        String result = this.resourceService.delete("test_no_1");

        Assert.assertEquals(result, "true");
    }

    @Test
    public void startUploadTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map mockResult = new HashMap();
        mockResult.put("no", "test_no_1");
        mockResult.put("extno", "test_extno_1");
        mockResult.put("uploadToken", "upload_token_1");
        mockResult.put("uploadUrl", "//upload.qiqiuyun.net");
        mockResult.put("reskey", "1577089429/5e007995f3d5794220468");

        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(new Gson().toJson(mockResult));

        Map<String, String> params = new HashMap();
        params.put("name", "test.mp4");// 文件名
        params.put("extno", "extno_test_1");// 业务端自己的资源标识
        params.put("resumeNo", "no_test_1");// 如果之前有上传失败的，将no保存到本地，然后传入这里，将会进行续传

        this.resourceService.setClient(this.mockClient);

        Map result = this.resourceService.startUpload(params);

        Assert.assertEquals(result.get("no"), result.get("no"));
        Assert.assertEquals(mockResult.get("uploadToken"), result.get("uploadToken"));
        Assert.assertEquals(mockResult.get("extno"), result.get("extno"));
        Assert.assertEquals(mockResult.get("uploadUrl"), result.get("uploadUrl"));
        Assert.assertEquals(mockResult.get("reskey"), result.get("reskey"));
    }

    @Test
    public void finishUploadTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
        Map mockResult = new HashMap();
        mockResult.put("no", "test_no_1");//资源唯一编号
        mockResult.put("extno", "test_extno_1");
        mockResult.put("name", "test.mp4");
        mockResult.put("size", 102400);// 文件大小
        mockResult.put("length", 200);// 视频时长，PPT页数
        Gson gson = new Gson();

        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(gson.toJson(mockResult));


        this.resourceService.setClient(this.mockClient);
        Map result = this.resourceService.finishUpload("test_no1");


        Assert.assertEquals(mockResult.get("no"), result.get("no"));
        Assert.assertEquals(mockResult.get("extno"), result.get("extno"));
        Assert.assertEquals(mockResult.get("name"), result.get("name"));
        Assert.assertEquals(mockResult.get("size"), ((Double) result.get("size")).intValue());
        Assert.assertEquals(mockResult.get("length"), ((Double) result.get("length")).intValue());
    }

    @Test
    public void UploadTest() throws URISyntaxException, IOException {
        File file = new File("/var/www/edusoho.mp4");
        this.resourceService.upload(file, "111112323333");
    }

    protected Map mockResource() {
        Map resource = new HashMap();
        resource.put("no", "test_no_1");
        resource.put("extno", "test_extno_1");
        resource.put("name", "test.mp4");
        resource.put("type", "video");
        resource.put("size", 102400);
        resource.put("length", 200);
        resource.put("thumbnail", "");
        resource.put("processStatus", "ok");
        resource.put("isShare", 1);
        resource.put("processedTime", "1548915578");
        resource.put("createdTime", "1548915578");
        resource.put("updatedTIme", "1548915578");

        return resource;
    }
}


