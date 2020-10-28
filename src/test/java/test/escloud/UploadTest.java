package test.escloud;

import com.escloud.Auth;
import com.escloud.httpClient.Client;
import com.escloud.service.UploadService;
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
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UploadTest {

    private UploadService uploadService;

    private Client mockClient;

    @Before
    public void setUp() throws Exception {
        String accessKey = "your access key";
        String secretKey = "your secret key";

        Auth auth = new Auth(accessKey, secretKey);
        this.uploadService = new UploadService(auth, new HashMap<>());
        mockClient = mock(Client.class);
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

        this.uploadService.setClient(this.mockClient);

        Map result = this.uploadService.startUpload(params);

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


        this.uploadService.setClient(this.mockClient);
        Map result = this.uploadService.finishUpload("test_no1");


        Assert.assertEquals(mockResult.get("no"), result.get("no"));
        Assert.assertEquals(mockResult.get("extno"), result.get("extno"));
        Assert.assertEquals(mockResult.get("name"), result.get("name"));
        Assert.assertEquals(mockResult.get("size"), ((Double) result.get("size")).intValue());
        Assert.assertEquals(mockResult.get("length"), ((Double) result.get("length")).intValue());
    }

    @Test
    public void UploadTest() throws URISyntaxException, IOException {
        File file = new File("/var/www/edusoho.mp4");
        this.uploadService.upload(file, "111112323333");
    }
}


