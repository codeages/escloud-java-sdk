//package test.escloud;
//
//import com.escloud.Auth;
//import com.escloud.httpClient.Client;
//import com.escloud.service.ResourceService;
//import com.escloud.service.SmsService;
//import com.google.gson.Gson;
//import org.json.JSONException;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class SmsTest {
//    private SmsService smsService;
//
//    private Client mockClient;
//
//    @Before
//    public void setUp() throws Exception {
//        String accessKey = "vqF0cR7TIpbIh1mJxS55vn6QgILGHCzF";
//        String secretKey = "wyOV9rew98ClmpuklnT1Y80omjc7ZLel";
//
//        Auth auth = new Auth(accessKey, secretKey);
//        this.smsService = new SmsService(auth, new HashMap<>());
//        mockClient = mock(Client.class);
//    }
//
////    @Test
////    public void startUploadTest() throws UnsupportedEncodingException, URISyntaxException, JSONException {
//////        Map mockResult = new HashMap();
//////        mockResult.put("no", "test_no_1");
//////        mockResult.put("extno", "test_extno_1");
//////        mockResult.put("uploadToken", "upload_token_1");
//////        mockResult.put("uploadUrl", "//upload.qiqiuyun.net");
//////        mockResult.put("reskey", "1577089429/5e007995f3d5794220468");
//////
//////        when(this.mockClient.request(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap(), Mockito.anyMap())).thenReturn(new Gson().toJson(mockResult));
////
////        Map<String, String> params = new HashMap();
//////        params.put("name", "test.mp4");// 文件名
//////        params.put("extno", "extno_test_1");// 业务端自己的资源标识
////
//////        this.smsService.setClient(this.mockClient);
////
////        String result = this.smsService.sendToOne(params);
////
////        System.out.print(result);
//    }
//}
