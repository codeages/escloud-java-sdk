import com.codeages.escloud.Auth;
import com.codeages.escloud.domain.Resource;
import com.codeages.escloud.service.ResourceService;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class ResourceDemo {
    String accessKey = "your access key";
    String secretKey = "your secret key";
    Auth auth = new Auth(accessKey, secretKey);
    ResourceService resourceService = new ResourceService(auth, new HashMap<String, String>());

    public Resource get() {
        try {
            return resourceService.get("577f3b833bcd4da4a1e3efa291b2c573");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        new ResourceDemo().get();
    }
}
