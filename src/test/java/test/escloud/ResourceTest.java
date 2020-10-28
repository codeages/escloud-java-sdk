package test.escloud;

import com.escloud.Auth;
import com.escloud.domain.Resource;
import com.escloud.httpClient.Client;
import com.escloud.service.ResourceService;
import com.escloud.util.Json;
import com.google.gson.Gson;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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


